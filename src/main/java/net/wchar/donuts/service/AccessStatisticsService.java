package net.wchar.donuts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wchar.donuts.model.bo.AccessStatisticsBo;
import net.wchar.donuts.model.po.AccessStatisticsPo;

import java.util.List;


/**
 * 访问量
 * @author Elijah
 */
public interface AccessStatisticsService extends IService<AccessStatisticsPo> {

    //top 10 uri 访问量统计
    List<AccessStatisticsBo> getTop10Visits();

    //top 10 ip 访问量统计
    List<AccessStatisticsBo> getTop10Ip();
}
