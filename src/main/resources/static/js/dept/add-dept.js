$(function () {

    $("#deptParentId").val($(parent.document).find("#editDeptId").val());
    $("#deptParentName").val($(parent.document).find("#editDeptName").val());
    if ($(parent.document).find("#editDeptName").val() != "") {
        $(".clear-icon").show();
    }

    // 点击清空图标时清空输入框内容并隐藏图标
    $('.clear-icon').click(function () {
        $('#deptParentName').val('').focus();
        $(this).hide();
    });
})

function handlerChildren(data) {
    if (Array.isArray(data.children)) {
        if (data.children.length === 0) {
            data.children = null;
        } else {
            data.children.forEach(child => handlerChildren(child));
        }
    }
}

function deptTreeClick() {
    let zTree;
    let setting = {
        data: {
            key: {
                name: "deptName", // 指定节点显示的名称字段
                children: "children"
            },
            view: {
                showIcon: true
            },
            simpleData: {
                enable: true,
                idKey: "deptId",
                pIdKey: "deptParentId",
                rootPId: 0
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                $('#deptParentName').val(treeNode.deptName);
                $('#deptParentId').val(treeNode.deptId);
                $('#form').formValidation('revalidateField', 'deptParentName');
                $(".clear-icon").show();
                $('#treeModal').modal('hide');
            }
        }
    };
    $.ajax({
        type: "GET",
        url: "/dept/getDeptTree.action",
        dataType: "json",
        success: function (response) {
            let data = response.data;
            handlerChildren(data);
            zTree = $.fn.zTree.init($("#treeDemo"), setting, data);
            zTree.expandAll(true);
        }
    });
    $('#treeModal').modal('show');
}

//下拉框美化插件，原生的bootstrap它会调用系统的那个下拉菜单
$('select').selectpicker();

$("#form").formValidation({
    locale: getLang(),
    //验证字段
    fields: {
        deptName: {
            validators: {
                notEmpty: true,
                stringLength: {
                    max: 15,
                    min: 3
                }
            }
        },
        deptCode: {
            validators: {
                notEmpty: true,
                stringLength: {
                    max: 15,
                    min: 3
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
        url: "/dept/addDept.action",
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
}).on('success.field.fv', function (e, data) {
    $(data.element).removeClass('is-valid');
})

