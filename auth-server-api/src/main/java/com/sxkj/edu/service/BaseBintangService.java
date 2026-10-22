package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.domain.sys.entity.BaseBintangEntity;

import java.util.List;

/**
 * <p>
 * 免登地址表 服务类
 * </p>
 *
 * @author zhengqn
 * @since 2023-02-20
 */
public interface BaseBintangService extends IService<BaseBintangEntity> {

    List<BaseBintangEntity> getListBaseUrlAll();

    /**
     * 判断URL是否在免鉴权缓存中（O(1)复杂度）
     *
     * @param url 请求URL
     * @return true=免鉴权放行
     */
    boolean isExemptUrl(String url);

    /**
     * 刷新免鉴权URL缓存（从DB重新加载到Redis Hash）
     */
    void refreshBintangUrlCache();
}
