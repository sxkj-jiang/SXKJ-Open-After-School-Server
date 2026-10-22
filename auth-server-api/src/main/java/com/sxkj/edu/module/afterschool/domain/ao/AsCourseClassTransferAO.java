package com.sxkj.edu.module.afterschool.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 教师移交
 */
@Data
@ApiModel("教师移交")
public class AsCourseClassTransferAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("移交后的教师编号")
    private Long teacherId;

    @ApiModelProperty("课程班id")
    private Long courseClassId;
}
