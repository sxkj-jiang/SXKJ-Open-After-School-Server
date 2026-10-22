package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("任课老师端课程评价列表查询AO")
public class TeacherCourseEvaluationPageAO extends PageParamDTO {

    @ApiModelProperty("Tab类型：1-学生评价 2-管理员评价")
    private Integer tabType;

    @ApiModelProperty("课程名称（模糊搜索）")
    private String courseName;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;
}
