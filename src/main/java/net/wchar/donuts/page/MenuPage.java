package net.wchar.donuts.page;


import net.wchar.donuts.sys.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 菜单页
 * @author Elijah
 */
@Controller
@RequestMapping("/menu")
public class MenuPage {

    //菜单界面
    @RequiresPermissions("sys:menu:view")
    @GetMapping("/menu.action")
    public String menu(){
        return "menu/menu";
    }

    //添加菜单
    @RequiresPermissions("sys:menu:add")
    @GetMapping("/addMenu.action")
    public String addMenu(){
        return "menu/add-menu";
    }

    //删除菜单
    @RequiresPermissions("sys:menu:edit")
    @GetMapping("/editMenu.action")
    public String editMenu(){
        return "menu/edit-menu";
    }
}
