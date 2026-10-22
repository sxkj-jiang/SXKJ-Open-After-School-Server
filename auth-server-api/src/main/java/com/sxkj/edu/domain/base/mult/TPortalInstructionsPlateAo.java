package com.sxkj.edu.domain.base.mult;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: TPortalInstructionsPlateAo
 * @Author yanghui
 * @Date 2024年11月27日
 */
@Data
public class TPortalInstructionsPlateAo extends PageQuery {

    @ApiModelProperty(value = "操作指南名称")
    private String operName;

    @ApiModelProperty(value = "操作指南类型,1-操作类，2-体验类，3-其他类")
    private Integer operType;

}
