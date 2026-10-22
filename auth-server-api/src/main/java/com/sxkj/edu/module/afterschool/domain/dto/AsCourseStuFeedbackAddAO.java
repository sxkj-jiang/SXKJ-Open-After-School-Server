package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学习打卡-新增/编辑反馈参数
 */
@Data
@ApiModel("学习打卡-新增/编辑反馈参数")
public class AsCourseStuFeedbackAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("反馈记录ID（编辑时传入）")
    private Long id;

    @NotNull(message = "课次ID不能为空")
    @ApiModelProperty(value = "课次ID", required = true)
    private Long sessionId;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "课程班ID", required = true)
    private Long courseClassId;

    @ApiModelProperty(value = "授课教师ID", required = true)
    @NotNull(message = "授课教师ID不能为空")
    private Long teacherId;

    @NotBlank(message = "反馈标题不能为空")
    @ApiModelProperty(value = "反馈标题", required = true)
    private String feedbackTitle;

    @NotBlank(message = "反馈内容不能为空")
    @ApiModelProperty(value = "反馈内容", required = true)
    private String feedbackContent;

    @ApiModelProperty(value = "附件ID集合，JSON数组字符串", required = true)
    @NotBlank(message = "附件ID集合不能为空")
    private String feedbackFileIds;

    @ApiModelProperty("学生稳定身份ID，家长绑定多个孩子时用于指定学生")
    private Long studentIdentityId;
}
