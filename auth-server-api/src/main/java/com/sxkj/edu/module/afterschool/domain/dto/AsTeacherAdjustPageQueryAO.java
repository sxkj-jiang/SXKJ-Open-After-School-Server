package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 教师调课分页查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师调课分页查询参数")
public class AsTeacherAdjustPageQueryAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("课程名称，模糊查询")
    private String courseName;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("教师ID，管理端匹配申请人/原教师/代课教师")
    private Long teacherId;

    @ApiModelProperty("申请状态：0待处理 1已同意 2已拒绝 3已撤回")
    private Integer requestStatus;

    @ApiModelProperty("申请开始时间")
    private Date applyStartTime;

    @ApiModelProperty("申请结束时间")
    private Date applyEndTime;

    @ApiModelProperty(value = "教师端视图：1-我发起 2-我收到",required = true)
    @NotNull(message = "查询类型：1我发起 2我相关,不能为空")
    private Integer viewScope;
}
