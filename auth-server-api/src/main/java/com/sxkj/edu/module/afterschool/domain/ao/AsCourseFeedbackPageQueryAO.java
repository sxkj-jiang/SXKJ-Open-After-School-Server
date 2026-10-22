package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课堂反馈分页查询AO
 *
 * @author system
 * @date 2026-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("课堂反馈分页查询参数")
public class AsCourseFeedbackPageQueryAO extends PageQuery {

    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("课后服务名称（模糊查询）")
    private String serviceName;

    @ApiModelProperty("课程班名称（模糊查询）")
    private String courseClassName;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("上课地点（模糊查询）")
    private String placeName;

    @ApiModelProperty("学期ID")
    private Long termId;
}
