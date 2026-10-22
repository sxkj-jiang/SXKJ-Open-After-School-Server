package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理后台-巡课记录分页查询返回VO
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("管理后台-巡课记录分页查询返回VO")
public class AsPatrolRecordAdminVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("巡课计划名称")
    private String planName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("巡课类型：1专项巡课 2日常巡课")
    private Integer patrolType;

    @ApiModelProperty("巡课日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date patrolDate;

    @ApiModelProperty("巡课人员名称")
    private String patrolUserName;

    @ApiModelProperty("提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("课次")
    private String sectionName;

    @ApiModelProperty(value = "学期名称")
    private String termName;

}
