package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 家长查询小孩考勤AO
 *
 * @author jiangjj
 * @date 2026-05-28
 */
@Data
public class AsParentAttendanceQueryAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty("学期ID，未传时使用学生快照所属学期")
    private Long termId;

    @ApiModelProperty(value = "查询类型：1日 2周 3月", required = true)
    @NotNull(message = "查询类型不能为空")
    private Integer queryType;

    @ApiModelProperty(value = "查询日期（格式：yyyy-MM-dd）", required = true)
    @NotNull(message = "查询日期不能为空")
    private String queryDate;

    @ApiModelProperty("周偏移量（查看其他周：0当前周，-1上一周，-2上上周，-3上上上周，1下一周等）")
    private Integer weekOffset = 0;

    @ApiModelProperty("月偏移量（查看其他月：0当前月，-1上一月，-2上上月，1下一月等）")
    private Integer monthOffset = 0;
}
