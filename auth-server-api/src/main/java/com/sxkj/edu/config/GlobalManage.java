package com.sxkj.edu.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * @author jjj
 * @version 1.0
 * @Description
 * @Classname GlobalManage
 * @company sxkj
 * @Date 2022/09/09 17:27
 */
public final class GlobalManage {
    private static ExecutorService executorService = null;

    public static ExecutorService getExecutorService(CommonConfig config) {
        if (null == executorService) {
            executorService = new ThreadPoolExecutor(config.getThreadPoolCoreSize(),
                    config.getThreadPoolMaxSize(),
                    30L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    new ThreadFactoryBuilder()
                            .setNameFormat("GlobalManage-%d").build());
        }
        return executorService;
    }

}
