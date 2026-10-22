package com.sxkj.edu.domain.sys.entity.mult;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_educl_class")
@ApiModel(value = "TEduclClass对象", description = "教务-班级管理表")
public class TEduclClass extends BaseModel {

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "年级表主键")
    private Long gradeId;

    @ApiModelProperty(value = "学校主键")
    private Long schoolId;

    @ApiModelProperty(value = "班级代码")
    private String classCode;

    @ApiModelProperty(value = "班级人数")
    private String classCount;

    @ApiModelProperty(value = "班主任id")
    private Long headTeacherId;

    @ApiModelProperty(value = "入学年份")
    private Integer enrollmentYear;

    @ApiModelProperty(value = "毕业年份")
    private Integer graduationYear;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "学期表主键")
    private Long termId;
}
