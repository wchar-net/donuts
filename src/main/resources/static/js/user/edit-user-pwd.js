$('#form').formValidation({
    locale: getLang(),
    fields: {
        oldPwd: {
            validators: {
                notEmpty: true,
                stringLength: {
                    min: 6,
                    max: 20
                }
            }
        },
        newPwd: {
            validators: {
                notEmpty: true,
                stringLength: {
                    min: 6,
                    max: 20
                }
            }
        },
        reNewPwd: {
            validators: {
                notEmpty: true,
                stringLength: {
                    min: 6,
                    max: 20
                },
                identical: {
                    field: 'newPwd',
                    message: '密码和确认密码必须一致'
                }
            }
        }
    }
}).on('success.form.fv', function (e) {
    //阻止表单提交
    e.preventDefault();
    //得到表单对象
    let $form = $(e.target);
    let data = $form.serialize();
    $.ajax({
        url: "/user/editUserPwd.action",
        method: 'POST',
        data
    }).then(function (res) {
        if (res.code === "1") {
            Swal.fire({
                title: '修改密码成功!',
                icon: 'warning',
                showCancelButton: false,
                confirmButtonText: '确定',
                allowOutsideClick: false,
                allowEscapeKey: false,
                allowEnterKey: false,
                preConfirm: () => {
                    $.ajax({
                        method: 'post',
                        url: '/logout.action',
                    }).then(response => {
                        eraseCookie($.x_token_var);
                        top.location.replace("/login.action");
                    }).catch(error => {
                        eraseCookie($.x_token_var);
                        top.location.replace("/login.action");
                    });
                }
            });
        } else {
            $('#btn-update-user-pwd').prop('disabled', false);
            $('#btn-update-user-pwd').removeClass('disabled');
        }
    }).catch(error => {
        $('#btn-update-user-pwd').prop('disabled', false);
        $('#btn-update-user-pwd').removeClass('disabled');
    });
});
