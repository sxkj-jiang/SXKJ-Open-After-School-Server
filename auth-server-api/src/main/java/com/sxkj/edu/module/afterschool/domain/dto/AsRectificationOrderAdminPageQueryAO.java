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

/**
 * 管理后台-问题整改工单多条件分页查询参数
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("管理后台-问题整改工单多条件分页查询参数")
public class AsRectificationOrderAdminPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "所属学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty("整改状态：0待整改 1整改中 2已整改 3已完成 4已逾期")
    private Integer rectifyStatus;

    @ApiModelProperty("课程班名称（模糊查询）")
    private String courseClassName;

    @ApiModelProperty("提交人（用户id）")
    private Long createUser;

    @ApiModelProperty("处理人（教师id）")
    private Long responsibleTeacherId;

    @ApiModelProperty("截止时间-起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadlineTimeFrom;

    @ApiModelProperty("截止时间-止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadlineTimeTo;

    @ApiModelProperty("学期ID")
    private Long termId;
}
