package net.wchar.donuts.api;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.model.bo.CaptchaBo;
import net.wchar.donuts.model.bo.MenuBo;
import net.wchar.donuts.model.bo.LoginBo;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.service.SysCaptchaService;
import net.wchar.donuts.service.SysLoginService;
import net.wchar.donuts.service.SysUserService;
import net.wchar.donuts.sys.util.RequestUtils;
import net.wchar.donuts.sys.util.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 登录接口
 * @author Elijah
 */
@Tag(name = "登录接口", description = "登录接口")
@RestController
public class LoginApi {

    private final Logger logger = LoggerFactory.getLogger(UserApi.class);

    //登录页面验证码有效期(秒)
    private int LOGIN_CAPTCHA_SECONDS = 60;

    private final SysCaptchaService sysCaptchaService;
    private final SysUserService sysUserService;

    private final SysLoginService sysLoginService;

    public LoginApi(SysCaptchaService sysCaptchaService, SysUserService sysUserService, SysLoginService sysLoginService) {
        this.sysCaptchaService = sysCaptchaService;
        this.sysUserService = sysUserService;
        this.sysLoginService = sysLoginService;
    }

    @Operation(summary = "检查token")
    @GetMapping("/checkToken.action")
    public ResultDomain<LoginBo> checkToken() {
        LoginBo user = UserContextHolder.getUser();
        return ResultUtils.success(user);
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/captcha.action")
    public ResultDomain<CaptchaBo> captcha(HttpServletRequest request) {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(120, 38, 4, 2);
        String captchaKey = UUID.randomUUID().toString().replaceAll("-", "");
        String captchaCode = captcha.getCode();
        String imageBase64 = captcha.getImageBase64Data();

        logger.info("captchaKey: {}  captchaCode: {} imageBase64:{}", captchaKey, captchaCode, imageBase64);
        sysCaptchaService.saveCaptcha(
                RequestUtils.getClientIp(request),
                RequestUtils.getUserAgent(request),
                RequestUtils.getUri(request),
                RequestUtils.getMethod(request),
                captchaKey, captchaCode, imageBase64,
                LOGIN_CAPTCHA_SECONDS);
        return ResultUtils.success(CaptchaBo.builder().captchaKey(captchaKey).imageBase64(imageBase64).build());

    }

    //登录
    @Operation(summary = "登录")
    @Parameters({
            @Parameter(name = "userName", description = "用户名", required = true),
            @Parameter(name = "userPwd", description = "密码", required = true),
            @Parameter(name = "captchaKey", description = "captchaKey", required = true),
            @Parameter(name = "captchaCode", description = "captchaCode", required = true),
    })
    @PostMapping("/login.action")
    public ResultDomain<String> login(HttpServletRequest request, String userName, String userPwd, String captchaKey, String captchaCode) {
        //检查验证码
        sysCaptchaService.checkCaptcha(captchaKey, captchaCode);
        //登录
        String xToken = sysUserService.login(RequestUtils.getUserAgent(request), RequestUtils.getClientIp(request), userName, userPwd);
        return ResultUtils.success(xToken);
    }

    //退出
    @Operation(summary = "退出")
    @PostMapping("/logout.action")
    public ResultDomain logout() {
        return ResultUtils.success(sysLoginService.logout());
    }

    //根据角色获取菜单
    @Operation(summary = "获取用户菜单")
    @Parameters({
            @Parameter(name = "lang", description = "语言"),
    })
    @GetMapping("/menu.action")
    public ResultDomain<List<MenuBo>> menu(String lang) {
        LoginBo user = UserContextHolder.getUser();
        return ResultUtils.success(sysUserService.getUserMenu(user, lang));
    }
}
