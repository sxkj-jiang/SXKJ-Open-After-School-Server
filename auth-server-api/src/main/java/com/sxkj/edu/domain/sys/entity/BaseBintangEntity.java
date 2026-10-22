package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 免登地址表
 * </p>
 *
 * @author jjj
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_base_bintang")
@ApiModel(value="BaseBintang对象", description="免登地址表")
public class BaseBintangEntity implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "免登接口地址")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;


}
