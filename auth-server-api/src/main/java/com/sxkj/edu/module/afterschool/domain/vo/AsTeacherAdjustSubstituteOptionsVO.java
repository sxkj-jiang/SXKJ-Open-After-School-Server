package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理端代课教师候选响应。
 */
@Data
@ApiModel("管理端代课教师候选响应")
public class AsTeacherAdjustSubstituteOptionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前查询条件最终确认的代课课次ID集合")
    private List<Long> approvedSessionIds = new ArrayList<>();

    @ApiModelProperty("可用代课教师列表")
    private List<SubstituteTeacherVO> substituteTeachers = new ArrayList<>();
}
