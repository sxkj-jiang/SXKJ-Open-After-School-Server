package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理后台-问题整改工单多条件分页查询返回VO
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("管理后台-问题整改工单多条件分页查询返回VO")
public class AsRectificationOrderAdminVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问题整改ID")
    private Long id;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("问题描述")
    private String problemDesc;

    @ApiModelProperty("提交人姓名")
    private String createUserName;

    @ApiModelProperty("处理人姓名")
    private String responsibleTeacherName;

    @ApiModelProperty("提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadlineTime;

    @ApiModelProperty("整改状态：0待整改 1整改中 2已整改 3已完成 4已逾期")
    private Integer rectifyStatus;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}
