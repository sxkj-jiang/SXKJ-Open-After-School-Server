package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 可代课教师信息
 *
 * @author wangyouhuai
 * @date 2026-06-14
 */
@Data
@ApiModel("可代课教师信息")
public class SubstituteTeacherVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;
}
