package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 学生去向预警-管理端分页查询请求参数
 *
 * @author yh
 * @date 2026-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生去向预警-管理端分页查询请求参数")
public class AsWarningAdminPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "年级ID")
    private Long gradeId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生去向类型")
    private String destinationName;

    @ApiModelProperty("考勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty("预警状态：1正常 2预警")
    private Integer warningStatus;

    @ApiModelProperty("学生ID集合（勾选导出）")
    private List<Long> ids;

    @ApiModelProperty("学期ID，未传时默认使用学校默认学期")
    private Long termId;
}
