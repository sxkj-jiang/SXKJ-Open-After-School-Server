package com.sxkj.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.domain.sys.entity.BaseBintangEntity;
import com.sxkj.edu.module.mapper.base.BaseBintangMapper;
import com.sxkj.edu.service.BaseBintangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 免登地址表 服务实现类
 *
 * @author system
 */
@Service
@Slf4j
public class BaseBintangServiceImpl extends ServiceImpl<BaseBintangMapper, BaseBintangEntity> implements BaseBintangService {

    private static final String REDIS_KEY_BINTANG_URLS = "base:bintang:urls";
    private static final long CACHE_EXPIRE_SECONDS = 24 * 60 * 60L;
    private static final String FIELD_VALUE_PLACEHOLDER = "1";

    @Resource
    private RedisCache redisCache;

    /**
     * 服务启动时将全量免鉴权URL加载到Redis Hash
     * Hash结构：field=url, value="1"，支持O(1)的hHasKey判断
     */
    @PostConstruct
    public void initBintangUrlCache() {
        try {
            doRefreshCache();
            log.info("免鉴权URL缓存初始化完成，redisKey={}", REDIS_KEY_BINTANG_URLS);
        } catch (Exception e) {
            log.error("免鉴权URL缓存初始化失败", e);
        }
    }

    @Override
    public List<BaseBintangEntity> getListBaseUrlAll() {
        return this.list();
    }

    @Override
    public boolean isExemptUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return redisCache.hHasKey(REDIS_KEY_BINTANG_URLS, url);
    }

    @Override
    public void refreshBintangUrlCache() {
        doRefreshCache();
        log.info("免鉴权URL缓存手动刷新完成，redisKey={}", REDIS_KEY_BINTANG_URLS);
    }

    /**
     * 执行缓存刷新：先删除旧key，再从DB加载全量数据写入Redis Hash
     */
    private void doRefreshCache() {
        redisCache.del(REDIS_KEY_BINTANG_URLS);
        List<BaseBintangEntity> list = this.list();
        if (list == null || list.isEmpty()) {
            return;
        }
        Map<String, Object> urlMap = new HashMap<>(list.size());
        for (BaseBintangEntity entity : list) {
            if (entity.getUrl() != null && !entity.getUrl().isEmpty()) {
                urlMap.put(entity.getUrl(), FIELD_VALUE_PLACEHOLDER);
            }
        }
        if (!urlMap.isEmpty()) {
            redisCache.hmset(REDIS_KEY_BINTANG_URLS, urlMap, CACHE_EXPIRE_SECONDS);
        }
    }
}
