package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AsCourseFeedbackStuDetailVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty(value = "学习打卡记录id")
    private Long id;

    @ApiModelProperty(value = "课次ID")
    private Long sessionId;

    @ApiModelProperty(value = "课程班ID")
    private Long courseClassId;

    @ApiModelProperty(value = "授课教师ID")
    private Long teacherId;

    @ApiModelProperty(value = "反馈标题")
    private String feedbackTitle;

    @ApiModelProperty(value = "反馈内容")
    private String feedbackContent;

    @ApiModelProperty(value = "附件ID集合，JSON数组字符串")
    private String feedbackFileIds;

    @ApiModelProperty(value = "附件ID集合，JSON数组字符串")
    private List<FileVO> feedbackFileList;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty("学生头像地址")
    private String imgUrl;

}
