package net.wchar.donuts.sys.interceptor;

import net.wchar.donuts.sys.annotation.RequiresPermissions;
import net.wchar.donuts.sys.exception.PermissionException;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.model.bo.LoginBo;
import net.wchar.donuts.sys.util.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 权限拦截器
 * @author Elijah
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {


    @Resource
    private MessageSource messageSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginBo currentUser = UserContextHolder.getUser();
        if (null != currentUser) {
            request.setAttribute(SecurityUtils.ATTR_ROLE, currentUser.getRoles());
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RequiresPermissions requiresPermissions = AnnotationUtils.findAnnotation(method, RequiresPermissions.class);
            if (requiresPermissions != null) {
                String permission = requiresPermissions.value();
                if (StringUtils.hasText(permission)) {
                    LoginBo user = UserContextHolder.getUser();
                    List<String> currentUserPerms = user.getPerms();
                    request.setAttribute(SecurityUtils.ATTR_PERMS, currentUserPerms);
                    if (!checkUserMenuPerms(permission, currentUserPerms)) {
                        throw new PermissionException(messageSource.getMessage("your.not.permission", null, LocaleContextHolder.getLocale()));
                    }
                }

            }
        }
        return true;
    }

    private boolean checkUserMenuPerms(String perms, List<String> currentUserPerms) {
        for (String cachePerm : currentUserPerms) {
            if (perms.equalsIgnoreCase(cachePerm)) {
                return true;
            }
        }
        return false;
    }
}
