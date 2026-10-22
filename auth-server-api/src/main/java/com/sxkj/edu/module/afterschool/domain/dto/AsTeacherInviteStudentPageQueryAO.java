package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师端邀请可选学生分页查询
 */
@Data
@ApiModel("教师端邀请可选学生分页查询")
public class AsTeacherInviteStudentPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "邀请目标课程班ID", required = true)
    private Long courseClassId;

    @ApiModelProperty("学期ID，不传时由课程班所属课后服务推导")
    private Long termId;

    @ApiModelProperty("学号/姓名关键词")
    private String keyword;

    @ApiModelProperty(value = "年级ID",required = true)
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("是否仅返回可勾选学生，默认true")
    private Boolean onlySelectable;
}
