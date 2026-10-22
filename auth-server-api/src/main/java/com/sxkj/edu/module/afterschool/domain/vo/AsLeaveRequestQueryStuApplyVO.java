package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("请假申请视图对象")
public class AsLeaveRequestQueryStuApplyVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("学生姓名")
    private String studentName;
    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("年级")
    private String gradeName;

    @ApiModelProperty("班级")
    private String className;

    @ApiModelProperty("审核状态：0待注册 1已通过 2已驳回 3待审核")
    private Integer registerStatus;
}
