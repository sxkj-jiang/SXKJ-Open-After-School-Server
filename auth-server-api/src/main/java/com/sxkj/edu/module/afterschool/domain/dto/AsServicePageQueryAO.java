package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课后服务分页查询对象。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务分页查询对象")
public class AsServicePageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID，不传时取当前登录学校")
    private Long schoolId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("服务状态：0未发布 1报名中 2进行中 3已结束（兼容旧版，建议使用 displayTab）")
    private Integer serviceStatus;

    @ApiModelProperty("Tab展示状态：0未发布 1待报名 2报名中 3待开课 4进行中 5已结束；不传表示全部")
    private Integer displayTab;

    @ApiModelProperty("服务名称/编码关键词")
    private String keyword;

    @ApiModelProperty("开课周期筛选开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date courseStartDate;

    @ApiModelProperty("开课周期筛选结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date courseEndDate;
}
