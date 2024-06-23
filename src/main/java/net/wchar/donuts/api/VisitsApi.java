package net.wchar.donuts.api;

import net.wchar.donuts.model.bo.AccessStatisticsBo;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.service.AccessStatisticsService;
import net.wchar.donuts.sys.annotation.RequiresPermissions;
import net.wchar.donuts.sys.util.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 统计信息
 * @author Elijah
 */
@RestController
@RequestMapping("/statistics")
public class VisitsApi {

    private final AccessStatisticsService accessStatisticsService;

    public VisitsApi(AccessStatisticsService accessStatisticsService) {
        this.accessStatisticsService = accessStatisticsService;
    }

    //top 10 uri 访问量统计
    @GetMapping("/top10Visits.action")
    @RequiresPermissions("sys:visitsStatistics:view")
    public ResultDomain<List<AccessStatisticsBo>> top10Visits(){
        return ResultUtils.success(accessStatisticsService.getTop10Visits());
    }


    //top 10 ip 访问量统计
    @GetMapping("/top10Ip.action")
    @RequiresPermissions("sys:visitsStatistics:view")
    public ResultDomain<List<AccessStatisticsBo>> top10Ip(){
        return ResultUtils.success(accessStatisticsService.getTop10Ip());
    }
}
