package net.wchar.donuts.sys.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.sys.exception.PermissionException;
import net.wchar.donuts.sys.util.RequestUtils;
import net.wchar.donuts.sys.util.ResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常配置
 * @author Elijah
 */
@ControllerAdvice
public class GlobalExceptionConfig {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    @Resource
    private ObjectMapper objectMapper;

    //无权限
    @ExceptionHandler(PermissionException.class)
    public String handlePermissionException(PermissionException ex, HttpServletRequest request, HttpServletResponse response ,Model model) throws IOException {
        logger.error("{}", ex);
        if (ex.getInternalServerErrorFlag()) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            response.setStatus(HttpStatus.OK.value());
        }
        if (RequestUtils.isAjaxRequest(request)) {
            ResultDomain domain = ResultUtils.fail(ResultUtils.CODE_SYS_FAIL, ex.getMessage());
            RequestUtils.setJsonResponse(response, domain, objectMapper);
            return null;
        } else {
            model.addAttribute("msg", ex.getMessage());
            model.addAttribute("requestUri", request.getRequestURI());
            return "error";
        }
    }

    // ajax异常
    @ExceptionHandler(BusException.class)
    @ResponseBody
    public ResultDomain<String> handleBusException(BusException ex, HttpServletRequest request, HttpServletResponse response) {
        logger.error("{}", ex);
        if (ex.getInternalServerErrorFlag()) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            response.setStatus(HttpStatus.OK.value());
        }
        return ResultUtils.fail(ResultUtils.CODE_BUS_FAIL, ex.getMessage());
    }


    // 404
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("{}", ex);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        if (RequestUtils.isAjaxRequest(request)) {
            ResultDomain domain = ResultUtils.fail(ResultUtils.CODE_SYS_FAIL, ex.getMessage());
            RequestUtils.setJsonResponse(response, domain, objectMapper);
            return null;
        } else {
            model.addAttribute("msg", ex.getMessage());
            model.addAttribute("requestUri", request.getRequestURI());
            return "404";
        }
    }

    //500
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("{}", ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (RequestUtils.isAjaxRequest(request)) {
            ResultDomain domain = ResultUtils.fail(ResultUtils.CODE_SYS_FAIL, getStackTraceAsString(ex));
            RequestUtils.setJsonResponse(response, domain, objectMapper);
            return null;
        } else {
            model.addAttribute("msg", getStackTraceAsString(ex));
            model.addAttribute("requestUri", request.getRequestURI());
            return "error";
        }
    }


    public String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
