package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课表单元格数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCellVO {

    @ApiModelProperty(value = "课程班名称")
    private String courseClassName;

    @ApiModelProperty(value = "教师名称")
    private String teacherName;
}
