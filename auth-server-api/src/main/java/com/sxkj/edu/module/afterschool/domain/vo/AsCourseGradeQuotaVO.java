package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
public class AsCourseGradeQuotaVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long gradeId;
    private Integer capacity;
    @ApiModelProperty("该年级已占用人数，按学生去重，不按课次累加")
    private Integer enrolledCount;
    private Integer remainingCount;
}
