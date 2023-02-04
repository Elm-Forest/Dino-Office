package com.ctgu.web.config;

import com.ctgu.web.handler.AdminInterceptor;
import com.ctgu.web.handler.JwtInterceptor;
import com.ctgu.web.handler.SuperAdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 * @create
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private static final Integer USER_RIGHT = -1;
    private static final Integer ADMIN_RIGHT = 100;
    private static final Integer SUPER_ADMIN_RIGHT = 200;

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Resource
    private AdminInterceptor adminInterceptor;

    @Resource
    private SuperAdminInterceptor superAdminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/user/**").excludePathPatterns("/user/dept")
                .addPathPatterns("/doc/**")
                .addPathPatterns("/message/**")
                .addPathPatterns("/department/**")
                .addPathPatterns("/employee/**")
                .addPathPatterns("/check/**")
                .addPathPatterns("/schedule/**")
                .addPathPatterns("/note/**")
                .addPathPatterns("/socket/**")
                .order(USER_RIGHT);
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/department/**")
                .excludePathPatterns("/department/create", "/department/img")
                .addPathPatterns("/check/admin/**")
                .addPathPatterns("/employee/**")
                .order(ADMIN_RIGHT);
        registry.addInterceptor(superAdminInterceptor)
                .addPathPatterns("/department/**")
                .excludePathPatterns("/department/create", "/department/img")
                .order(SUPER_ADMIN_RIGHT);
    }
}
