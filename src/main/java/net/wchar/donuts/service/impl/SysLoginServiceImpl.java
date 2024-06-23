package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.mapper.SysLoginMapper;
import net.wchar.donuts.model.po.SysLoginPo;
import net.wchar.donuts.service.SysLoginService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 登录
 * @author Elijah
 */
@Service
public class SysLoginServiceImpl extends ServiceImpl<SysLoginMapper, SysLoginPo> implements SysLoginService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean logout() {
        return lambdaUpdate().set(SysLoginPo::getLoginStatus, 0)
                .set(SysLoginPo::getDelFlag, 1)
                .set(SysLoginPo::getUpdateBy, "SysLoginServiceImpl->logout")
                .set(SysLoginPo::getUpdateTime, LocalDateTime.now())
                .eq(SysLoginPo::getXToken, UserContextHolder.getUser().getXToken())
                .eq(SysLoginPo::getUserId, UserContextHolder.getUser().getUserId())
                .update();
    }
}
