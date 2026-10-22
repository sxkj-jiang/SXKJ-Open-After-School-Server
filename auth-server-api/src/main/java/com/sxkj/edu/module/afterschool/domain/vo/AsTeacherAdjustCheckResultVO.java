package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAdjustSessionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 代课教师可用性校验结果。
 */
@Data
@ApiModel("代课教师可用性校验结果")
public class AsTeacherAdjustCheckResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否可用")
    private Boolean available = true;

    @ApiModelProperty("冲突课次")
    private List<AsTeacherAdjustSessionDTO> conflicts = new ArrayList<>();
}
