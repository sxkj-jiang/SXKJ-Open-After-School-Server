package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAdjustSessionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 教师调课-可调课次及代课教师汇总
 *
 * @author wangyouhuai
 * @date 2026-06-14
 */
@Data
@ApiModel("教师调课-可调课次及代课教师汇总")
public class AsTeacherAdjustAvailableVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("可调课课次列表（每节课自动推荐一位代课教师）")
    private List<AsTeacherAdjustSessionDTO> sessions;

    @ApiModelProperty("整个调课周期所有课次都有空的代课教师列表")
    private List<SubstituteTeacherVO> substituteTeachers;
}
