package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.mapper.SysCaptchaMapper;
import net.wchar.donuts.model.po.SysCaptchaPo;
import net.wchar.donuts.service.SysCaptchaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;


/**
 * 验证码
 * @author Elijah
 */
@Service
public class SysCaptchaServiceImpl extends ServiceImpl<SysCaptchaMapper, SysCaptchaPo> implements SysCaptchaService {

    @Resource
    private SysCaptchaMapper sysCaptchaMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCaptcha(String ip,String userAgent,String uri,String method, String captchaKey, String captchaCode, String imageBase64,int seconds) {
        LocalDateTime now = LocalDateTime.now();
        //过期时间 + seconds
        LocalDateTime captchaExpTime = now.plusSeconds(seconds);
        SysCaptchaPo captchaPo = SysCaptchaPo.builder()
                .ip(ip)
                .uri(uri)
                .method(method)
                .userAgent(userAgent)
                .captchaKey(captchaKey)
                .captchaCode(captchaCode)
                //1正常 0失效
                .captchaStatus(1)
                //30秒
                .captchaExpTime(captchaExpTime)
                .captchaImgBase64(imageBase64)
                .createTime(now)
                .build();
        return save(captchaPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkCaptcha(String captchaKey, String captchaCode) {
        Assert.hasText(captchaKey, "captchaKey 不能为空!");
        Assert.hasText(captchaCode, "captchaCode 不能为空!");
        SysCaptchaPo recordPo = lambdaQuery().eq(SysCaptchaPo::getCaptchaKey, captchaKey).one();
        if (null == recordPo || null == recordPo.getId()) {
            throw new BusException("无效的验证码!");
        }
        Integer captchaStatus = recordPo.getCaptchaStatus();
        if (1 != captchaStatus) {
            throw new BusException("验证码已过期!");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime captchaExpTime = recordPo.getCaptchaExpTime();
        if (captchaExpTime.isAfter(now)) {
        } else {
            throw new BusException("验证码已经过期!");
        }
        if (!captchaCode.equalsIgnoreCase(recordPo.getCaptchaCode())) {
            throw new BusException("验证码不正确!");
        }
        //失效验证码
        lambdaUpdate().eq(SysCaptchaPo::getCaptchaKey, captchaKey)
                .set(SysCaptchaPo::getCaptchaStatus, 0).update();
    }
}
