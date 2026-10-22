package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TEduclMajorNameCodeVo {

    @ApiModelProperty("专业Id")
    private Long id;

    @ApiModelProperty("专业名称")
    private String majorName;

    @ApiModelProperty("专业代码")
    private String majorCode;
}
