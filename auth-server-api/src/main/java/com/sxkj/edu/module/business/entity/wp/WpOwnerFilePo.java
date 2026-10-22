package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseAssignIdEntity;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import lombok.Data;

import java.util.Date;

/**
 *@Description : 教师资源表，一个文件对应多个教师
 *@Author : JIANGJJ
 *@Date : 2026-7-6 18:23
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
@TableName("t_wp_owner_file")
public class WpOwnerFilePo extends BaseAssignIdEntity {


    private Long userId;

    // 原作者id
    private Long authUserId;

    private Long sessionId;

    private Long fileId;

    private Long schoolId;

    private Long resourceId;

}
