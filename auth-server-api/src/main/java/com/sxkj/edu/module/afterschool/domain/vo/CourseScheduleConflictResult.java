package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程排课冲突校验结果。
 */
@Data
@ApiModel("课程排课冲突校验结果")
public class CourseScheduleConflictResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否存在冲突")
    private boolean hasConflict;

    @ApiModelProperty("冲突明细")
    private List<CourseScheduleConflictDetail> conflicts = new ArrayList<>();

    public static CourseScheduleConflictResult ok() {
        CourseScheduleConflictResult result = new CourseScheduleConflictResult();
        result.setHasConflict(false);
        return result;
    }

    /**
     * 合并多条冲突提示，供保存接口直接返回。
     */
    public String firstMessage() {
        if (conflicts == null || conflicts.isEmpty()) {
            return null;
        }
        return conflicts.stream()
                .map(CourseScheduleConflictDetail::getMessage)
                .distinct()
                .collect(Collectors.joining("；"));
    }
}
