package com.sxkj.edu.domain.sys.entity.mult;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JJJ
 * @version 1.0
 * @Description 访问类型统计
 * @Classname StatisticsVisitSource
 * @company sx
 * @Date 2020/10/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("up_statistics_visit_source")
public class StatisticsVisitSource implements Serializable {

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
     * 来源类型  学习平台 pc-xxpt,教务端 pc-jw,小程序 xcx
     */
    @TableField("type")
    private String type;

    /**
     * 系统类型
     */
    @TableField("os_name")
    private String osName;

    /**
     * 系统版本号
     */
    @TableField("os_version")
    private String osVersion;


    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

}
