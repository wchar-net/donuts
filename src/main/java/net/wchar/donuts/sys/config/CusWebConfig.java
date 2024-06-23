package net.wchar.donuts.sys.config;

import net.wchar.donuts.sys.interceptor.AssessStatisticsInterceptor;
import net.wchar.donuts.sys.interceptor.LoginInterceptor;
import net.wchar.donuts.sys.interceptor.PermissionInterceptor;
import net.wchar.donuts.sys.interceptor.ReferrerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * web配置
 * @author Elijah
 */
@Configuration
public class CusWebConfig implements WebMvcConfigurer {

    private final LocaleChangeInterceptor localeChangeInterceptor;

    private final ReferrerInterceptor referrerInterceptor;
    private final AssessStatisticsInterceptor assessStatisticsInterceptor;
    private final LoginInterceptor loginInterceptor;

    private final PermissionInterceptor permissionInterceptor;

    public CusWebConfig(LocaleChangeInterceptor localeChangeInterceptor, ReferrerInterceptor referrerInterceptor, AssessStatisticsInterceptor assessStatisticsInterceptor, LoginInterceptor loginInterceptor, PermissionInterceptor permissionInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
        this.referrerInterceptor = referrerInterceptor;
        this.assessStatisticsInterceptor = assessStatisticsInterceptor;
        this.loginInterceptor = loginInterceptor;
        this.permissionInterceptor = permissionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);
        registry.addInterceptor(referrerInterceptor);
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(assessStatisticsInterceptor);
        registry.addInterceptor(permissionInterceptor);
    }
}