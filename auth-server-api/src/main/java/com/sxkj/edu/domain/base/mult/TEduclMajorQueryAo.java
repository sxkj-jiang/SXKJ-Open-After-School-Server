package com.sxkj.edu.domain.base.mult;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TEduclMajorQueryAo extends PageQuery {

    @ApiModelProperty("名称/代码")
    private String queryName;

    @ApiModelProperty("专业库id")
    private Long libraryMajorId;

    @ApiModelProperty("院系id")
    private Long siteId;
}
