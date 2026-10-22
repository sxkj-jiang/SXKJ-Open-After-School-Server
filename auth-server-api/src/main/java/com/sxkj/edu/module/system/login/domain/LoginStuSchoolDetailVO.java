package com.sxkj.edu.module.system.login.domain;

import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录返回校友学校DTO
 *
 * @author jiangjj
 * @date 2017年12月21日上午09:06:31
 */
@Data
public class LoginStuSchoolDetailVO {

    @ApiModelProperty("学校id")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("学校代码")
    private String schoolCode;

    @ApiModelProperty("学校封面信息")
    private FileVO schoolFileVo;


}
