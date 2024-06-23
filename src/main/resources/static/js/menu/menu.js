$(function () {
    //展开折叠
    let t = false;
    $('.expandAll').on('click', function () {
        t = !t;
        if (t) {
            $('#table').treegrid('collapseAll');
        } else {
            $('#table').treegrid('expandAll');
        }
    })


    //搜索处理
    $('.bsa-querySearch-btn').on('click', function () {
        $('#table').bootstrapTable('refresh');
    })


    //新增
    $('.btn-add').on('click', function () {
        $(document).find("#editMenuId").val(null);
        $(document).find("#editMenuName").val(null);
        window.modalInstance = $.modal({
            url: '/menu/addMenu.action',
            title: '添加菜单',
            //禁用掉底部的按钮区域
            buttons: [],
            modalDialogClass: 'modal-dialog-centered modal-lg',
            onHidden: function (obj, data) {
                if (data === true) {
                    $('#table').bootstrapTable('refresh');
                }
            }
        })
    })

    //重置处理
    $('.bsa-reset-btn').on('click', function () {
        //把所有的字段都恢复默认值
        $('#menuName').val('');
        //刷新回到第一页
        $('#table').bootstrapTable('refresh');
    })
    $('#table').bootstrapTable({
        //处理返回的格式
        responseHandler: function (res) {
            if (res.code === "1") {
                return {
                    total: res.data.total,
                    rows: res.data
                };
            } else {
                $.toasts({
                    type: 'danger',
                    delay: 2000,
                    content: res.msg
                })
                return {
                    total: 0,
                    rows: []
                };
            }
        },
        locale: getLang(),
        url: '/menu/getMenuList.action',
        idField: 'menuId',
        showColumns: true,
        fixedColumns: true,
        // 显示图标
        showButtonIcons: true,
        // 是否显示详细视图和列表视图的切换按钮
        showToggle: true,
        // 是否显示刷新按钮
        showRefresh: true,
        // 按钮的类
        buttonsClass: 'light',
        // 类名前缀
        buttonsPrefix: 'btn',
        // 工具按钮容器
        toolbar: '#toolbar',
        fixedNumber: 1,
        //右侧固定列数
        fixedRightNumber: 1,
        undefinedText: '',
        //加载模板,不改的话，默认的会超出不好看
        loadingTemplate: function () {
            return '<div class="spinner-grow" role="status"><span class="visually-hidden">Loading...</span></div>';
        },
        queryParams: function (params) {
            return {
                menuName: $("#menuName").val()
            };
        },
        columns: [
            {
                field: 'menuId',
                visible: false
            },
            {
                field: 'menuName',
                title: '菜单名称',
                width: '300px',
                formatter: formatMenuName
            },
            {
                field: 'url',
                align: 'center',
                width: '300px',
                title: '访问路径'
            },
            {
                title: '菜单类型',
                field: 'menuType',
                width: '80px',
                align: 'center',
                formatter: formatMenuType
            },
            {
                field: 'perms',
                title: '权限标识',
                width: '300px',
                align: 'center',
            },
            {
                field: 'visible',
                title: '可见',
                align: 'center',
                width: '100px',
                formatter: formatVisible
            },
            {
                field: 'target',
                align: 'center',
                title: '打开方式',
                width: '80px',
                formatter: formatTarget
            },
            {
                field: 'orderNum',
                title: '排序',
                width: '80px',
                align: 'center',
            },
            {
                title: '创建时间',
                field: 'createTime',
                align: 'center',
                width: '100px',
            },
            {
                title: '操作',
                align: 'center',
                formatter: formatAction,
                events: {
                    'click .edit-btn': function (event, value, row, index) {
                        event.stopPropagation();
                        $("#editMenuId").val(row.menuId);
                        $("#editMenuName").val(row.menuName);
                        window.modalInstance = $.modal({
                            url: '/menu/editMenu.action',
                            title: '修改菜单',
                            //禁用掉底部的按钮区域
                            buttons: [],
                            modalDialogClass: 'modal-dialog-centered modal-lg',
                            onHidden: function (obj, data) {
                                if (data === true) {
                                    //刷新当前数据表格
                                    $('#table').bootstrapTable('refresh');
                                }
                            }
                        })

                    },
                    'click .del-btn': function (event, value, row, index) {
                        event.stopPropagation();
                        $.modal({
                            body: `确定要删除节点[ ${row.menuName} ]吗？`,
                            ok: function () {
                                $.ajax({
                                    url: '/menu/removeMenu.action',
                                    method: 'post',
                                    data: {
                                        menuId: row.menuId
                                    }
                                }).then(response => {
                                    if (response.code === "1") {
                                        $.toasts({
                                            type: 'success',
                                            delay: 2000,
                                            content: response.msg,
                                        })
                                        $('#table').bootstrapTable('refresh');
                                    }
                                });
                            }
                        })
                    },
                    'click .add-btn': function (event, value, row, index) {
                        event.stopPropagation();
                        $("#editMenuId").val(row.menuId);
                        $("#editMenuName").val(row.menuName);
                        window.modalInstance = $.modal({
                            url: '/menu/addMenu.action',
                            title: '添加菜单',
                            //禁用掉底部的按钮区域
                            buttons: [],
                            modalDialogClass: 'modal-dialog-centered modal-lg',
                            onHidden: function (obj, data) {
                                if (data === true) {
                                    $('#table').bootstrapTable('refresh');
                                }
                            }
                        })
                    },
                }
            }
        ],
        treeShowField: 'menuName',
        parentIdField: 'parentId',
        rootParentId: "0",
        onPostBody: function () {
            //重新启用弹出层,否则formatter格式化函数返回的html字符串上的tooltip提示不生效
            $('[data-bs-toggle="tooltip"]').each(function (i, el) {
                new bootstrap.Tooltip(el)
            })

            //jquery-treegrid的相关逻辑
            let columns = $('#table').bootstrapTable('getOptions').columns;
            if (columns && columns[0][1].visible) {
                $('#table').treegrid({
                    initialState: 'expanded', // 初始状态：collapsed 折叠或 expanded 展开
                    treeColumn: 0, // 树形结构列索引，从0开始
                    onChange: function () {
                        $('#table').bootstrapTable('resetWidth');
                    }
                })
            }

        }
    })
})

function formatVisible(value, row, index) {
    if (value === 1) {
        return '<span class="badge text-bg-success">可见</span>';
    } else {
        return '<span class="badge text-bg-danger">隐藏</span>';
    }
}

function formatMenuName(value, row, index) {
    return `<i class="${row.icon}"></i> ${row.menuName}`;
}

function formatTarget(value, row, index) {
    if (value === 1) {
        return '<span class="badge text-bg-success">内部</span>';
    } else {
        return '<span class="badge text-bg-danger">新窗口</span>';
    }
}


function formatMenuType(value, row, index) {
    if (value === 1) {
        return '<span class="badge text-bg-success">目录</span>'
    } else if (value === 2) {
        return '<span class="badge text-bg-danger">菜单</span>'
    } else if (value === 3) {
        return '<span class="badge text-bg-primary">按钮</span>'
    }
}

function showTooltip(that) {
    $(that).tooltip('show')
}

function hideTooltip(that) {
    $(that).tooltip('hide')
}

// 这里关于给节点新增节点可以自己增加一个判断,比如你后端只能允许最大三个层级之类的。
function formatAction(val, rows) {
    let html = '';
    //第一个按钮
    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light btn-sm add-btn" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="添加菜单">
        <i class="bi bi-plus"></i>
        </button>`
    //第二个按钮
    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light btn-sm edit-btn mx-1" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="编辑菜单"><i class="bi bi-pencil"></i></button>`
    //第三个按钮
    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light btn-sm del-btn" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="删除菜单"><i class="bi bi-trash3"></i></button>`

    return html;
}