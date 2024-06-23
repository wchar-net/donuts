package net.wchar.donuts.sys.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 白名单
 * @author Elijah
 */
@Component
public class ReferrerInterceptor implements HandlerInterceptor {

    @Value("${referrer.domain}")
    private String referrerDomain;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("referer");
        if (isValidReferrer(referer)) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }

    private boolean isValidReferrer(String referer) {
        if (!StringUtils.hasText(referrerDomain)) {
            //防盗链配置为空直接放行
            return true;
        }
        if (!StringUtils.hasText(referer)) {
            return true;
        }
        String[] arr = StringUtils.delimitedListToStringArray(referrerDomain, ",");
        if (null == arr || arr.length == 0) {
            return true;
        }
        boolean flag;
        for (String item : arr) {
            flag = referer.contains(item);
            if (flag) {
                return true;
            }
        }
        return false;
    }
}