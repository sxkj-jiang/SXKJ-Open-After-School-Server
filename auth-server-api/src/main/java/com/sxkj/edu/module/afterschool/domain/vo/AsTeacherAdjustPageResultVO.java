package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.common.base.domain.PageResultDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教师调课分页结果，包含状态 Tab 数量。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师调课分页结果")
public class AsTeacherAdjustPageResultVO extends PageResultDTO<AsTeacherAdjustRequestVO> {

    @ApiModelProperty("状态数量")
    private AsTeacherAdjustStatusCountVO statusCount;
}
