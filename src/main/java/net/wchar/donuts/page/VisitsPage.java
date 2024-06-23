package net.wchar.donuts.page;

import net.wchar.donuts.sys.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 统计信息
 * @author Elijah
 */
@Controller
@RequestMapping("/statistics")
public class VisitsPage {

    //访问量统计
    @GetMapping("/visits.action")
    @RequiresPermissions("sys:visitsStatistics:view")
    public String visitsStatistics(){
        return "/statistics/visits";
    }
}
