package com.sxkj.edu.domain.sys.entity.mult;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WQ
 * @version 1.0
 * @Description 事件统计
 * @Classname AirFaithAgent
 * @company sx
 * @Date 2020/10/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("up_statistics_event")
public class StatisticsEvent implements Serializable {

    /**
     * 主键ID
     */
    private String id;

    /**
     * ip
     */
    private String ip;

    /**
     * 来源类型  平台网页端 PTPC,管理端 JWPC,平台小程序 XCX
     */
    private String type;

    /**
     * 模块
     */
    private String model;

    /**
     * 页面
     */
    private String sourcePage;

    /**
     * 事件
     */
    private String event;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 页面代码
     */
    private String pageCode;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户角色
     */
    private String roleIds;

    /**
     * 浏览器
     */
    private String remoteBrowser;

    /**
     * 操作系统
     */
    private String remoteOs;
}
