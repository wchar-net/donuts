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
        $(document).find("#editDeptId").val(null);
        $(document).find("#editDeptName").val(null);
        window.modalInstance = $.modal({
            url: '/dept/addDept.action',
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
        $('#deptName').val('');
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
        url: '/dept/getDeptList.action',
        idField: 'deptId',
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
        loadingTemplate: function () {
            return '<div class="spinner-grow" role="status"><span class="visually-hidden">Loading...</span></div>';
        },
        queryParams: function (params) {
            return {
                deptName: $("#deptName").val()
            };
        },
        columns: [
            {
                field: 'deptId',
                visible: false
            },
            {
                field: 'deptName',
                title: '部门名称',
                width: '300px',
                align: 'center'
            },
            {
                field: 'deptCode',
                align: 'center',
                width: '300px',
                title: '部门编码'
            },
            {
                title: '负责人',
                field: 'deptLeader',
                width: '80px',
                align: 'center'
            },
            {
                field: 'deptStatus',
                title: '部门状态',
                width: '80px',
                align: 'center',
                formatter: formatDeptType
            },
            {
                field: 'deptPhone',
                title: '联系电话',
                width: '300px',
                align: 'center',
            },
            {
                field: 'deptEmail',
                title: '邮箱',
                width: '300px',
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
                        $("#editDeptId").val(row.deptId);
                        $("#editDeptName").val(row.deptName);
                        window.modalInstance = $.modal({
                            url: '/dept/editDept.action',
                            title: '修改部门',
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
                            body: `确定要删除部门[ ${row.deptName} ]吗？`,
                            ok: function () {
                                $.ajax({
                                    url: '/dept/removeDept.action',
                                    method: 'post',
                                    data: {
                                        deptId: row.deptId
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
                        $("#editDeptId").val(row.deptId);
                        $("#editDeptName").val(row.deptName);
                        window.modalInstance = $.modal({
                            url: '/dept/addDept.action',
                            title: '添加部门',
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
        treeShowField: 'deptName',
        parentIdField: 'deptParentId',
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


function formatDeptType(value, row, index) {
    if (value === 1) {
        return '<span class="badge text-bg-success">正常</span>'
    } else {
        return '<span class="badge text-bg-danger">禁用</span>'
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
        data-bs-title="添加部门">
        <i class="bi bi-plus"></i>
        </button>`
    //第二个按钮
    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light btn-sm edit-btn mx-1" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="修改部门"><i class="bi bi-pencil"></i></button>`
    //第三个按钮
    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light btn-sm del-btn" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="删除部门"><i class="bi bi-trash3"></i></button>`

    return html;
}