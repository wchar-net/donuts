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
    $.ajax({
        url: `/role/getRoleDetail.action`,
        method: 'get',
        data: {
            roleId: $(parent.document).find("#roleId").val()
        }
    }).then(response => {
        let selMenuIds = response.data.menuIds;
        if (response.code === "1") {
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
                fixedRightNumber: 0,
                undefinedText: '',
                clickToSelect: true,
                loadingTemplate: function () {
                    return '<div class="spinner-grow" role="status"><span class="visually-hidden">Loading...</span></div>';
                },
                ignoreClickToSelectOn: ignoreClickToSelectOn,
                onCheck: function (row) {
                    //获取所有的行
                    let datas = $('#table').bootstrapTable('getData');
                    //勾选子节点
                    selectChilds(datas, row, "menuId", "parentId", true);
                    //勾选父节点
                    selectParentChecked(datas, row, "menuId", "parentId")
                    // 刷新数据
                    $('#table').bootstrapTable('load', datas);
                },
                //取消勾选事件
                onUncheck: function (row) {
                    let datas = $('#table').bootstrapTable('getData');
                    //把所有的子节点都取消掉
                    selectChilds(datas, row, "menuId", "parentId", false);
                    //取消选中最后一个子元素时查找对应父元素取消
                    selectParentUnchecked(datas, row, "menuId", "parentId")
                    $('#table').bootstrapTable('load', datas);
                },
                onLoadSuccess: function () {
                    $('#table').bootstrapTable('checkBy', {field: 'menuId', values: selMenuIds});
                    let tableData = $('#table').bootstrapTable('getData');
                    let idsToUncheck = tableData
                        .filter(function (row) {
                            return !selMenuIds.includes(row.menuId);
                        })
                        .map(function (row) {
                            return row.menuId;
                        });

                    // 取消所有需要取消选中的菜单项的选中状态
                    $('#table').bootstrapTable('uncheckBy', {field: 'menuId', values: idsToUncheck});
                },
                queryParams: function (params) {
                    return {
                        menuName: $("#menuName").val()
                    };
                },
                columns: [
                    {
                        checkbox: true,
                        //是否显示该列
                        visible: true,
                    },
                    {
                        field: 'menuId',
                        title: '菜单id',
                        align: 'center',
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
                        title: '创建时间',
                        field: 'createTime',
                        align: 'center',
                        width: '100px',
                    },
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
                    if (columns && columns[0][2].visible) {
                        $('#table').treegrid({
                            treeColumn: 2,
                            onChange: function () {
                                $('#table').bootstrapTable('resetView')
                            }
                        })
                    }

                }
            })
        } else {
            $('#submit-btn').remove();
        }
    });

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

$('.submit-btn').on('click', function () {
    //获取选中的行的id
    let menuIds = [];
    let rowSelected = $("#table").bootstrapTable('getSelections');
    $.each(rowSelected, function (index, row) {
        menuIds.push(row.menuId);
    })

    //判断是否有勾选
    if (menuIds.length === 0) {
        $.toasts({
            type: 'danger',
            content: '请选择需要分配的菜单',
        })
        return false;
    }

    let reqData = {
        roleId: $(parent.document).find("#roleId").val(),
        menuIds: menuIds
    };
    $.ajax({
        url: `/role/assignRoleMenu.action`,
        method: 'post',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(reqData),
    }).then(response => {
        if (response.code === "1") {
            $.toasts({
                type: 'success',
                content: '分配成功',
                delay: 1000,
                onHidden: function () {
                    parent.rolemodal.setData(true);
                    parent.rolemodal.close();
                }
            })
        }
    });
})

function ignoreClickToSelectOn(e) {
    return $(e).hasClass('treegrid-expander');
}

/**
 * 选中父项时，同时选中子项
 * @param datas 所有的数据
 * @param row 当前数据
 * @param id id 字段名
 * @param pid 父id字段名
 * @param checked 是否选中
 */
function selectChilds(datas, row, id, pid, checked) {
    for (let i in datas) {
        if (datas[i][pid] === row[id]) {
            datas[i][0] = checked;
            selectChilds(datas, datas[i], id, pid, checked);
        }
    }
}

function selectParentUnchecked(datas, row, id, pid) {
    let key = 0;
    for (let i in datas) {
        if (datas[i].parentId === row.parentId && datas[i][0] === true) {
            key++
        }
    }
    if (key === 0) {
        for (let i in datas) {
            if (datas[i][id] === row[pid]) {
                datas[i][0] = false;
                selectParentUnchecked(datas, datas[i], id, pid);
            }
        }
    }
}

function selectParentChecked(datas, row, id, pid) {
    for (let i in datas) {
        if (datas[i][id] === row[pid]) {
            datas[i][0] = true;
            selectParentChecked(datas, datas[i], id, pid);
        }
    }
}