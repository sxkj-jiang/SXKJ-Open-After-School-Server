package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.module.afterschool.domain.dto.RectificationOrderItemAO;
import com.sxkj.edu.module.afterschool.domain.vo.TeacherCheckItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 巡课记录编辑对象（仅id必填，其他字段按需传入）
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("巡课记录编辑对象")
public class AsPatrolRecordUpdateAO {

    @NotNull(message = "记录ID不能为空")
    @ApiModelProperty(value = "记录ID", required = true)
    private Long id;

    @ApiModelProperty("问题整改单列表（有问题时传入，传入则全量替换）")
    @Valid
    private List<RectificationOrderItemAO> rectificationOrders;
}
