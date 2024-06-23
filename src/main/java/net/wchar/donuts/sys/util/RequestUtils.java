package net.wchar.donuts.sys.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 请求工具类
 *
 * @author Elijah
 */
public class RequestUtils {
    public static String getClientIp(HttpServletRequest request) {
        String xFor = request.getHeader("X-Forwarded-For");
        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xFor)) {
            return xFor;
        } else if (StringUtils.hasText(xRealIp)) {
            return xRealIp;
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    public static String getCookie(HttpServletRequest request, String cookieName) {
        // 获取请求中的所有 Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        String acceptHeader = request.getHeader("Accept");
        return "XMLHttpRequest".equals(requestedWithHeader) || (acceptHeader != null && acceptHeader.contains("application/json"));
    }

    public static <T> void setJsonResponse(HttpServletResponse response, T t, ObjectMapper objectMapper) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = objectMapper.writeValueAsString(t);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
