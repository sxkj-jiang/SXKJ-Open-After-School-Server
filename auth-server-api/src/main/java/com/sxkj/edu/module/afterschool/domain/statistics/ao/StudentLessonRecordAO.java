package com.sxkj.edu.module.afterschool.domain.statistics.ao;

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
 * 学生课时详情-详细课时记录分页查询参数
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生课时详情-详细课时记录分页查询参数")
public class StudentLessonRecordAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID", required = true)
    private Long studentId;

    @ApiModelProperty(value = "学生稳定身份ID",required = true)
    @NotNull(message = "学生稳定身份ID不能为空")
    private Long studentIdentityId;

    @ApiModelProperty(value = "学习周期ID", required = true)
    @NotNull(message = "学习周期ID不能为空")
    private Long termId;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("上课日期-开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDateStart;

    @ApiModelProperty("上课日期-结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDateEnd;

    @ApiModelProperty("出勤状态（1-出勤 2-迟到 3-早退 4-缺勤 5-请假）")
    private Integer attendanceStatus;
}
