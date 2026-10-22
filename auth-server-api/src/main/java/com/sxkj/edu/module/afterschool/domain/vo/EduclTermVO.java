package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 学期视图对象
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@ApiModel("学期视图对象")
public class EduclTermVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学期编码")
    private String termCode;

    @ApiModelProperty("学年")
    private String schoolYear;

    @ApiModelProperty("学期类型(1-上学期 2-下学期)")
    private Integer termType;

    @ApiModelProperty("开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty("结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty("校历事件列表")
    private List<EduclCalendarEventVO> calendarEventList;


    @ApiModelProperty("总周数")
    private Integer totalWeeks;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("1启用 0停用")
    private Integer termStatus;
}
