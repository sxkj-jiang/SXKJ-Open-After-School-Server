package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 学生档案分页查询 AO
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生档案分页查询参数")
public class AsStudentQueryAO extends PageQuery {

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学生姓名（模糊）")
    private String studentName;

    @ApiModelProperty("学号（模糊）")
    private String studentNo;

    @ApiModelProperty("家长手机号")
    private String parentPhone;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("学籍状态：1在籍 2已毕业")
    private Integer schoolRollStatus;

    @ApiModelProperty("审核状态：0待注册 1已通过 2已驳回 3待审核")
    private Integer registerStatus;

    @ApiModelProperty("学期ID")
    private Long termId;
}
