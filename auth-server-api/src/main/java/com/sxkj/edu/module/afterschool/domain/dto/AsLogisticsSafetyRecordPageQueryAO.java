package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 后勤安全检查记录分页查询参数
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("后勤安全检查记录分页查询参数")
public class AsLogisticsSafetyRecordPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("课程班名称（模糊匹配）")
    private String courseClassName;

    @ApiModelProperty("检查日期-起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date checkDateFrom;

    @ApiModelProperty("检查日期-止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date checkDateTo;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("上课地点（模糊匹配）")
    private String placeName;

    @ApiModelProperty("提交教师ID")
    private Long teacherId;

    @ApiModelProperty("记录ID集合")
    private List<Long> ids;

    @ApiModelProperty("创建人id")
    private Long userId;

    @ApiModelProperty("记录状态：0未记录 1已记录")
    private Integer recordStatus;

    @ApiModelProperty("学期ID")
    private Long termId;
}
