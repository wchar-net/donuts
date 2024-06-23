$(function (){
    // 密码可见性切换
    $('#toggle-password').on('click', function() {
        let passwordField = $('#userPwd');
        let passwordFieldType = passwordField.attr('type');
        let passwordToggleIcon = $('#toggle-password-icon');

        if (passwordFieldType === 'password') {
            passwordField.attr('type', 'text');
            passwordToggleIcon.removeClass('bi-eye').addClass('bi-eye-slash');
        } else {
            passwordField.attr('type', 'password');
            passwordToggleIcon.removeClass('bi-eye-slash').addClass('bi-eye');
        }
    });
})


function deptClick(){
    let zTree;
    let setting = {
        data: {
            key: {
                name: "deptName" // 指定节点显示的名称字段
            },
            view: {
                showIcon: true,
            },
            simpleData: {
                enable: true,
                idKey: "deptId", // 指定节点的 id 字段
                pIdKey: "deptParentId", // 指定父 id 字段
                rootPId: 0 // 根节点的父 id，这里设置为 0
            },
        },
        callback: {
            onClick: function(event, treeId, treeNode) {
                $('#userDeptName').val(treeNode.deptName);
                $('#userDeptId').val(treeNode.deptId);
                $('#form').formValidation('revalidateField', 'userDeptName');
                $('#treeModal').modal('hide');
            }
        }
    };
    $.ajax({
        type: "GET",
        url: "/dept/getDeptList.action", // 替换为实际的 API 接口地址
        dataType: "json",
        success: function (response) {
            let data = response.data;
            zTree = $.fn.zTree.init($("#treeDemo"), setting, data);
            zTree.expandAll(true);
        }
    });
    $('#treeModal').modal('show');
}
//下拉框美化插件，原生的bootstrap它会调用系统的那个下拉菜单
$('select').selectpicker();
let croppie = new Croppie(document.querySelector('#croppie-wraper'), {
    viewport: {width: 200, height: 200, type: 'circle'},
    boundary: {width: 300, height: 300},
    showZoomer: true,
    enableOrientation: true
});
function selectFile() {
    document.querySelector('#fileinput').dispatchEvent(new MouseEvent('click'));
}
document.querySelector('#fileinput').addEventListener('change', function (e) {
    let selectFileList = e.target.files;
    let reader = new FileReader();
    reader.readAsDataURL(selectFileList[0]);
    reader.onload = function (ex) {
        croppie.bind({
            url: ex.target.result,
            orientation: 1,
            zoom: 0
        }).then(() => {
            updatePreview();
        });
    }
});
document.querySelectorAll('.rotate').forEach((el) => {
    el.addEventListener('click', function (event) {
        event.preventDefault();
        let deg = this.dataset.deg;
        croppie.rotate(parseInt(deg));
        updatePreview();
    });
});
document.querySelector('#croppie-result').addEventListener('click', function (event) {
    croppie.result('blob').then(function (blob) {
        let formData = new FormData();
        formData.set('file', blob, 'cropped.png');
        $.ajax({
            url: "/file/upload.action",
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success: function (res) {
                if (res.code === "1") {
                    document.querySelector('#userAvatar').src = "/file/" +  res.data;
                    $('#exampleModal').modal('hide');
                    $("#userAvatarReal").val(res.data);
                }
            }
        });
    });
});

croppie.element.addEventListener('update', function () {
    updatePreview();
});

function updatePreview() {
    croppie.result('base64').then(function (blob) {
        document.querySelector('#preview1').src = blob;
        document.querySelector('#preview2').src = blob;
    });
}

$("#form").formValidation({
    locale: getLang(),
    //验证字段
    fields: {
        userEmail: {
            validators: {
                emailAddress: true
            }
        },
        userType: {
            validators: {
                notEmpty: true
            }
        },
        userDeptName: {
            validators: {
                notEmpty: true
            }
        },
        userSex:{
            validators: {
                notEmpty: true
            }
        },
        userNick: {
            validators: {
                stringLength: {
                    max: 15
                }
            }
        },
        userPwd: {
            validators: {
                notEmpty: true,
                stringLength: {
                    min:6,
                    max: 20
                }
            }
        },
        userName: {
            validators: {
                notEmpty: true,
                stringLength: {
                    min:3,
                    max: 8
                },
                regexp: {
                    regexp: /^[a-zA-Z0-9]+$/,
                    message: '只能是字母或数字'
                }
            },
        }
    }
}).on('success.form.fv', function (e) {
    //阻止表单提交
    e.preventDefault();
    let $form = $(e.target);
    let formDataArray = $form.serializeArray();
    let formData = {};
    $(formDataArray).each(function(index, obj){
        formData[obj.name] = obj.value;
    });
    let formDataJSON = JSON.stringify(formData);

    $.ajax({
        url: "/user/addUser.action",
        method: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: formDataJSON,
        success: function (res) {
            if (res.code === "1") {
                $.toasts({
                    type: 'success',
                    content: '保存成功',
                    delay: 1000,
                    autohide: true,
                    onHidden: function () {
                        parent.modalInstance.setData(true);
                        parent.modalInstance.close();
                    }
                })
            }
        }
    })
});