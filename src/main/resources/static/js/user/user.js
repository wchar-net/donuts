$(function () {
    let zTree;
    function onClickNode(event, treeId, treeNode) {
        $("#deptId").val(treeNode.deptId);
        $('#table').bootstrapTable('refresh');
    }
    let setting = {
        data: {
            key: {
                name: "deptName"
            },
            view: {
                showIcon: true,
            },
            simpleData: {
                enable: true,
                idKey: "deptId",
                pIdKey: "deptParentId",
                rootPId: 0
            },
        },
        callback: {
            onClick: onClickNode
        }
    };
    $.ajax({
        type: "GET",
        url: "/dept/getDeptList.action",
        dataType: "json",
        success: function (response) {
            let data = response.data;
            zTree = $.fn.zTree.init($("#ztree-dept"), setting, data);
            zTree.expandAll(true);
        }
    });

    $("#refreshTree").click(function () {
        $.ajax({
            type: "GET",
            url: "/dept/getDeptList.action",
            dataType: "json",
            success: function (response) {
                zTree.destroy();
                let data = response.data;
                zTree = $.fn.zTree.init($("#ztree-dept"), setting, data);
                zTree.expandAll(false);
                zTree.expandAll(true);
            }
        });
    });

    let isExpanded = true;
    $("#toggleExpand").click(function () {
        if (isExpanded) {
            zTree.expandAll(false);
            isExpanded = false;
        } else {
            zTree.expandAll(true);
            isExpanded = true;
        }
    });

    OverlayScrollbarsGlobal.OverlayScrollbars(document.querySelector('#bsa-layout1-scroll-area'), {
        overflowBehavior: {
            x: 'scroll',
            y: 'scroll'
        }
    });
    $('#table').bootstrapTable({
        url: '/user/pageUser.action',
        pagination: true,
        locale: getLang(),
        fitColumns: false,
        sidePagination: 'server',
        undefinedText: '',
        search: false,
        sortable: false,
        fixedColumns: true,
        fixedNumber: 1,
        fixedRightNumber: 1,
        pageList: [5, 10, 25, 50, 100],
        paginationLoop: true,
        paginationPagesBySide: 2,
        uniqueId: 'userId',
        idField: 'userId',
        clickToSelect: true,
        showToggle: true,
        dataType: 'json',
        method: 'get',
        toolbar: '#toolbar',
        showColumns: true,
        showRefresh: true,
        showButtonIcons: true,
        showButtonText: false,
        showFullscreen: false,
        showPaginationSwitch: false,
        totalField: 'total',
        undefinedText: '',
        sortOrder: "asc",
        buttonsClass: 'light',
        buttonsPrefix: 'btn',
        iconsPrefix: 'bi',
        iconSize: undefined,
        icons: {
            fullscreen: 'bi-arrows-fullscreen',
        },
        loadingTemplate: function () {
            return '<div class="spinner-grow" role="status"><span class="visually-hidden">Loading...</span></div>';
        },
        onPostBody: function () {
            $('[data-bs-toggle="tooltip"]').each(function (i, el) {
                new bootstrap.Tooltip(el)
            })
        },
        queryParams: function (params) {
            return {
                pageIndex: Math.ceil(params.offset / params.limit) + 1,
                pageSize: params.limit,
                userName: $("#userName").val(),
                userPhone: $("#userPhone").val(),
                userSex: $("#userSex").val(),
                userStatus: $("#userStatus").val(),
                lastLoginDate: $("#lastLoginDate").val(),
                deptId: $("#deptId").val()
            };
        },
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
        columns: [
            {
                checkbox: true,
                visible: true,
            },
            {
                field: 'userId',
                title: i18n_userId,
                width: '100px',
                align: "center"
            },
            {
                field: 'userName',
                title: i18n_userName,
                width: '100px',
                align: "center"
            },
            {
                field: 'userNick',
                title: i18n_userNick,
                width: '100px',
                align: "center"
            },
            {
                field: 'deptName',
                title: i18n_deptName,
                width: '100px',
                align: "center"
            },
            {
                field: 'userType',
                title: i18n_userOrigin,
                width: '40px',
                align: "center",
                formatter: function (value, row, index) {
                    return value === 1 ? '<span class="badge text-bg-success">' + i18n_external + '</span>' : '<span class="badge text-bg-secondary">' + i18n_inside + '</span>';
                }
            },
            {
                field: 'userStatus',
                title: i18n_userStatus,
                width: '50px',
                align: "center",
                formatter: function (value, row, index) {
                    return value === 1 ? '<span class="badge text-bg-success">' + i18n_normal + '</span>' : '<span class="badge text-bg-secondary">' + i18n_deactivate + '</span>';
                }
            },
            {
                field: 'userEmail',
                title: i18n_userMail,
                width: '180px',
                align: "center",
                cellStyle: {css: {"max-width": "180px"}}
            },
            {
                field: 'userPhone',
                title: i18n_userPhone,
                width: '130px',
                align: "center",
                cellStyle: {css: {"min-width": "130px"}}
            },

            {
                field: 'userSex',
                title: i18n_userSex,
                width: '50px',
                align: "center",
                cellStyle: {css: {"max-width": "30px"}},
                formatter: function (value, row, index) {
                    const genderMap = {
                        0: {text: i18n_male, class: 'text-bg-primary'},
                        1: {text: i18n_female, class: 'text-bg-success'},
                        2: {text: '?', class: 'text-bg-info'}
                    };
                    const genderInfo = genderMap[value];
                    return `<span class="badge ${genderInfo.class} p-1 m-0">${genderInfo.text}</span>`;
                }
            },

            {
                field: 'lastLoginIp',
                title: i18n_lastLoginIp,
                width: '100px',
                align: "center"
            },
            {
                field: 'lastLoginDate',
                title: i18n_lastLoginDate,
                width: '100px',
                align: "center",
                formatter: function (value, row, index) {
                    return formatLocalDateTime(value);
                }
            },
            {
                field: 'lastPwdUpdateDate',
                title: i18n_lastPwdDate,
                width: '100px',
                align: "center",
                formatter: function (value, row, index) {
                    return formatLocalDateTime(value);
                }
            },
            generateActionsColumn()
        ].filter(Boolean),
    });
})

//操作列
function generateActionsColumn() {
    if (current_user_perms.includes('sys:user:edit') ||
        current_user_perms.includes('sys:user:del') ||
        current_user_perms.includes('sys:user:assign')) {
        return {
            field: 'actions',
            title: i18n_op,
            align: 'center',
            width: '150px',
            formatter: function (value, row, index) {
                let html = '';
                if (current_user_perms.includes('sys:user:edit')) {
                    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light btn-sm mx-1 edit-btn" data-toggle="tooltip" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="${i18n_updateUser}">
                                <i class="bi bi-pencil"></i>
                            </button>`;
                }
                if (current_user_perms.includes('sys:user:del')) {
                    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light mx-1 btn-sm del-btn" data-toggle="tooltip" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="${i18n_updateDel}">
                                <i class="bi bi-trash3"></i>
                            </button>`;
                }
                if (current_user_perms.includes('sys:user:assign')) {
                    html += `<button type="button" onmouseleave="hideTooltip(this)" onmouseenter="showTooltip(this)" class="btn btn-light btn-sm mx-1 role-btn" data-toggle="tooltip" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-title="${i18n_assignRole}">
                                <i class="bi bi-person-square"></i>
                            </button>`;
                }
                return html;
            },events: {
                'click .del-btn': function (event, value, row, index) {
                    event.stopPropagation();
                    let tips = i18n_delConfirm + "  " + row.userName + "  " + "?";
                    $.modal({
                        body: tips,
                        ok: function () {
                            $.ajax({
                                url: '/user/removeUser.action',
                                method: 'post',
                                data: {
                                    userId: row.userId
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

                }, 'click .role-btn': function (event, value, row, index) {
                    event.stopPropagation();
                    $("#assignUserId").val(row.userId);
                    $("#assignUserName").val(row.userName);
                    window.rolemodal = $.modal({
                        url: '/user/assignRole.action',
                        title: i18n_assignRole,
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
                'click .edit-btn': function (event, value, row, index) {
                    event.stopPropagation();
                    $("#assignUserId").val(row.userId);
                    window.modalInstance = $.modal({
                        url: '/user/editUser.action',
                        title:  i18n_userEdit,
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
        };
    } else {
        return null;
    }
}

//添加用户
$('.add-btn').on('click', function () {
    window.modalInstance = $.modal({
        url: '/user/addUser.action',
        title: i18n_userAdd,
        buttons: [],
        modalDialogClass: 'modal-dialog-centered modal-xl',
        onHidden: function (obj, data) {
            if (data === true) {
                $('#table').bootstrapTable('refresh');
            }
        }
    })
})

//搜索处理
$('.bsa-querySearch-btn').on('click', function () {
    $('#table').bootstrapTable('refresh');
})

//重置密码
$('.reset-password').on('click', function () {
    let selections = $('#table').bootstrapTable('getSelections');
    if (selections.length === 0) {
        Swal.fire({
            icon: 'warning',
            title: i18n_pleaseSelectAtLeastOneRowOfData,
        });
        return;
    }
    let selectedUserIds = selections.map(function (row) {
        return row.userId;
    });
    let selectedUserNames = selections.map(function (row, index) {
        if (index === selections.length - 1) {
            return row.userName;
        } else {
            return row.userName;
        }
    });

    Swal.fire({
        title: i18n_confirmResetPassword,
        html: `<p style="color: red;font-weight: bold">${selectedUserNames}</p>`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: i18n_cancel,
        confirmButtonText: i18n_ok,
        showLoaderOnConfirm: true,
        preConfirm: async () => {
            const requestData = {
                userIds: selectedUserIds
            };
            await $.ajax({
                url: "/user/resetUserPwd.action",
                method: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(requestData),
                success: function (response) {
                    if (response.code === "1") {
                        Swal.fire({
                            title: i18n_resetPasswordSuccess,
                            html: `<p style="color: red;font-weight: bold">${response.data}</p>`,
                            icon: "success"
                        });
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
//修改状态
$('.edit-status-btn').on('click', function () {
    let selections = $('#table').bootstrapTable('getSelections');
    if (selections.length === 0) {
        Swal.fire({
            icon: 'warning',
            title: i18n_pleaseSelectAtLeastOneRowOfData,
        });
        return;
    }
    let selectedUserIds = selections.map(function (row) {
        return row.userId;
    });
    Swal.fire({
        title: i18n_editStatus,
        input: 'select',
        inputOptions: {
            '0': i18n_normal,
            '1': i18n_deactivate,
        },
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: i18n_cancel,
        confirmButtonText: i18n_ok,
        showLoaderOnConfirm: true,
        preConfirm: async (selectedValue) => {
            const requestData = {
                userIds: selectedUserIds,
                status: selectedValue === '1' ? '0' : '1'
            };
            await $.ajax({
                url: "/user/modifyUserStatus.action",
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

//批量删除
$('.batch-btn').on('click', function () {
    let selections = $('#table').bootstrapTable('getSelections');
    if (selections.length === 0) {
        Swal.fire({
            icon: 'warning',
            title: i18n_pleaseSelectAtLeastOneRowOfData,
        });
        return;
    }
    let selectedUserIds = selections.map(function (row) {
        return row.userId;
    });
    let selectedUserNames = selections.map(function (row, index) {
        if (index === selections.length - 1) {
            return row.userName;
        } else {
            return row.userName;
        }
    });

    Swal.fire({
        title: i18n_confirmDel,
        html: `<p style="color: red;font-weight: bold">${selectedUserNames}</p>`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: i18n_cancel,
        confirmButtonText: i18n_ok,
        showLoaderOnConfirm: true,
        preConfirm: async () => {
            const requestData = {
                userIds: selectedUserIds
            };
            await $.ajax({
                url: "/user/batchRemoveUser.action",
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


function showTooltip(that) {
    $(that).tooltip('show')
}

function hideTooltip(that) {
    $(that).tooltip('hide')
}

//重置处理
$('.bsa-reset-btn').on('click', function () {
    $('#userName').val('');
    $('#userPhone').val('');
    $('#lastLoginDate').val(null);
    $('#userStatus').selectpicker('val', $('#userStatus option:first').val());
    $('#table').bootstrapTable('refresh');
})

new tempusDominus.TempusDominus(document.getElementById('lastLoginDate'), {
    //本地化控制
    localization: {
        ...td_zh,
        format: 'yyyy-MM-dd',
    },
    //布局控制
    display: {
        //视图模式(选择年份视图最先开始)
        viewMode: 'calendar',
        components: {
            //是否开启日历，false:则是时刻模式
            calendar: true,
            //支持年份选择
            year: true,
            //是否开启月份选择
            month: true,
            //支持日期选择
            date: true,
            //底部按钮中那个时刻选择是否启用，false则表示隐藏，下面三个需要该选项为true时有效
            clock: false,
            //时
            hours: false,
            //分
            minutes: false,
            //秒
            seconds: false
        },
        //图标
        icons: {
            time: 'bi bi-clock',
            date: 'bi bi-calendar',
            up: 'bi bi-arrow-up',
            down: 'bi bi-arrow-down',
            previous: 'bi bi-chevron-left',
            next: 'bi bi-chevron-right',
            today: 'bi bi-calendar-check',
            clear: 'bi bi-trash',
            close: 'bi bi-x',
        },
        //视图底部按钮
        buttons: {
            today: true,
            clear: true,
            close: true,
        },
    }
});
//下拉框美化插件，原生的bootstrap它会调用系统的那个下拉菜单
$('select').selectpicker();