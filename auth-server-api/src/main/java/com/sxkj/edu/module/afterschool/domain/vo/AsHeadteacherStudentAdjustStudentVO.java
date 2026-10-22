package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 班主任端-学生调课-可调课学生视图对象
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@Data
@ApiModel("班主任-可调课学生视图")
public class AsHeadteacherStudentAdjustStudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("头像URL")
    private String avatarFileUrl;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("行政班ID")
    private Long classId;

    @ApiModelProperty("行政班名称")
    private String className;
}
