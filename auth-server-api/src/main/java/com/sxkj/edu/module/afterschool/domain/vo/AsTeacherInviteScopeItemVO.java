package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 邀请选课范围内学生年级/班级去重结果。
 * <p>由单条 SQL JOIN 查询返回，避免先加载全部学生再逐条查年级班级。</p>
 */
@Data
@ApiModel("邀请选课范围年级班级项")
public class AsTeacherInviteScopeItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;
}
