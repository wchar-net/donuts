package net.wchar.donuts.sys.util;

import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.model.domain.ResultPageDomain;

import java.util.List;

/**
 * 统一返回值工具类
 *
 * @author Elijah
 */
public class ResultUtils {

    //登录相关验证错误全部返回 L500
    public static final String CODE_LOGIN_FAIL = "L500";

    //成功
    public static final String CODE_SUCCESS = "1";

    //成功 msg
    public static final String CODE_SUCCESS_MSG = "操作成功!";

    //失败
    public static final String CODE_FAIL = "0";


    //系统错误
    public static final String CODE_SYS_FAIL = "S500";

    //业务错误
    public static final String CODE_BUS_FAIL = "B500";

    public static ResultDomain fail(String code, String msg) {
        return ResultDomain.builder().code(code).msg(msg).build();
    }

    public static <T> ResultDomain<T> success(T t) {
        return ResultDomain.<T>builder().code(CODE_SUCCESS).msg(CODE_SUCCESS_MSG).data(t).build();
    }
    public static <T> ResultDomain<T> success(String msg,T t) {
        return ResultDomain.<T>builder().code(CODE_SUCCESS).msg(msg).data(t).build();
    }

    public static <T> ResultPageDomain<T> pageSuccess(Long total, List<T> records) {
        return ResultPageDomain.<T>builder().total(total).code(CODE_SUCCESS).msg(CODE_SUCCESS_MSG).data(records).build();
    }
}
