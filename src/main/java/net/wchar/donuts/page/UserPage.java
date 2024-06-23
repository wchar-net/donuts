package net.wchar.donuts.page;

import net.wchar.donuts.sys.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 用户页
 * @author Elijah
 */
@Controller
@RequestMapping("/user")
public class UserPage {

    /**
     * 用户管理
     */
    @RequiresPermissions("sys:user:view")
    @GetMapping("/user.action")
    public String user(){
        return "user/user";
    }

    /**
     * 角色分配
     */
    @RequiresPermissions("sys:user:assign")
    @GetMapping("/assignRole.action")
    public String assignRole(){
        return "user/assign-role";
    }

    /**
     * 修改用户
     */
    @RequiresPermissions("sys:user:edit")
    @GetMapping("/editUser.action")
    public String editUser(){
        return "user/edit-user";
    }

    /**
     * 添加用户
     */
    @RequiresPermissions("sys:user:add")
    @GetMapping("/addUser.action")
    public String addUser(){
        return "user/add-user";
    }

    /**
     * 修改用户密码
     */
    @GetMapping("/editUserPwd.action")
    public String editUserPwd(){
        return "user/edit-user-pwd";
    }

}
