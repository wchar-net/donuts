package net.wchar.donuts.sys.interceptor;

import net.wchar.donuts.model.po.AccessStatisticsPo;
import net.wchar.donuts.service.AccessStatisticsService;
import net.wchar.donuts.sys.util.RequestUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求记录
 * @author Elijah
 */
@Component
public class AssessStatisticsInterceptor implements HandlerInterceptor {
    @Resource
    private AccessStatisticsService accessStatisticsService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(null == ex){
            insertStatistics(request);
        }
    }


    @Async
    @Transactional(readOnly = true)
    public void insertStatistics(HttpServletRequest request) {
        AccessStatisticsPo accessStatisticsPo = AccessStatisticsPo.builder()
                .ip(RequestUtils.getClientIp(request))
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .userAgent(request.getHeader("User-Agent"))
                .build();
        accessStatisticsService.save(accessStatisticsPo);
    }
}
