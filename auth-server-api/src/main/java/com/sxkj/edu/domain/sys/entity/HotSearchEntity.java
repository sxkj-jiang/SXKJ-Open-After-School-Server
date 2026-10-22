package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 热门搜索
 * </p>
 *
 * @author zhengqn
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_hot_search")
@ApiModel(value="HotSearch对象", description="热门搜索")
public class HotSearchEntity implements Serializable {


      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "搜索内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "搜索次数")
    @TableField("count")
    private Long count;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


}
