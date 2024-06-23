package net.wchar.donuts.page;

import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.sys.interceptor.LoginInterceptor;
import net.wchar.donuts.model.bo.LoginBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页
 * @author Elijah
 */
@Controller
public class IndexPage {
    private final Logger logger = LoggerFactory.getLogger(IndexPage.class);

    @GetMapping(value = {"/", "/index.html", "/index.htm"})
    public String defaultIndex() {
        return "redirect:/index.action";
    }


    @GetMapping("/index.action")
    public String index(Model model) {
        LoginBo user = UserContextHolder.getUser();
        logger.info("user: {}", user);
        model.addAttribute(LoginInterceptor.CURRENT_USER, user);
        return "index";
    }

    //首页
    @GetMapping("/welcome.action")
    public String welcome() {
        return "welcome";
    }

    //权限管理说明
    @GetMapping("/rbacHelp.action")
    public String rbacHelp() {
        return "help/rbacHelp";
    }
}
