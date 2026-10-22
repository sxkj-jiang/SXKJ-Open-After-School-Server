package com.sxkj.edu.module.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.validation.Mobile;
import com.sxkj.edu.common.validation.SchoolCodeAnno;
import com.sxkj.edu.domain.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 学校信息表
 * </p>
 *
 * @author zhengqn
 * @since 2025-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_school_manage")
@ApiModel(value = "SchoolManagePO对象", description = "学校信息表")
public class SchoolManagePo extends BaseModel {


    @ApiModelProperty(value = "学校名称")
    @TableField("school_name")
    private String schoolName;

    @ApiModelProperty(value = "学校类型:1-大学，2-职业学院，3-中学、4-小学")
    @TableField("school_type")
    private String schoolType;

    @ApiModelProperty(value = "管理员ID")
    @TableField("manager_id")
    private Long managerId;

    @ApiModelProperty(value = "管理员姓名")
    @TableField("manager_name")
    private String managerName;

    @ApiModelProperty(value = "管理员手机号")
    @TableField("manager_phone")
    private String managerPhone;

    @ApiModelProperty(value = "学校logo图片路径")
    @TableField("school_logo")
    private String schoolLogo;

    @ApiModelProperty(value = "学校荣誉录")
    @TableField("school_honor")
    private String schoolHonor;

    @ApiModelProperty(value = "学校发展历程")
    @TableField("school_process")
    private String schoolProcess;

    @ApiModelProperty(value = "学校简介")
    @TableField("school_brief")
    private String schoolBrief;

    @ApiModelProperty(value = "学校位置/来校路线")
    @TableField("school_address")
    private String schoolAddress;

    @ApiModelProperty(value = "学校代码")
    @TableField("school_code")
    @SchoolCodeAnno
    private String schoolCode;


}
