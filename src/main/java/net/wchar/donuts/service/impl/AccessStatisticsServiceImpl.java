package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.mapper.AccessStatisticsMapper;
import net.wchar.donuts.model.bo.AccessStatisticsBo;
import net.wchar.donuts.model.po.AccessStatisticsPo;
import net.wchar.donuts.service.AccessStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 访问量
 * @author Elijah
 */
@Service
public class AccessStatisticsServiceImpl extends ServiceImpl<AccessStatisticsMapper, AccessStatisticsPo> implements AccessStatisticsService {

    private final AccessStatisticsMapper accessStatisticsMapper;

    public AccessStatisticsServiceImpl(AccessStatisticsMapper accessStatisticsMapper) {
        this.accessStatisticsMapper = accessStatisticsMapper;
    }

    @Override
    public List<AccessStatisticsBo> getTop10Visits() {
        return accessStatisticsMapper.getTop10Visits();
    }

    @Override
    public List<AccessStatisticsBo> getTop10Ip() {
        return accessStatisticsMapper.getTop10Ip();
    }
}
