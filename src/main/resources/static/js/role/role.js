$(function () {
    $('#table').bootstrapTable({
        //配置语言
        locale: getLang(),
        //设置高度，就可以固定表头
        // height: 500,
        //请求地址
        url: '/role/pageRole.action',
        responseHandler: function (res) {
            if (res.code === "1") {
                return {
                    total: res.total,
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

        //是否开启分页
        pagination: true,
        //是客户端分页还是服务端分页  'client','server',由于演示没有后端提供服务，所以采用前端分页演示
        sidePagination: 'server',
        // 初始化加载第一页，默认第一页
        pageNumber: 1,
        //默认显示几条
        pageSize: 5,
        //固定列功能开启
        fixedColumns: true,
        //左侧固定列数
        fixedNumber: 1,
        //右侧固定列数
        fixedRightNumber: 1,
        //可供选择的每页的行数 - (亲测大于1000存在渲染问题)
        pageList: [5, 10, 25, 50, 100],
        //在上百页的情况下体验较好 - 能够显示首尾页
        paginationLoop: true,
        // 展示首尾页的最小页数
        paginationPagesBySide: 2,

        // 唯一ID字段
        uniqueId: 'roleId',
        // 每行的唯一标识字段
        idField: 'roleId',
        // 是否启用点击选中行
        clickToSelect: true,
        // 是否显示详细视图和列表视图的切换按钮
        showToggle: true,
        // 请求得到的数据类型
        dataType: 'json',
        // 请求方法
        method: 'get',
        // 工具按钮容器
        toolbar: '#toolbar',
        // 是否显示所有的列
        showColumns: true,
        // 是否显示刷新按钮
        showRefresh: true,
        // 显示图标
        showButtonIcons: true,
        // 显示文本
        showButtonText: false,
        // 显示全屏
        showFullscreen: false,
        // 开关控制分页
        showPaginationSwitch: false,
        // 总数字段
        totalField: 'total',
        // 当字段为 undefined 显示
        undefinedText: '',
        // 排序方式
        sortOrder: "asc",

        // 按钮的类
        buttonsClass: 'light',
        // 类名前缀
        buttonsPrefix: 'btn',

        // 图标前缀
        iconsPrefix: 'bi',
        // 图标大小 undefined sm lg
        iconSize: undefined,
        // 图标的设置  这里只做了一个演示，可设置项目参考 https://examples.bootstrap-table.com/#options/table-icons.html
        icons: {
            fullscreen: 'bi-arrows-fullscreen',
        },
        loadingTemplate: function () {
            return '<div class="spinner-grow" role="status"><span class="visually-hidden">Loading...</span></div>';
        },
        onPostBody: function () {
            //重新启用弹出层,否则formatter格式化函数返回的html字符串上的tooltip提示不生效
            $('[data-bs-toggle="tooltip"]').each(function (i, el) {
                new bootstrap.Tooltip(el)
            })
        },
        //头部的那个复选框选中事件
        onCheckAll: function (row) {
            batchBtnStatusHandle()
        },
        //单行选中事件
        onCheck: function (row) {
            batchBtnStatusHandle()
        },
        //单行取消选中事件
        onUncheck: function (row) {
            batchBtnStatusHandle()
        },
        //头部的那个复选框取消选中事件
        onUncheckAll: function (row) {
            batchBtnStatusHandle()
        },
        //加载模板,不改的话，默认的会超出不好看
        loadingTemplate: function () {
            return '<div class="spinner-grow" role="status"><span class="visually-hidden">Loading...</span></div>';
        },
        //params是一个对象
        queryParams: function (params) {
            return {
                pageIndex: Math.ceil(params.offset / params.limit) + 1,
                pageSize: params.limit,
                roleName: $("#roleName").val(),
            }
        },
        //列
        columns: [
            {
                checkbox: true,
                //是否显示该列
                visible: true,
            },
            {
                title: '角色id',
                field: 'roleId',
                align: 'center',
                // 是否作为排序列
                sortable: true,
                // 当列名称与实际名称不一致时可用
                sortName: 'sortId',
                switchable: false,
                // 列的宽度
                width: 8,
                // 宽度单位
                widthUnit: 'rem'
            },
            {
                title: '角色名称',
                field: 'roleName',
                align: 'center'
            },
            {
                title: '状态',
                field: 'status',
                align: 'center',
                formatter: function (value, row, index) {
                    return value === 1 ? '<span class="badge text-bg-success">' + i18n_normal + '</span>' : '<span class="badge text-bg-secondary">' + i18n_deactivate + '</span>';
                }
            },
            {
                title: '创建时间',
                field: 'createTime',
                align: 'center',
            },
            {
                title: '操作',
                align: 'center',
                formatter: formatAction,
                events: {
                    'click .edit-btn': function (event, value, row, index) {
                        event.stopPropagation();
                        $("#roleId").val(row.roleId);
                        window.editRoleModal = $.modal({
                            url: '/role/editRole.action',
                            title: '编辑角色 - ' + row.roleName,
                            //禁用掉底部的按钮区域
                            buttons: [],
                            modalDialogClass: 'modal-dialog-centered modal-xl',
                            onHidden: function (obj, data) {
                                if (data === true) {
                                    $('#table').bootstrapTable('refresh');
                                }
                            }
                        })
                    },
                    'click .del-btn': function (event, value, row, index) {
                        event.stopPropagation();
                        let roleIds = [];
                        roleIds.push(row.roleId);
                        $.modal({
                            body: '确定要删除角色【' + row.roleName + '】?',
                            ok: function () {
                                $.ajax({
                                    url: '/role/batchRemoveRole.action',
                                    method: 'post',
                                    dataType: "json",
                                    contentType: "application/json; charset=utf-8",
                                    data: JSON.stringify({
                                        roleIds: roleIds
                                    }),
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
                    'click .node-btn': function (event, value, row, index) {
                        event.stopPropagation();
                        $("#roleId").val(row.roleId);
                        window.rolemodal = $.modal({
                            url: '/role/assignMenu.action',
                            title: '分配菜单 - ' + row.roleName,
                            //禁用掉底部的按钮区域
                            buttons: [],
                            modalDialogClass: 'modal-dialog-centered modal-xl',
                            onHidden: function (obj, data) {
                                if (data === true) {
                                    $('#table').bootstrapTable('refresh');
                                }
                            }
                        })

                    }
                }
            }
        ]
    });


    // 添加角色
    $('.add-btn').on('click', function () {
        window.addRoleModal = $.modal({
            url: '/role/addRole.action',
            title: '添加角色',
            //禁用掉底部的按钮区域
            buttons: [],
            modalDialogClass: 'modal-dialog-centered modal-xl',
            onHidden: function (obj, data) {
                if (data === true) {
                    $('#table').bootstrapTable('refresh');
                }
            }
        })
    })

    //批量处理
    $('.batch-btn').on('click', function () {
        let selections = $('#table').bootstrapTable('getSelections');
        if (selections.length === 0) {
            Swal.fire({
                icon: 'warning',
                title: '请至少选择一行数据!',
            });
            return;
        }
        let selectedRoleIds = selections.map(function (row) {
            return row.roleId;
        });
        let selectedRoleNames = selections.map(function (row, index) {
            if (index === selections.length - 1) {
                return row.roleName;
            } else {
                return row.roleName;
            }
        });

        Swal.fire({
            title: '确认删除?',
            html: `<p style="color: red;font-weight: bold">${selectedRoleNames}</p>`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            cancelButtonText: '取消',
            confirmButtonText: '确认',
            showLoaderOnConfirm: true,
            preConfirm: async () => {
                const requestData = {
                    roleIds: selectedRoleIds
                };
                await $.ajax({
                    url: "/role/batchRemoveRole.action",
                    method: "POST",
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(requestData),
                    success: function (response) {
                        if (response.code === "1") {
                            $.toasts({
                                type: 'success',
                                delay: 1000,
                                content: response.msg,
                            });
                            $('#table').bootstrapTable('refresh');
                        }
                    }
                });
            },
            allowOutsideClick: () => !Swal.isLoading()
        }).then((result) => {
            if (result) {
            }
        });
    })


    //处理批量按钮的disabled状态
    function batchBtnStatusHandle() {
        let rowSelected = $("#table").bootstrapTable('getSelections');
        if (rowSelected.length > 0) {
            $('.edit-status-btn').attr('disabled', false);
            $('.batch-btn').attr('disabled', false);
        } else {
            $('.edit-status-btn').attr('disabled', true);
            $('.batch-btn').attr('disabled', true);
        }
    }


    //格式化列数据演示 val:当前数据 rows:当前整行数据
    function formatAction(val, rows) {

        let html = '';

        html += `<button type="button" class="btn btn-light btn-sm edit-btn" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="修改角色"><i class="bi bi-pencil"></i></button>`

        html += `<button type="button" class="btn btn-light btn-sm del-btn mx-1" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="删除"><i class="bi bi-trash3"></i></button>`

        html += `<button type="button" class="btn btn-light btn-sm node-btn" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" data-bs-toggle="tooltip" data-bs-placement="top"
        data-bs-title="分配菜单"><i class="bi bi-diagram-2"></i></button>`

        return html;
    }

    //搜索处理
    $('.bsa-querySearch-btn').on('click', function () {
        $('#table').bootstrapTable('refresh');
        //$('#table').bootstrapTable('selectPage', 1)//跳转到第一页
    })

    //重置处理
    $('.bsa-reset-btn').on('click', function () {
        //把所有的字段都恢复默认值
        $('#roleName').val('');
        //刷新回到第一页
        $('#table').bootstrapTable('refresh');
        //$('#table').bootstrapTable('selectPage', 1)//跳转到第一页
    })

});

$('.edit-status-btn').on('click', function () {
    let selections = $('#table').bootstrapTable('getSelections');
    if (selections.length === 0) {
        Swal.fire({
            icon: 'warning',
            title: '请至少选择一行数据!',
        });
        return;
    }
    let selectedRoleIds = selections.map(function (row) {
        return row.roleId;
    });
    Swal.fire({
        title: '修改状态',
        input: 'select',
        inputOptions: {
            '0': '正常',
            '1': '停用',
        },
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: '取消',
        confirmButtonText: '确认',
        showLoaderOnConfirm: true,
        preConfirm: async (selectedValue) => {
            const requestData = {
                roleIds: selectedRoleIds,
                status: selectedValue === '1' ? '0' : '1'
            };
            await $.ajax({
                url: "/role/modifyRoleStatus.action",
                method: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(requestData),
                success: function (response) {
                    if (response.code === "1") {
                        $.toasts({
                            type: 'success',
                            delay: 1000,
                            content: response.msg,
                        });
                        $('#table').bootstrapTable('refresh');
                    }
                }
            });
        },
        allowOutsideClick: () => !Swal.isLoading()
    }).then((result) => {
        if (result) {
        }
    });

});

function showTooltip(that) {
    $(that).tooltip('show')
}

function hideTooltip(that) {
    $(that).tooltip('hide')
}
