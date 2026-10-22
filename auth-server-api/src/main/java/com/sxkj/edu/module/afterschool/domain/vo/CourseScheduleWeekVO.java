package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.module.afterschool.domain.ao.evaluation.CourseScheduleListTeachVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CourseScheduleWeekVO {

    @ApiModelProperty("星期几(1-7)")
    private Integer weekDay;

    @ApiModelProperty("课次列表")
    private List<CourseScheduleListTeachVO> courseVOList;
}
