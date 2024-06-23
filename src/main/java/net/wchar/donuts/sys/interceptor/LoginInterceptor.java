package net.wchar.donuts.sys.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.wchar.donuts.api.FileApi;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.model.bo.DeptBo;
import net.wchar.donuts.model.bo.RoleBo;
import net.wchar.donuts.model.bo.LoginBo;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.model.po.SysLoginPo;
import net.wchar.donuts.model.po.SysUserPo;
import net.wchar.donuts.service.SysLoginService;
import net.wchar.donuts.service.SysUserService;
import net.wchar.donuts.sys.util.RequestUtils;
import net.wchar.donuts.sys.util.ResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 登录拦截器
 * @author Elijah
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${wl.list}")
    private String wlList;

    public static final String X_TOKEN = "x_token";
    public static final String NOT_LOGIN_REDIRECT_ADDR = "/login.action";

    public static  final String CURRENT_USER = "currentUser";

    @Resource
    private SysLoginService sysLoginService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //所有后台接口都是action结尾
        if (!request.getRequestURI().endsWith("action")) {
            return true;
        }

        //白名单
        if (checkWlList(request.getRequestURI())) {
            return true;
        }

        UserContextHolder.clear();
        String xToken = RequestUtils.getCookie(request, X_TOKEN);
        if (!StringUtils.hasText(xToken)) {
            xToken = request.getHeader(X_TOKEN);
        }
        //返回空token正确,有值则token错误
        String checkToken = checkToken(xToken,request);

        boolean tokenFlag = StringUtils.hasText(checkToken);
        boolean ajaxRequestFlag = RequestUtils.isAjaxRequest(request);
        if (tokenFlag) {
            if (ajaxRequestFlag) {
                ResultDomain domain = ResultUtils.fail(ResultUtils.CODE_LOGIN_FAIL, checkToken);
                RequestUtils.setJsonResponse(response, domain, objectMapper);
            } else {
                response.sendRedirect(request.getContextPath() + NOT_LOGIN_REDIRECT_ADDR);
            }
            return false;
        }
        return true;
    }

    //是否是百名单
    private boolean checkWlList(String url) {
        if (!StringUtils.hasText(wlList)) {
            return true;
        }
        String[] arr = StringUtils.delimitedListToStringArray(wlList, ",");
        if (null == arr || arr.length == 0) {
            return true;
        }
        boolean flag = false;
        for (String item : arr) {
            if (url.equalsIgnoreCase(item)) {
                flag = true;
            }
        }
        return flag;
    }

    //返回空token正确,有值则token错误
    private String checkToken(String xToken,HttpServletRequest request) {
        if (!StringUtils.hasText(xToken)) {
            //重定向到登录页 未找到token
            return "无效的token!";
        }

        SysLoginPo sysLoginPo = sysLoginService.lambdaQuery().eq(SysLoginPo::getDelFlag, 1).eq(SysLoginPo::getXToken, xToken).one();
        if (null == sysLoginPo || null == sysLoginPo.getId()) {
            //重定向到登录页 token数据不存在
            return "无效的token!";
        }
        //登录状态 1有效 0无效
        Integer loginStatus = sysLoginPo.getLoginStatus();
        if (1 != loginStatus) {
            //重定向到登录页 token无效
            return "无效的token!";
        }
        LocalDateTime loginExpTime = sysLoginPo.getLoginExpTime();
        if (loginExpTime.isBefore(LocalDateTime.now())) {
            //重定向到登录页 token过期了
            return "token已经过期!";
        }


        UserContextHolder.clear();
        //用户
        SysUserPo userPo = sysUserService.getById(sysLoginPo.getUserId());
        LoginBo loginBo = LoginBo.builder().userId(sysLoginPo.getUserId())
                .userName(userPo.getUserName()).xToken(xToken).userNick(userPo.getUserNick())
                .userAvatar(userPo.getUserAvatar())
                .build();
        if(!StringUtils.hasText(loginBo.getUserAvatar())){
            //默认头像
            loginBo.setUserAvatar(FileApi.DEFAULT_AVATAR);
        }else {
            loginBo.setUserAvatar(FileApi.FILE_PREFIX + "/" + loginBo.getUserAvatar());
        }

        //部门
        List<DeptBo> deptList = sysUserService.getAllDept(sysLoginPo.getUserId());
        loginBo.setDeptList(deptList);

        //角色信息
        List<RoleBo> roles = sysUserService.getAllRole(sysLoginPo.getUserId());
        loginBo.setRoles(roles);

        //权限
        List<String> perms = sysUserService.getUserAllMenuPermission(sysLoginPo.getUserId());
        loginBo.setPerms(perms);

        UserContextHolder.setUser(loginBo);
        return null;
    }
}
