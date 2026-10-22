package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师对学生的评价反馈VO
 *
 * @author system
 * @date 2026-06-21
 */
@Data
public class AsTeacherFeedbackVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("授课教师名称")
    private String teacherName;

    @ApiModelProperty("评价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    @ApiModelProperty("评价意见")
    private String commentText;

    @ApiModelProperty("课程班名称")
    private String courseClassName;
}
