  package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("年级视图对象")
public class EduclGradeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("年级ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("年级编码")
    private String gradeCode;
    @ApiModelProperty("阶段 1-小学/2-初中/3-高中")
    private Integer schoolStage;
    @ApiModelProperty("年级名称")
    private String gradeName;
    @ApiModelProperty("年级主任用户ID")
    private Long directorUserId;
    @ApiModelProperty("年级主任姓名")
    private String directorName;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("排序号")
    private Integer sortNo;
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
