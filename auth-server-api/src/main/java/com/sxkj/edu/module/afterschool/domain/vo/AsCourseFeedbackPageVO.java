package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课堂反馈分页查询VO
 *
 * @author system
 * @date 2026-06-29
 */
@Data
@ApiModel("课堂反馈分页查询视图对象")
public class AsCourseFeedbackPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("反馈ID")
    private Long id;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程类别")
    private String categoryName;

    @ApiModelProperty("上课时间")
    private String classTime;

    @ApiModelProperty("年级")
    private String gradeNames;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}
