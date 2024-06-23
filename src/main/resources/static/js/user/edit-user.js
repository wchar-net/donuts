function deptClick() {
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
            onClick: function (event, treeId, treeNode) {
                $('#userDeptName').val(treeNode.deptName);
                $('#userDeptId').val(treeNode.deptId);
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
let url = "/user/getUserDetail.action?userId=" + $(parent.document).find("#assignUserId").val();
$.ajax({
    url: url,
    method: 'get',
}).then(response => {
    if (response.code === "1") {
        $("#userId").val(response.data.userId);
        $("#userName").val(response.data.userName);
        $("#userNick").val(response.data.userNick);
        let userType = response.data.userType;
        if (userType === 0) {
            $("#userType-1").prop("checked", true);
        }
        if (userType === 1) {
            $("#userType-2").prop("checked", true);
        }
        $("#userEmail").val(response.data.userEmail);
        $("#userPhone").val(response.data.userPhone);
        // 0男 1女 2未知）
        let userSex = response.data.userSex;
        if (userSex === 0) {
            $("#gender-1").prop("checked", true);
        } else if (userSex === 1) {
            $("#gender-2").prop("checked", true);
        } else {
            $("#gender-3").prop("checked", true);
        }
        //头像
        $("#userAvatar").attr("src", `${response.data.userAvatar}`);
        $("#userAvatarReal").val(response.data.userAvatar);

        //用户状态
        $('#userStatus').selectpicker('val', response.data.userStatus.toString());
        //用户部门
        $("#userDeptName").val(response.data.dept.deptName);
    } else {
        $('#edit-cf').remove();
    }
});
//实例化头像裁剪插件
let croppie = new Croppie(document.querySelector('#croppie-wraper'), {
    viewport: {width: 200, height: 200, type: 'circle'},
    boundary: {width: 300, height: 300},
    showZoomer: true,
    enableOrientation: true
});
document.querySelector('#croppie-wraper').addEventListener('update', function (ev) {
    let cropData = ev.detail;
    if (cropData.orientation !== undefined) {
        croppie.result('base64', 'viewport').then(function (blob) {
            document.querySelector('#preview1').setAttribute('src', blob);
            document.querySelector('#preview2').setAttribute('src', blob);
        })
    }
});

// 选择文件
function selectFile() {
    document.querySelector('#fileinput').dispatchEvent(new MouseEvent('click'))
}

document.querySelector('#fileinput').addEventListener('change', function (e) {
    let selectFileList = e.target.files;
    //回显头像
    let reader = new FileReader();
    reader.readAsDataURL(selectFileList[0]);
    reader.onload = function (ex) {
        croppie.bind({
            url: ex.target.result,
            orientation: 1,
            //0:最小化现实 1:缩放显示
            zoom: 0
        });
    }
});


//旋转按钮操作
document.querySelectorAll('.rotate').forEach((el) => {
    el.addEventListener('click', function (event) {
        event.preventDefault();
        let deg = this.dataset.deg;
        croppie.rotate(parseInt(deg));
    });
});


//确定按钮裁剪图片
document.querySelector('#croppie-result').addEventListener('click', function (event) {
    croppie.result('blob', 'viewport').then(function (blob) {
        //创建forData表单对象
        let formData = new FormData();
        //添加头像
        formData.set('file', blob, 'a.png');
        $.ajax({
            url: "/file/upload.action",
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success: function (res) {
                if (res.code === "1") {
                    document.querySelector('#userAvatar').src = "/file/" + res.data;
                    $('#exampleModal').modal('hide');
                    $("#userAvatarReal").val(res.data);
                }
            }
        });
    });
});

$("#form").formValidation({
    //验证字段
    fields: {
        userEmail: {
            validators: {
                emailAddress: true
            }
        },
        userNick: {
            validators: {
                notEmpty: true,
                stringLength: {
                    max: 20
                }
            }
        }
    }
}).on('success.form.fv', function (e) {
    //阻止表单提交
    e.preventDefault();
    let $form = $(e.target);
    let formDataArray = $form.serializeArray();
    let formData = {};
    $(formDataArray).each(function (index, obj) {
        formData[obj.name] = obj.value;
    });
    let formDataJSON = JSON.stringify(formData);

    $.ajax({
        url: "/user/modifyUser.action",
        method: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: formDataJSON,
        success: function (res) {
            if (res.code === "1") {
                $.toasts({
                    type: 'success',
                    content: '修改成功',
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
