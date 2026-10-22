package com.sxkj.edu.domain.base.mult;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.common.validation.NumberAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: TPortalIndustryAo
 * @Author YZY
 * @Date 2024年07月08日
 */
@Data
public class TPortalIndustryAo{

    @ApiModelProperty(value = "主键id，更新时必填")
    private Long id;

    @ApiModelProperty(value = "行业名称", required = true)
    @NotBlank(message = "行业名称不能为空")
    @ChineseTitleAnno
    private String industryName;

    @ApiModelProperty(value = "6位数字行业代码", required = true)
    @NotNull(message = "行业代码不能为空")
    @NumberAnno
    @Length(min = 6,max = 6)
    private String code;

    @ApiModelProperty(value = "描述")
    private String describeNotes;

    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

}
