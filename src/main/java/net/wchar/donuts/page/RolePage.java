package net.wchar.donuts.page;


import net.wchar.donuts.sys.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 角色页
 * @author Elijah
 */
@Controller
@RequestMapping("/role")
public class RolePage {

    @GetMapping("/role.action")
    @RequiresPermissions("sys:role:view")
    public String role(){
        return "role/role";
    }

    //分配菜单
    @GetMapping("/assignMenu.action")
    @RequiresPermissions("sys:role:assign")
    public String assignMenu(){
        return "role/assign-menu";
    }

    //添加角色
    @GetMapping("/addRole.action")
    @RequiresPermissions("sys:role:add")
    public String addRole(){
        return "role/add-role";
    }

    //修改角色
    @GetMapping("/editRole.action")
    @RequiresPermissions("sys:role:edit")
    public String editRole(){
        return "role/edit-role";
    }
}
