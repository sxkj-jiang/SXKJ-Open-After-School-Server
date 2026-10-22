package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 班主任端-学生调课-可调课学生分页查询参数
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("班主任-可调课学生分页查询参数")
public class AsHeadteacherStudentAdjustStudentPageAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索关键字：学生姓名或学号")
    private String keyword;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;
}
