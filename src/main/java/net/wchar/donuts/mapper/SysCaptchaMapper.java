package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wchar.donuts.model.po.SysCaptchaPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 * @author Elijah
 */
@Mapper
public interface SysCaptchaMapper extends BaseMapper<SysCaptchaPo> {
}
