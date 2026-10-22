package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生画像-分页查询参数")
public class StudentProfilePageAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("学生姓名（模糊查询）")
    private String studentName;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;
}
