package com.sxkj.edu.domain.sys.entity.mult;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jjj
 * @version 1.0
 * @Description pv上报
 * @Classname AirFaithAgent
 * @company sx
 * @Date 2020/10/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("up_statistics_pv")
public class StatisticsPv implements Serializable {

    /**
     * 主键ID
     */
    @TableId("id")
    private String id;

    /**
     * ip
     */
    @TableField("ip")
    private String ip;

    /**
     * 来源类型  平台网页端 PTPC,管理端 JWPC,平台小程序 XCX
     */
    @TableField("type")
    private String type;

    /**
     * 模块
     */
    @TableField("model")
    private String model;

    /**
     * 页面
     */
    @TableField("source_page")
    private String sourcePage;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    /**
     * 页面代码
     */
    @TableField("page_code")
    private String pageCode;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户角色
     */
    @TableField("role_ids")
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
