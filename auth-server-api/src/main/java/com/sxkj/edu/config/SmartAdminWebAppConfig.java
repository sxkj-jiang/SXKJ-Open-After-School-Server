package com.sxkj.edu.config;

import com.sxkj.edu.interceptor.SmartAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class SmartAdminWebAppConfig implements WebMvcConfigurer {

    @Autowired
    private SmartAuthenticationInterceptor smartAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smartAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/v2/api-docs-ext",
                        "/doc.html",
                        "/doc.html/**",
                        "/swagger-ui.html",
                        "/swagger-ui.html/**",
                        "/knife4j/**"
                );
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/druidMonitor").setViewName("redirect:/druid/index.html");
        registry.addViewController("/swaggerApi").setViewName("redirect:/swagger-ui.html");
    }
}
