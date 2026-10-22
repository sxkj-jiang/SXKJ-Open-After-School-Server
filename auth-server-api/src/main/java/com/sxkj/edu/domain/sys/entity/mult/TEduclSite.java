package com.sxkj.edu.domain.sys.entity.mult;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_educl_site")
@ApiModel(value = "TEduclSite对象", description = "教务-院系管理表")
public class TEduclSite extends BaseModel {

    @ApiModelProperty(value = "院系/站点名称")
    private String siteName;

    @ApiModelProperty(value = "院系代码")
    private String siteCode;

    @ApiModelProperty(value = "负责人姓名")
    private String directorName;

    @ApiModelProperty(value = "负责人电话")
    private String directorPhone;

    @ApiModelProperty(value = "院系介绍")
    private String siteIntroduce;

    @ApiModelProperty(value = "所属学校id")
    private Long schoolId;

}
