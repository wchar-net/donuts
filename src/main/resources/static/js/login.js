function getCaptcha(){
    $.ajax({
        method: 'get',
        url: '/captcha.action'
    }).then(response => {
        $('#captchaImg').attr('src', response.data.imageBase64);
        $('#captchaKey').val(response.data.captchaKey);
    });
}
function  checkToken(){
    if(getCookie($.x_token_var) == null){
        return;
    }
    $.ajax({
        method: 'get',
        url: '/checkToken.action'
    }).then(response => {
        let userName =  response.data.userName;
        Swal.fire({
            title: i18n_loggedInTitle,
            html: `${i18n_loggedInTitle} <b style="color: red">${userName}</b> ${i18n_loggedInHtml}`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: i18n_gotoHomePage,
            cancelButtonText: i18n_cancel
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.replace($.index_action_var);
            }
        });
    });
}

$(function () {
    checkToken();
    getCaptcha();
    //前端表单验证
    $('#loginForm').formValidation({
        fields: {
            userName: {
                validators: {
                    notEmpty: true,
                }
            },
            userPwd: {
                validators: {
                    notEmpty: true,
                }
            },
            captchaCode: {
                validators: {
                    notEmpty: true,
                }
            }
        }
    }).on('success.form.fv', function (e) {
        e.preventDefault();
        //得到表单对象
        let $form = $(e.target);
        //获取数据
        let data = $form.serialize();
        //发起ajax请求
        $.ajax({
            method: 'post',
            url: '/login.action',
            //表单数据
            data: data,
        }).then(response => {
            if (response.code === "1") {
                setCookie($.x_token_var,response.data,2)
                $.toasts({
                    type: 'success',
                    delay: 100,
                    content: i18n_loginSuccess,
                    onHidden: function () {
                       top.location.replace($.index_action_var);
                    }
                })
            }else {
                getCaptcha();
                $('#loginSubmit').removeAttr('disabled');
                $('#loginSubmit').removeClass('disabled');
            }
        });
    });
})