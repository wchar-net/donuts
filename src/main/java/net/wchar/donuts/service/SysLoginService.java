package net.wchar.donuts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wchar.donuts.model.po.SysLoginPo;

/**
 * 登录
 * @author Elijah
 */
public interface SysLoginService extends IService<SysLoginPo> {

    //退出
    Boolean logout();
}
