package com.sxkj.edu.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sxkj.edu.common.utils.Args;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyBatis-Plus 全局自动填充处理器
 * 自动填充创建时间、更新时间、创建人、更新人、是否有效等字段
 *
 * @author jiangjj
 * @date 2026-05-22
 */
@Slf4j
@Component
public class SmartMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        
        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        
        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        
        // 自动填充创建人
        Long currentUserId = SmartRequestTokenUtil.getRequestUserId();
        if (Args.isNotNull(currentUserId)) {
            this.strictInsertFill(metaObject, "createUser", Long.class, currentUserId);
        }else{
            this.strictInsertFill(metaObject, "createUser", Long.class, 1L);
        }
        
        // 自动填充更新人
        if (Args.isNotNull(currentUserId)) {
            this.strictInsertFill(metaObject, "updateUser", Long.class, currentUserId);
        }else{
            this.strictInsertFill(metaObject, "updateUser", Long.class, 1L);
        }
        
        // 自动填充是否有效标识（默认为1-有效）
        this.strictInsertFill(metaObject, "isValid", Integer.class, 1);
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");
        
        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        
        // 自动填充更新人
        Long currentUserId = SmartRequestTokenUtil.getRequestUserId();
        if (Args.isNotNull(currentUserId)) {
            this.strictUpdateFill(metaObject, "updateUser", Long.class, currentUserId);
        }
    }
}
