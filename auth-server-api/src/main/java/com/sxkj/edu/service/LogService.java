package com.sxkj.edu.service;

import com.sxkj.edu.common.utils.SmartThreadFactory;
import com.sxkj.edu.domain.sys.entity.UserLoginLogEntity;
import com.sxkj.edu.domain.sys.entity.UserOperateLogEntity;
import com.sxkj.edu.module.business.dao.UserLoginLogDao;
import com.sxkj.edu.module.business.dao.UserOperateLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class LogService {

    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private UserLoginLogDao userLoginLogDao;


    @Autowired
    private UserOperateLogDao userOperateLogDao;

    @PostConstruct
    void init() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(1, 2000, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), SmartThreadFactory.create("LogAspect"));
        }
    }

    @PreDestroy
    void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
            threadPoolExecutor = null;
        }
    }

    public void addLog(Object object) {
        try {
            if (object instanceof UserLoginLogEntity) {
                threadPoolExecutor.execute(() -> userLoginLogDao.insert((UserLoginLogEntity) object));
            }
            if (object instanceof UserOperateLogEntity) {
                threadPoolExecutor.execute(() -> userOperateLogDao.insert((UserOperateLogEntity) object));
            }
        } catch (Throwable e) {
            log.error("userLogAfterAdvice:{}", e);
        }
    }
}
