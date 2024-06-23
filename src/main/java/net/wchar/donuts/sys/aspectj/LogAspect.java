package net.wchar.donuts.sys.aspectj;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.wchar.donuts.sys.annotation.OpLog;
import net.wchar.donuts.sys.async.AsyncFactory;
import net.wchar.donuts.sys.async.AsyncManager;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.model.bo.LoginBo;
import net.wchar.donuts.model.po.SysOpLogPo;
import net.wchar.donuts.sys.util.RequestUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

/**
 * 日志切面 @OpLog
 * @author Elijah
 */
@Aspect
@Component
public class LogAspect
{
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /** 计算操作消耗时间 */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerOpLog)")
    public void boBefore(JoinPoint joinPoint, OpLog controllerOpLog)
    {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerOpLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OpLog controllerOpLog, Object jsonResult)
    {
        handleLog(joinPoint, controllerOpLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(controllerOpLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OpLog controllerOpLog, Exception e)
    {
        handleLog(joinPoint, controllerOpLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, OpLog controllerOpLog, final Exception e, Object jsonResult)
    {
        try
        {
            // 获取当前的用户
            LoginBo currentUser = UserContextHolder.getUser();

            // *========数据库日志=========*//
            SysOpLogPo opLog = new SysOpLogPo();
            opLog.setStatus(1);
            // 请求的地址
            HttpServletRequest request = getHttpServletRequest();
            String ip = RequestUtils.getClientIp(request);
            opLog.setOpIp(ip);
            opLog.setOpUrl(request.getRequestURI());
            if (currentUser != null)
            {
                opLog.setOpUser(currentUser.getUserName());
                opLog.setOpUserId(currentUser.getUserId());
            }

            if (e != null)
            {
                opLog.setStatus(0);
                opLog.setErrorMsg(e.getMessage());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            opLog.setMethod(className + "->" + methodName + "()");
            // 设置请求方式
            opLog.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerOpLog, opLog, jsonResult);
            // 设置消耗时间
            opLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOp(opLog));
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            logger.error("Log Aspect 异常信息:{}", exp);
        }
        finally
        {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, OpLog log, SysOpLogPo opLog, Object jsonResult) throws Exception
    {
        // 设置action动作
        opLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        opLog.setTitle(log.title());
        // 设置操作人类别
        opLog.setOpType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, opLog);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && null != jsonResult)
        {
            opLog.setJsonResult(objectMapper.writeValueAsString(jsonResult));
        }
    }

    private HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
         return request;
    }
    /**
     * 获取请求的参数，放到log中
     *
     * @param opLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOpLogPo opLog) throws Exception
    {

        Map<String, String[]> map =  getHttpServletRequest().getParameterMap();
        if (!CollectionUtils.isEmpty(map))
        {
            String params = objectMapper.writeValueAsString(map);
            opLog.setOpParam(params);
        }
        else
        {
            Object argsObj = joinPoint.getArgs();
            if (null != argsObj)
            {
                String args = argsObj.toString();
                if(StringUtils.hasText(args)){
                    String params = objectMapper.writeValueAsString(joinPoint.getArgs());
                    opLog.setOpParam(params);
                }
            }
        }
    }
}
