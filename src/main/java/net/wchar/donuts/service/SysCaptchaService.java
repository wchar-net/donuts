package net.wchar.donuts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wchar.donuts.model.po.SysCaptchaPo;

/**
 * 验证码
 * @author Elijah
 */
public interface SysCaptchaService extends IService<SysCaptchaPo> {

    /**
     * 保存验证码
     * @param ip  ip
     * @param  userAgent user-agent
     * @param uri uri
     * @param method http method
     * @param captchaKey  captchaKey
     * @param captchaCode captchaCode
     * @param imageBase64 img base64
     * @param seconds 有效期(秒)
     */
    boolean saveCaptcha(String ip, String userAgent, String uri, String method, String captchaKey, String captchaCode, String imageBase64, int seconds);


    /**
     * 校验验证码
     */
    void checkCaptcha(String captchaKey, String captchaCode);
}
