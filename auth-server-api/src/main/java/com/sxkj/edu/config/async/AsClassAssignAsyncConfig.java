package com.sxkj.edu.config.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 仅启用课后服务发布及行政班安排异步方法，不改变项目中其它普通 {@code @Async} 方法的行为。
 */
@Configuration
@EnableAsync(annotation = AsClassAssignAsync.class)
public class AsClassAssignAsyncConfig {
}
