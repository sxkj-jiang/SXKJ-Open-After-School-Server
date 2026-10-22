package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 班级分页查询参数
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("班级分页查询参数")
public class EduclClassQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属学校ID",required = true)
    @NotNull(message = "所属学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty("班级名称(模糊)")
    private String className;

    @ApiModelProperty(value = "年级表主键集合")
    private List<Long> gradeIds;

    @ApiModelProperty("班主任ID")
    private Long headTeacherId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("创建时间-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty("创建时间-结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
}
