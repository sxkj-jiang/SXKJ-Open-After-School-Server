package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("班主任-请假申请请假人查询")
public class AsLeaveRequestApplyStudentByLeaderAO implements Serializable {

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "自然班级ID", required = true)
    @NotNull(message = "自然班级ID不能为空")
    private Long classId;

    @ApiModelProperty(value = "学生姓名、学号查询")
    private String queryKey;

    @ApiModelProperty("学期ID，未传时默认使用学校默认学期")
    private Long termId;
}
