package net.wchar.donuts.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录页
 * @author Elijah
 */
@Controller
public class LoginPage {

    @GetMapping("/login.action")
    public String login(){
        return "login";
    }
}
