package com.sxkj.edu.config;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *@Description : 线程池配置
 *@Author : JIANGJJ
 *@Date : 2022-12-29 14:35
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Getter
@Configuration
public class CommonConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 处理数据核心线程数量
     */
    @Value("${air.global.thread_pool_core_size}")
    private Integer threadPoolCoreSize;

    /**
     * 处理数据最大线程数量
     */
    @Value("${air.global.thread_pool_max_size}")
    private Integer threadPoolMaxSize;
}

