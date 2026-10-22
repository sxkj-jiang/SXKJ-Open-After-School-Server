package com.sxkj.edu.domain.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class UpdatePersonDTO {
    
    @ApiModelProperty("人员ID，取值为创建人员接口中的PersonId")
    @NotBlank(message = "人员id，不能为空")
    private  String personId;
    
    @ApiModelProperty("需要修改的人员名称")
    private String personName;
    
    @ApiModelProperty("需要修改的人员性别，1代表男性，2代表女性")
    private Integer gender;
}
