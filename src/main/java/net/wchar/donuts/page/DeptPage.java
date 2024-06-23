package net.wchar.donuts.page;


import net.wchar.donuts.sys.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 部门页面
 * @author Elijah
 */
@Controller
@RequestMapping("/dept")
public class DeptPage {

    @GetMapping("/dept.action")
    @RequiresPermissions("sys:dept:view")
    public String role() {
        return "dept/dept";
    }

    //添加部门
    @GetMapping("/addDept.action")
    @RequiresPermissions("sys:dept:add")
    public String addDept() {
        return "dept/add-dept";
    }

    //修改部门
    @GetMapping("/editDept.action")
    @RequiresPermissions("sys:dept:edit")
    public String editDept() {
        return "dept/edit-dept";
    }
}
