package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 学期分页查询参数
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学期分页查询参数")
public class EduclTermQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学期名称(模糊)")
    private String termName;

    @ApiModelProperty("学期类型 1第一学期 2第二学期 3其他")
    private Integer termType;

    @ApiModelProperty("是否查询全部学期（含已结束学期），默认true")
    private Boolean isAll = true;

    @ApiModelProperty("是否查询历史学期（含已开始学期）")
    private Boolean isHistory;

}
