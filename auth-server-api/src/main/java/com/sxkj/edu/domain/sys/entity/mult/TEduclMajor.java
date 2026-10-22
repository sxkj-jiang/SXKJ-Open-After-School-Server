package com.sxkj.edu.domain.sys.entity.mult;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_educl_major")
@ApiModel(value = "TEduclMajor对象", description = "教务-专业管理表")
public class TEduclMajor extends BaseModel {

    @ApiModelProperty(value = "专业名")
    private String majorName;

    @ApiModelProperty(value = "6位的数字专业编号，如410202")
    @Size(min = 4, max = 6)
    private String majorCode;

    @ApiModelProperty(value = "专业类")
    private String majorType;

    @ApiModelProperty(value = "专业介绍")
    private String majorIntroduce;

    @ApiModelProperty(value = "院系表主键")
    private Long siteId;

    @ApiModelProperty(value = "学历层次：1-中专 2-大专(专科) 3-本科 4-硕士研究生 5-博士研究生")
    private Integer degree;

}
