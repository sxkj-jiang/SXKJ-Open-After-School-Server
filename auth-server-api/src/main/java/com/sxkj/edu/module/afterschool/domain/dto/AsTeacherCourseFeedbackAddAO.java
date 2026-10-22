package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师课程反馈-新增/编辑参数
 */
@Data
@ApiModel("教师课程反馈-新增/编辑参数")
public class AsTeacherCourseFeedbackAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("反馈记录ID（编辑时传入）")
    private Long id;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "课次ID不能为空")
    @ApiModelProperty(value = "课次ID", required = true)
    private Long sessionId;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "课程班ID", required = true)
    private Long courseClassId;

    @NotBlank(message = "课堂总结不能为空")
    @ApiModelProperty(value = "课堂总结", required = true)
    private String classroomSummary;

    @ApiModelProperty("表扬学生ID集合，JSON数组")
    private String praiseStudentIds;

    @ApiModelProperty("改进学生ID集合，JSON数组")
    private String improveStudentIds;

    @ApiModelProperty("学生表现附件ID集合，JSON数组")
    private String studentFeedbackJson;

    @NotNull(message = "提交状态不能为空")
    @ApiModelProperty(value = "提交状态：0草稿 1已提交", required = true)
    private Integer submitStatus;
}
