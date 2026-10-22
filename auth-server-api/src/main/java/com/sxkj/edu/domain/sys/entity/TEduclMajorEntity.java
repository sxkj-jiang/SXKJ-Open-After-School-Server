package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *@Description : 教务-专业表
 *@Author : JIANGJJ
 *@Date : 2022-12-24 11:46
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_educl_major")
public class TEduclMajorEntity extends BaseModel {


    @ApiModelProperty(value = "专业名",required = true)
    @NotNull(message = "专业名不能为空")
    private String majorName;

    @ApiModelProperty(value = "6位的数字专业编号，如410202",required = true)
    @Size(min = 6,max = 6)
    @NotBlank(message = "专业编号不能为空")
    private String majorCode;

    @ApiModelProperty(value = "专业说明",required = true)
    @NotBlank(message = "专业说明不能为空")
    private String majorDesc;

    @ApiModelProperty(value = "专业库表主键",required = true)
    @NotNull(message = "专业库表主键不能为空")
    private Long libraryId;

    @ApiModelProperty(value = "院系表主键",required = true)
    @NotNull(message = "院系表主键不能为空")
    private Long siteId;

   

    @ApiModelProperty("学制")
    private String educlSystem;

}
