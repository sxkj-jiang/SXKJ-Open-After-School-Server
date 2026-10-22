package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareTypeVO {

    @ApiModelProperty("0公共，1私密，2好友")
    private Integer shareType;
}
