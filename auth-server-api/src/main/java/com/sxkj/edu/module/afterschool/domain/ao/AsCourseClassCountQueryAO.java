package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程班数量统计查询AO
 *
 * @author wangyouhuai
 * @date 2026-06-08
 */
@Data
public class AsCourseClassCountQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "学期ID")
    private Long termId;
}
