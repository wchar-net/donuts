package net.wchar.donuts.sys.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * 安全工具类
 *
 * @author Elijah
 */
@Component
public class SecurityUtils {
    public static final String ATTR_PERMS = "current_user_perms";
    public static final String ATTR_ROLE = "current_user_roles";

    public static boolean hasPermission(String perms) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        List<String> userAllPerms = (List<String>) request.getAttribute(ATTR_PERMS);
        for (String item :userAllPerms) {
            if (perms.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }
}
