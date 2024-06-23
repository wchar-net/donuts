function generateMenu(menuData, parentElement) {
    menuData.forEach(function (item) {
        let li = document.createElement('li');
        let a = document.createElement('a');
        a.setAttribute('href', item.url);
        a.innerHTML = '<i class="' + item.icon + '"></i>' + item.menuName;
        a.setAttribute("cus-target", item.target);
        a.setAttribute("cus-icon", item.icon);
        li.appendChild(a);
        //目录a
        if (item.children && item.children.length > 0) {
            a.classList.add('has-children');
            a.addEventListener('click', function (event) {
                event.preventDefault();
                if (item.url) {
                    a.href = item.url;
                    if (item.target === 2) {
                        a.target = '_blank';
                        window.open(item.url, '_blank');
                    } else {
                        // //' + inputValue + '</span>
                        Quicktab.get('.qtab').addTab({
                            title: `<i class="${item.icon}"></i><span class="ms-2">${item.menuName}</span>`,
                            url: item.url,
                            close: true,
                        })
                    }
                    li.classList.toggle('active');
                }
            });
            let ul = document.createElement('ul');
            generateMenu(item.children, ul);
            li.appendChild(ul);
        }

        parentElement.appendChild(li);
    });
}

function loadMenu() {
    $.ajax({
        method: 'get',
        url: '/menu.action?lang=' + getCookie("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE")
    }).then(response => {
        let menuData = response.data;
        if (null == menuData || menuData.length === 0) {
            Swal.fire({
                icon: "error",
                title: "无权限",
                allowOutsideClick: false,
                allowEscapeKey: false,
                text: "对不起,你没有任何菜单展示!",
                footer: '<a href="javascript:void(0)">如有疑问建议联系管理员咨询!</a>'
            });
            return;
        }
        let menuElement = document.getElementById('menu');
        generateMenu(menuData, menuElement);
    });
}

$(function () {
    loadMenu();

    //头部搜索框处理(不需要可以删除,不明白的可以看bootstrap-admin官方文档)
    $(document).on('search.bsa.navbar-search', function (e, inputValue, data) {
        //先得到请求地址,组合后大概是这样pages/search.html?keyword=dsadsa&type=article&user=admin2
        let url = data.action + '?keyword=' + inputValue + '&' + $.param(data.params);
        //然后通过tab打开一个搜索结果的窗口
        Quicktab.get('.qtab').addTab({
            title: '<i class="bi bi-search"></i><span class="text-danger ms-2">' + inputValue + '</span>',
            url: url,
            close: true,
        })
    })

    //退出登录
    $(document).on('click', '.bsa-logout', function (e) {
        e.preventDefault();
        $.modal({
            body: '确定要退出吗？',
            cancelBtn: true,
            ok: function () {
                //请求退出路由
                $.ajax({
                    method: 'post',
                    url: '/logout.action',
                }).then(response => {
                    eraseCookie($.x_token_var);
                    $.toasts({
                        type: 'success',
                        content: '退出成功',
                        delay: 10,
                        onHidden: function () {
                            top.location.replace("/login.action");
                        }
                    })
                });
            }
        })
    });


    let togglerSidebarFlag = true;

    let w = $(window).width();
    if (w < 992) {
        $("#btnTogglerSidebar").hide();
    } else {
        $("#btnTogglerSidebar").show();
    }
    $(window).resize(function () {
        let w = $(window).width();
        if (w < 992) {
            $("#btnTogglerSidebar").hide();
            $(document.documentElement).css('--bsa-sidebar-width', '240px');
            $(".bsa-sidebar").show();
            togglerSidebarFlag = false;
        } else {
            togglerSidebarFlag = true;
            $("#btnTogglerSidebar").show();
        }
    });


    $("#btnTogglerSidebar").click(function () {
        if (togglerSidebarFlag) {
            //收缩
            //--bsa-sidebar-width
            $(document.documentElement).css('--bsa-sidebar-width', '0px');
            $(".bsa-sidebar").hide(500);
            togglerSidebarFlag = false;
            $('.bsa-sidebar').removeClass("open");
            $('.bsa-sidebar').data('isOpen', true);
            $('.bsa-mask').remove();
        } else {
            //展开
            $(document.documentElement).css('--bsa-sidebar-width', '240px');
            $(".bsa-sidebar").show(500);
            togglerSidebarFlag = true;
            $('.bsa-sidebar').addClass("open");
            $('.bsa-sidebar').data('isOpen', true);
            if ($('.bsa-mask').length === 0) {
                $('<div class="bsa-mask"></div>').prependTo('body');
            }
        }
    });
});


function btnEditPwd() {
    Quicktab.get('.qtab').addTab({
        title: '<i class="bi bi-key"></i><span class="ms-2">修改密码</span>',
        url: '/user/editUserPwd.action',
        close: true,
    })
}