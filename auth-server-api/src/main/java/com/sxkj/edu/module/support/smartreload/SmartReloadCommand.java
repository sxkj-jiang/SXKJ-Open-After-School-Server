package com.sxkj.edu.module.support.smartreload;

import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.reload.abstracts.AbstractSmartReloadCommand4Spring;
import com.sxkj.edu.config.reload.domain.entity.ReloadItem;
import com.sxkj.edu.config.reload.domain.entity.SmartReloadResult;
import com.sxkj.edu.module.support.smartreload.dao.ReloadItemDao;
import com.sxkj.edu.module.support.smartreload.dao.ReloadResultDao;
import com.sxkj.edu.module.support.smartreload.domain.entity.ReloadItemEntity;
import com.sxkj.edu.module.support.smartreload.domain.entity.ReloadResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Smart Reload 业务
 *
 * @author listen
 * @date 2018/02/10 09:18
 */
@Component
public class SmartReloadCommand extends AbstractSmartReloadCommand4Spring {

    @Autowired
    private ReloadItemDao reloadItemDao;

    @Autowired
    private ReloadResultDao reloadResultDao;

    /**
     *
     * @return List<ReloadItem>
     */
    @Override
    public List<ReloadItem> readReloadItem() {
        List<ReloadItemEntity> reloadItemEntityList = reloadItemDao.selectList(null);
        return SmartBeanUtil.copyList(reloadItemEntityList, ReloadItem.class);
    }

    /**
     * 保存reload结果
     *
     * @param smartReloadResult
     */
    @Override
    public void handleReloadResult(SmartReloadResult smartReloadResult) {
        ReloadResultEntity reloadResultEntity = SmartBeanUtil.copy(smartReloadResult, ReloadResultEntity.class);
        reloadResultDao.insert(reloadResultEntity);
    }
}
