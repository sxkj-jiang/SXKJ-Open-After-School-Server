package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学生课表按周分组VO
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("学生学号检索")
public class AsQueryStudentNoByGradeClassVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String studentName;

}
