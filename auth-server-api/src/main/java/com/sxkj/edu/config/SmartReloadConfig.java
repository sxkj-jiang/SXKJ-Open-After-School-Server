package com.sxkj.edu.config;

import com.sxkj.edu.config.reload.SmartReloadManager;
import com.sxkj.edu.config.reload.interfaces.SmartReloadThreadLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class SmartReloadConfig {

    @Value("${smart-reload.thread-count}")
    private Integer threadCount;

    @Bean
    public SmartReloadManager initSmartReloadManager() {
        /**
         * 创建 Reload Manager 调度器
         */
        SmartReloadManager smartReloadManager = new SmartReloadManager(new SmartReloadThreadLogger() {
            @Override
            public void error(String string) {
                log.error(string);
            }

            @Override
            public void error(String string, Throwable e) {
                log.error(string, e);
            }
        }, threadCount);
        return smartReloadManager;
    }
}
