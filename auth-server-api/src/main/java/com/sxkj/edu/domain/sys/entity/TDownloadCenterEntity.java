package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 下载中心
 * @author jiangjj
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_download_center")
public class TDownloadCenterEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 编号
     */
    @TableField(value = "downloadNum")
    private String downloadNum;

    /**
     * 下载名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 下载地址
     */
    @TableField(value = "download_link")
    private String downloadLink;
    /**
     * 是否展示
     */
    @TableField(value = "display")
    private String display;

    /**
     * 发布时间
     */
    @TableField(value = "publish_date")
    private Date publishDate;

    private Integer isValid;
}
