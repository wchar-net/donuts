package net.wchar.donuts.sys.holder;

import net.wchar.donuts.model.bo.LoginBo;

/**
 * 用户 holder
 * @author Elijah
 */
public class UserContextHolder {
    private static final ThreadLocal<LoginBo> userThreadLocal = new ThreadLocal<>();

    public static void setUser(LoginBo user) {
        userThreadLocal.set(user);
    }

    public static LoginBo getUser() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}
