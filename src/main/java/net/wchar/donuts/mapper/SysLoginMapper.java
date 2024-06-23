package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wchar.donuts.model.po.SysLoginPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录
 * @author Elijah
 */
@Mapper
public interface SysLoginMapper extends BaseMapper<SysLoginPo> {
}
