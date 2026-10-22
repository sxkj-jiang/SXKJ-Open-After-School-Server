package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 课程班批量导入-行级错误明细 VO。
 *
 * @author huangpu
 * @date 2026-07-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolBaseImportErrorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Excel 行号（含表头行）")
    private Integer rowNum;

    @ApiModelProperty(value = "出错字段中文名")
    private String field;

    @ApiModelProperty(value = "错误原因")
    private String reason;

    /**
     * 便捷构造：把"第N行: 字段 - 原因"快速组装为 VO。
     */
    public static SchoolBaseImportErrorVO of(Integer rowNum, String field, String reason) {
        return SchoolBaseImportErrorVO.builder()
                .rowNum(rowNum).field(field).reason(reason).build();
    }
}
