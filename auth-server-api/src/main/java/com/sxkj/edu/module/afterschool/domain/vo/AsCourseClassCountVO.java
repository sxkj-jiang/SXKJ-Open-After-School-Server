package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程班数量统计VO
 *
 * @author wangyouhuai
 * @date 2026-06-08
 */
@Data
@ApiModel("课程班数量统计")
public class AsCourseClassCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全部数量")
    private Long allCount = 0L;

    @ApiModelProperty("公开选课数量")
    private Long publicEnrollCount = 0L;

    @ApiModelProperty("邀请选课数量")
    private Long inviteEnrollCount = 0L;

    @ApiModelProperty("行政班安排数量")
    private Long adminArrangeCount = 0L;
}
