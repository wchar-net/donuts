package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.mapper.SysOpLogMapper;
import net.wchar.donuts.model.po.SysOpLogPo;
import net.wchar.donuts.service.SysOpLogService;
import org.springframework.stereotype.Service;

/**
 * 日志
 * @author Elijah
 */
@Service
public class SysOpLogServiceImpl extends ServiceImpl<SysOpLogMapper, SysOpLogPo> implements SysOpLogService {

}