package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 课程班分页查询AO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AsCourseClassPageQueryAO extends PageQuery {

    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "报名方式：1公开报名 2邀请报名 3行政班安排", required = true)
    private Integer enrollType;

    @ApiModelProperty("课程班名称（模糊查询）")
    private String courseClassName;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("课程类别集合")
    private List<Long> categoryIdList;

    @ApiModelProperty("学期ID")
    private Long termId;
}
