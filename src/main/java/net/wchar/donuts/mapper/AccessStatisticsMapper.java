package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wchar.donuts.model.bo.AccessStatisticsBo;
import net.wchar.donuts.model.po.AccessStatisticsPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 统计信息
 * @author Elijah
 */
@Mapper
public interface AccessStatisticsMapper extends BaseMapper<AccessStatisticsPo> {

    //top 10 uri 访问量统计
    List<AccessStatisticsBo> getTop10Visits();

    //top 10 ip 访问量统计
    List<AccessStatisticsBo> getTop10Ip();
}
