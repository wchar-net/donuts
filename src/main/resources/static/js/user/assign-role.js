$(function () {
    $("#assignUserName").text("你正在为 " + $(parent.document).find("#assignUserName").val() + " 分配角色");
    //记录选中的id
    let selections = new Set();

    //用户已经选中的id
    let current_user_roles = new Set();

    //当前页面已经删除掉的id
    let already_user_roles = new Set();

    $.ajax({
        url: `/user/getUserRole.action`,
        method: 'get',
        data: {
            userId : $(parent.document).find("#assignUserId").val()
        }
    }).then(response => {
        if (response.code === "1") {
            current_user_roles = response.data.map(function(item) {
                return item.roleId;
            });

            let lang = getCookie("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE");
            if (typeof lang === "undefined" || null === lang || "null" === lang) {
                lang = "zh-CN";
            }
            $('#table').bootstrapTable({
                //配置语言
                locale: lang,
                //设置高度，就可以固定表头
                // height: 500,
                //请求地址
                url: '/user/pageUserRole.action',
                responseHandler: function (res) {
                    return {
                        total: res.total,
                        rows: res.data
                    };
                },
                //加载模板,不改的话，默认的会超出不好看
                loadingTemplate: function () {
                    return '<div class="spinner-grow" role="status"><span class="visually-hidden">Loading...</span></div>';
                },
                //头部的复选框是否显示
                checkboxHeader: false,
                //是否单选
                singleSelect: false,
                //是否开启分页
                pagination: true,
                //是客户端分页还是服务端分页  'client','server',由于演示没有后端提供服务，所以采用前端分页演示
                sidePagination: 'server',
                // 初始化加载第一页，默认第一页
                pageNumber: 1,
                //默认显示几条
                pageSize: 5,
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
                showToggle: false,
                // 请求得到的数据类型
                dataType: 'json',
                // 请求方法
                method: 'get',
                // 是否显示所有的列
                showColumns: false,
                // 是否显示刷新按钮
                showRefresh: false,
                // 显示图标
                showButtonIcons: false,
                // 显示文本
                showButtonText: false,
                // 显示全屏
                showFullscreen: false,
                // 开关控制分页
                showPaginationSwitch: false,
                // 总数字段
                totalField: 'total',
                // 当字段为 undefined 显示
                undefinedText: '-',
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
                //头部一键全选
                checkboxHeader: true,
                onPostBody: function () {
                    //重新启用弹出层,否则formatter格式化函数返回的html字符串上的tooltip提示不生效
                    $('[data-bs-toggle="tooltip"]').each(function (i, el) {
                        new bootstrap.Tooltip(el)
                    })
                },
                //params是一个对象
                queryParams: function (params) {
                    return {
                        // 每页数据量
                        pageIndex: Math.ceil(params.offset / params.limit) + 1,
                        pageSize: params.limit,
                        roleName: $("#roleName").val(),
                    }
                },
                onCheck: function (row) {
                    selections.add(row.roleId);
                },
                onUncheck: function (row) {
                    already_user_roles.add(row.roleId)
                },
                onCheckAll: function (rows) {
                    rows.forEach(row => selections.add(row.roleId));
                },
                onUncheckAll: function (rows) {
                    let allRowsData = $('#table').bootstrapTable('getData');
                    // 提取所有行的 roleId
                    let roleIdList = allRowsData.map(function(row) {
                        return row.roleId;
                    });
                    roleIdList.forEach(function(item) {
                        already_user_roles.add(item);
                    });
                },
                onPageChange: function (number, size) {

                },
                onLoadSuccess: function () {
                    current_user_roles.forEach(function(item) {
                        selections.add(item);
                    });
                    already_user_roles.forEach(function(item) {
                        selections.delete(item);
                    });
                    $('#table').bootstrapTable('checkBy', {field: 'roleId', values: Array.from(selections)});
                },
                //列
                columns: [
                    {
                        field: 'state',
                        checkbox: true
                    },
                    {
                        title: '角色id',
                        field: 'roleId',
                        // 使用[align]，[halign]和[valign]选项来设置列和它们的标题的对齐方式。h表示横向，v标识垂直
                        align: 'center',
                        // 是否作为排序列
                        sortable: false,
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
                    }
                ]
            });
        }else {
            $('#assign-btn').remove();
        }
    });

    //授权按钮
    $('.btn-submit').on('click', function () {
        //获取选中的行
        let userId = $(parent.document).find("#assignUserId").val();
        already_user_roles.forEach(function(item) {
            selections.delete(item);
        });
        let data = {
            roleIds: Array.from(selections),
            userId: userId
        }
        $.ajax({
            url: `/user/assignUserRole.action`,
            method: 'post',
            contentType: 'application/json',
            data: JSON.stringify(data)
        }).then(response => {
            if (response.code === "1") {
                $.toasts({
                    type: 'success',
                    content: '分配成功',
                    delay: 500,
                    onHidden: function () {
                        parent.rolemodal.setData(true);
                        parent.rolemodal.close();
                    }
                })
            }
        });
    })
    //搜索处理
    $('.bsa-querySearch-btn').on('click', function () {
        $('#table').bootstrapTable('refresh');
    })

    //重置处理
    $('.bsa-reset-btn').on('click', function () {
        $('#roleName').val('');
        $('#table').bootstrapTable('refresh');
    })

});