package com.sxkj.edu.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.validation.UserName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class DictItemVo {

    @ApiModelProperty("字典id,更新时必填")
    private Long id;

    @ApiModelProperty("字典父类编号")
    private Long parentId;

    @ApiModelProperty(value = "字典编码")
    private String typeCode;

    @ApiModelProperty("字典值")
    private String typeValue;

    @ApiModelProperty("字典名称")
    private String typeName;

    @ApiModelProperty("状态(1:正常;0:禁用)")
    private Integer status;

    @ApiModelProperty("排序值")
    private Integer sort;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("更新人")
    private Long updateUser;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("子类字典集合")
    private List<DictItemChildVo> childList;
}
