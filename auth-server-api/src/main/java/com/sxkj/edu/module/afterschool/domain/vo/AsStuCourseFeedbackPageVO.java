package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AsStuCourseFeedbackPageVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty(value = "学习打卡记录id")
    private Long feedbackId;

    @ApiModelProperty(value = "课次ID")
    private Long sessionId;

    @ApiModelProperty(value = "学生ID")
    private Long studentId;

    @ApiModelProperty("学生头像")
    private String avatar;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;
}
