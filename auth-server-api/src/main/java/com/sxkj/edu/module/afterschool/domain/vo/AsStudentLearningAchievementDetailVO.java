package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 学生学习成果详情VO
 *
 * @author system
 * @date 2026-06-23
 */
@Data
@ApiModel("学生学习成果详情VO")
public class AsStudentLearningAchievementDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("打卡日期（创建日期）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("反馈内容")
    private String feedbackContent;

    @ApiModelProperty("打卡上传的附件ID集合，JSON数组")
    private String feedbackFileIds;

    @ApiModelProperty("附件地址集合")
    private List<String> attachmentUrls;

    @ApiModelProperty("学生抬头")
    private String studentTitle;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("自然班级ID")
    private Long classId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期")
    private String termName;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;


}
