package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseSchedulePageQueryAO extends PageQuery {

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "课后服务名称")
    private String serviceName;

    @ApiModelProperty(value = "学期ID")
    private Long termId;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}
