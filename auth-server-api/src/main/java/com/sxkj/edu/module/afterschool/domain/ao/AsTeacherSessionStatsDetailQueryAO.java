package com.sxkj.edu.module.afterschool.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AsTeacherSessionStatsDetailQueryAO extends PageQuery {

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间", required = true)
    private Date sessionDateStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间", required = true)
    private Date sessionDateEnd;

    @ApiModelProperty(value = "教师ID")
    private Long teacherId;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;
}
