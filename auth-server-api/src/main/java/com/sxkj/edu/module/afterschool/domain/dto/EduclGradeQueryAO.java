package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 年级分页查询参数
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("年级分页查询参数")
public class EduclGradeQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("年级名称(模糊)")
    private String gradeName;

    @ApiModelProperty("阶段")
    private Integer schoolStage;

    @ApiModelProperty("年级主任用户ID")
    private Long directorUserId;

    @ApiModelProperty("创建时间-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty("创建时间-结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

}
