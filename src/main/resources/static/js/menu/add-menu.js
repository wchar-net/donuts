document.addEventListener('DOMContentLoaded', function () {
    //添加传递过来的 父菜单id
    $("#parentId").val($(parent.document).find("#editMenuId").val());
    $("#menuParentName").val($(parent.document).find("#editMenuName").val());
    if ($(parent.document).find("#editMenuName").val() != "") {
        $(".clear-icon").show();
    }

    // 点击清空图标时清空输入框内容并隐藏图标
    $('.clear-icon').click(function () {
        $('#menuParentName').val('').focus(); // 清空输入框内容并聚焦到输入框
        $(this).hide(); // 隐藏清空图标
    });

    // 监听图标输入框点击事件
    let iconInput = document.getElementById('icon');
    let iconDropdown = document.getElementById('icon-dropdown');
    let iconSelectItems = iconDropdown.querySelectorAll('.icon-select');
    iconInput.addEventListener('click', function () {
        // 显示图标选择菜单
        iconDropdown.classList.toggle('show');
    });

    // 监听图标选择项点击事件
    iconSelectItems.forEach(function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            let selectedIcon = this.getAttribute('data-icon');
            iconInput.value = selectedIcon;
            $('#form').formValidation('revalidateField', 'icon');
            // 隐藏图标选择菜单
            iconDropdown.classList.remove('show');
        });
    });
    // 点击页面其他地方隐藏图标选择菜单
    document.addEventListener('click', function (e) {
        if (!iconInput.contains(e.target)) {
            iconDropdown.classList.remove('show');
        }
    });

    //当菜单类型是按钮的时候不存在url
    let radioButtons = document.getElementsByName('menuType');
    radioButtons.forEach(function (radioButton) {
        radioButton.addEventListener('click', function () {
            if (this.checked) {
                if (this.checked && this.value === '3') {
                    $("#box_path").hide('fast');
                    $("#box_start_method").hide('fast');
                } else {
                    $("#box_path").show('fast');
                    $("#box_start_method").show('fast');
                }
            }
        });
    });
});


// 递归处理数据函数
function processTreeData(nodes) {
    if (!nodes || nodes.length === 0) return;
    nodes.forEach(function (node) {
        if (node.children && node.children.length === 0) {
            node.children = null; // 将空的 children 数组设置为 null
        } else {
            processTreeData(node.children); // 递归处理子节点
        }
        // 删除 icon 和 url 属性
        delete node.icon;
        delete node.url;
    });
}

function menuTreeClick() {
    let zTree;
    let setting = {
        data: {
            key: {
                name: "menuName", // 指定节点显示的名称字段
                children: "children"
            },
            view: {
                showIcon: true
            },
            simpleData: {
                enable: true,
                idKey: "menuId", // 指定节点的 id 字段
                pIdKey: "parentId", // 指定父 id 字段
                rootPId: 0 // 根节点的父 id，这里设置为 0
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                $('#menuParentName').val(treeNode.menuName);
                $('#parentId').val(treeNode.menuId);
                $('#form').formValidation('revalidateField', 'menuParentName');
                $(".clear-icon").show();
                $('#treeModal').modal('hide');
            }
        }
    };
    $.ajax({
        type: "GET",
        url: "/menu/getMenuTree.action",
        dataType: "json",
        success: function (response) {
            let data = response.data;
            processTreeData(data);
            zTree = $.fn.zTree.init($("#treeDemo"), setting, data);
            zTree.expandAll(true);
        }
    });
    $('#treeModal').modal('show');
}

$('select').selectpicker();

$("#form").formValidation({
    locale: getLang(),
    //验证字段
    fields: {
        menuType: {
            validators: {
                notEmpty: true
            }
        },
        menuName: {
            validators: {
                notEmpty: true,
                stringLength: {
                    max: 15,
                    min: 3
                }
            }
        },
        icon: {
            validators: {
                stringLength: {
                    max: 35
                }
            }
        },
        visible: {
            validators: {
                notEmpty: true,
            }
        },
        target: {
            validators: {
                notEmpty: true,
            }
        },
        orderNum: {
            validators: {
                digits: true
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
        url: "/menu/addMenu.action",
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