package com.sxkj.edu.config;

import com.sxkj.edu.common.config.SmartSwaggerApiModelEnumPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import springfox.documentation.swagger.common.SwaggerPluginSupport;


/**
 *@Description : 对于枚举类进行swagger注解，与前端的vue-enum相匹配
 *@Author : JIANGJJ
 *@Date : 2022-9-7 14:53
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Configuration
public class SmartSwaggerApiModelEnumConfig {

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
    public SmartSwaggerApiModelEnumPlugin swaggerEnum(){
        return new SmartSwaggerApiModelEnumPlugin();
    }
}
