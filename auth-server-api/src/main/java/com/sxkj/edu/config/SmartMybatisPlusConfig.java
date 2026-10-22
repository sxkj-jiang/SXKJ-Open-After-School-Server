package com.sxkj.edu.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author jiangjj
 */
@EnableTransactionManagement
@Configuration
@MapperScans({
    @MapperScan(basePackages = {"com.sxkj.edu.module.*"}, markerInterface = BaseMapper.class),
    @MapperScan(basePackages = {"com.sxkj.edu.module.support.codegenerator.dao", "com.sxkj.edu.module.support.idgenerator"})
})
public class SmartMybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        // 开启 count 的 join 优化,只针对 left join !!!
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor().setCountSqlParser(new JsqlParserCountOptimize(true));
        // 设置最大单页限制数量，默认500条，-1不受限制
        paginationInterceptor.setLimit(-1);
        return paginationInterceptor;
    }

}
