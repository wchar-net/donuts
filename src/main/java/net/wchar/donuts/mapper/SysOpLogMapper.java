package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wchar.donuts.model.po.SysOpLogPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志
 * @author Elijah
 */
@Mapper
public interface SysOpLogMapper extends BaseMapper<SysOpLogPo> {
}
