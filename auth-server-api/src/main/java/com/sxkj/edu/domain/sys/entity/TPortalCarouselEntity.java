package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 *@Description : 平台管理-轮播图
 *@Author : JIANGJJ
 *@Date : 2022-12-24 11:46
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_portal_carousel")
public class TPortalCarouselEntity extends BaseModel {


    private String culNum; // 编号

    private String culName; // 轮播图名称

    private String culDesc; // 轮播图描述

    private Integer sort; // 排序

    private Integer artType; // 轮播图类型：1-首页/2-广告/3-校园新闻轮播,4-其他

    private Long culUrlId; //附件表的地址id

    private Integer status; //启用状态，1-启用，其他禁用

    private Date publishDate; //审核时间

    private Long schoolId;// 学校id

}
