//================封装的公共方法===============================

// 这里可以存你自己封装的一些公共函数

//=================================================

//一些全局插件的统一配置
$.loading.default.type = 'border';
$.loading.default.color = 'success';
$.x_token_var = 'x_token';
$.index_action_var = '/index.action';
/**
 * ajax的一些统一处理
 * Tips:建议后端对于ajax请求返回统一json格式,方便我们处理业务逻辑
 * 这里的状态码是我们自己的业务上的code,不是http请求的status那个code
 * 比如:
 * {code:403 msg:'登录过期,重新登录' data:[]}
 * {code:200 msg:'' data:[{...},{...}]}
 * {code:10001 msg:'文章缩略图上传错误' data:[]}
 * 这样我们只要检测到403我们就直接返回登录页
 */

//发送ajax前的统一设置
$.ajaxSetup({
    //超时时间:5秒
    timeout: 5000,
    //请求头添加参数
    headers: {
        //请求头防止csrf攻击(参考php框架laravel)
        //'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
    },
    //统一返回类型
    dataType: 'json'
});


//发送ajax前回调
$(document).ajaxSend(function (event, xhr, options) {
    //可以加你自己的处理逻辑
});


//ajax请求成功时回调
$(document).ajaxSuccess(function (event, xhr, options) {
    let responseData = JSON.parse(xhr.responseText);
    let code = responseData.code;
    let msg = responseData.msg;

    if (code !== "1") {
        //登录页面跳转
        if (code === "L500") {
            eraseCookie($.x_token_var);
            $.toasts({
                type: 'danger',
                delay: 100,
                content: i18n_loginExpire,
                onHidden: function () {
                    top.location.replace('/login.action');
                }
            })
        } else {
            $.toasts({
                type: 'danger',
                delay: 3000,
                content: msg,
            })
        }

    }

});


//ajax请求失败时回调
$(document).ajaxError(function (event, xhr, options, thrownError) {
    $.toasts({
        type: 'danger',
        delay: 3000,
        content:  "ajax => " + xhr.statusText,
    })
});


// ajax请求结束,成功失败都会执行
$(document).ajaxComplete(function (event, xhr, options) {
    //可以加你自己的处理逻辑

});


function setCookie(name, value, hours) {
    let expires = "";
    if (hours) {
        let date = new Date();
        date.setTime(date.getTime() + (hours * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + encodeURIComponent(value) + expires + "; path=/";
}


function eraseCookie(name) {
    document.cookie = name+'=; Max-Age=-99999999;';
}

function getCookie(name) {
    let cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.indexOf(name + '=') === 0) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}


function formatLocalDateTime(value) {
    if (value) {
        let date = new Date(value);
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let hours = date.getHours();
        let minutes = date.getMinutes();
        let seconds = date.getSeconds();
        if (month < 10) {
            month = '0' + month;
        }
        if (day < 10) {
            day = '0' + day;
        }
        if (hours < 10) {
            hours = '0' + hours;
        }
        if (minutes < 10) {
            minutes = '0' + minutes;
        }
        if (seconds < 10) {
            seconds = '0' + seconds;
        }
        return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
    } else {
        return '';
    }
}
//日期时间的翻译，由于该插件没有内置中文翻译，需要手动通过选项进行翻译
let td_zh = {
    today: '回到今天',
    clear: '清除选择',
    close: '关闭选取器',
    selectMonth: '选择月份',
    previousMonth: '上个月',
    nextMonth: '下个月',
    selectYear: '选择年份',
    previousYear: '上一年',
    nextYear: '下一年',
    selectDecade: '选择十年',
    previousDecade: '上一个十年',
    nextDecade: '下一个十年',
    previousCentury: '上一个世纪',
    nextCentury: '下一个世纪',
    pickHour: '选取时间',
    incrementHour: '增量小时',
    decrementHour: '递减小时',
    pickMinute: '选取分钟',
    incrementMinute: '增量分钟',
    decrementMinute: '递减分钟',
    pickSecond: '选取秒',
    incrementSecond: '增量秒',
    decrementSecond: '递减秒',
    toggleMeridiem: '切换上下午',
    selectTime: '选择时间',
    selectDate: '选择日期',
}

function getLang(){
    let lang = getCookie("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE");
    if (typeof lang === "undefined" || null === lang || "null" === lang) {
        lang = "zh-CN";
    }else if(lang === "en-US"){
        lang = "en_US";
    }
    return lang;
}
