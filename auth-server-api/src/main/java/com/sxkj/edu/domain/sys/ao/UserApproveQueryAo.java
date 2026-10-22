package com.sxkj.edu.domain.sys.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class UserApproveQueryAo {

    @ApiModelProperty(value = "用户id集合")
    @NotEmpty(message = "用户id不能为空")
    private List<Long> userIds;

}
