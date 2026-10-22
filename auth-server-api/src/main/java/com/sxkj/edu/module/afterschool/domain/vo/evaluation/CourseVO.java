package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseVO extends AsCourseClassEntity {
    @ApiModelProperty("上课地点")
    private String placeName;
}
