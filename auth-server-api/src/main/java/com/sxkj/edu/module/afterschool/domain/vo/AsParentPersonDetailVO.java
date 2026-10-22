package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 家长个人信息详情VO
 *
 * @author system
 * @date 2026-06-13
 */
@Data
@ApiModel("家长个人信息详情VO")
public class AsParentPersonDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ========== 学生信息 ==========

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生稳定身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("学生快照所属学期ID")
    private Long termId;

    @ApiModelProperty("学生快照所属学期名称")
    private String termName;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生性别：0男 1女")
    private Integer studentGender;

    @ApiModelProperty("学生身份证(脱密后)")
    private String idCard;

    @ApiModelProperty("学生头像地址")
    private String imgUrl;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    // ========== 家长(当前用户)信息 ==========

    @ApiModelProperty("家长记录ID")
    private Long guardianId;

    @ApiModelProperty("家长姓名")
    private String parentName;

    @ApiModelProperty("亲属关系")
    private String relationName;

    @ApiModelProperty("家长手机号")
    private String phone;

    @ApiModelProperty("家长性别：0男 1女")
    private Integer gender;

    @ApiModelProperty("登录用户名(脱密后)")
    private String loginName;

    @ApiModelProperty("当前家长在本学校绑定的孩子选项")
    private List<AsGuardianStudentOptionVO> studentOptions = new ArrayList<>();
}
