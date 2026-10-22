package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 教师个人信息详情VO
 *
 * @author system
 * @date 2026-06-13
 */
@Data
@ApiModel("教师个人信息详情VO")
public class AsTeacherPersonDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("头像地址")
    private String imgUrl;

    @ApiModelProperty("教师工号")
    private String teacherNo;

    @ApiModelProperty("性别：0男 1女")
    private Integer gender;

    @ApiModelProperty("登录用户名")
    private String loginName;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("是否班主任：0否 1是")
    private Integer isHeadTeacher;

    @ApiModelProperty("年级名称列表")
    private List<String> gradeNames;

    @ApiModelProperty("班级名称列表")
    private List<String> classNames;

    @ApiModelProperty("职位")
    private String position;

    @ApiModelProperty("学段,1：小学 2：初中 3：高中")
    private Integer schoolStage;
}
