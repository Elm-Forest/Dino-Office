package com.ctgu.oaweb.config;

import com.ctgu.oaweb.handler.JwtInterceptor;
import com.ctgu.oaweb.handler.AdminInterceptor;
import com.ctgu.oaweb.handler.SuperAdminInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Zhang Jinming
 * @create
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getJwtInterceptor())
                .addPathPatterns("/user/**").excludePathPatterns("/user/dept")
                .addPathPatterns("/doc/**")
                .addPathPatterns("/message/**")
                .addPathPatterns("/department/**")
                .addPathPatterns("/employee/**")
                .addPathPatterns("/check/**")
                .addPathPatterns("/schedule/**")
                .addPathPatterns("/note/**")
                .order(-1);
        registry.addInterceptor(getAdminInterceptor())
                .addPathPatterns("/department/**").excludePathPatterns("/department/create")
                .addPathPatterns("/employee/**")
                .order(100);
        registry.addInterceptor(getSuperAdminInterceptor())
                .addPathPatterns("/department/**").excludePathPatterns("/department/create")
                .order(200);
    }

    @Bean
    public SuperAdminInterceptor getSuperAdminInterceptor() {
        return new SuperAdminInterceptor();
    }

    @Bean
    public JwtInterceptor getJwtInterceptor() {
        return new JwtInterceptor();
    }

    @Bean
    public AdminInterceptor getAdminInterceptor() {
        return new AdminInterceptor();
    }
}
