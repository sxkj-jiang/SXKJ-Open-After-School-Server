package com.sxkj.edu.domain.base.khfw.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AsStudentBindParentAO {

    @ApiModelProperty("学校id")
    private Long schoolId;

    @ApiModelProperty("学期id")
    private Long termId;

    @ApiModelProperty("年级ids")
    private List<Long> gradeIds;

    @ApiModelProperty("班级ids")
    private List<Long> classIds;
}
