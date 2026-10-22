package com.sxkj.edu.module.afterschool.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 班级视图对象
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@ApiModel("班级视图对象")
public class EduclClassVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("班级代码")
    private String classCode;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班主任ID")
    private Long headTeacherId;

    @ApiModelProperty("班主任名称")
    private String headTeacherName;

    @ApiModelProperty("班主任用户ID")
    private Long headTeacherUserId;

    @ApiModelProperty("入学年份")
    private Integer enrollmentYear;

    @ApiModelProperty("毕业年份")
    private Integer graduationYear;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ExcelProperty("学期")
    private String termName;


}
