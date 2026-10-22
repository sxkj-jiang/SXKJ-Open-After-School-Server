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
 * 课程评论表
 * </p>
 *
 * @author zhengqn
 * @since 2023-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_portal_comment")
@ApiModel(value="PortalComment对象", description="评论表")
public class PortalCommentEntity implements Serializable {


    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "课程id")
    @TableField("link_id")
    private Long linkId;

    @ApiModelProperty(value = "评论")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "评论类型 1-课程评论 2-练习评论 3-评论回复")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "评分 1-10")
    @TableField("score")
    private Integer score;

    @ApiModelProperty(value = "评论人")
    @TableField("create_user")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;


    @ApiModelProperty(value = "评论人头像")
    private String noteUrl;

}
