package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学科视图对象
 *
 * @author system
 * @date 2026-08-26
 */
@Data
@ApiModel("学科视图对象")
public class AsSubjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学科ID")
    private Long id;

    @ApiModelProperty("教师ID（批量查询时用于分组）")
    private Long teacherId;

    @ApiModelProperty("学科编码")
    private String subjectCode;

    @ApiModelProperty("学科名称")
    private String subjectName;

    @ApiModelProperty("学科描述")
    private String subjectDesc;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("是否启用 0-否 1-是")
    private Integer enabled;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createUser;
}
