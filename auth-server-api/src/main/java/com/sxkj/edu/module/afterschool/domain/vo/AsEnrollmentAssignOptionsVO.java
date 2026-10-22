package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分配课程候选结果
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("分配课程候选结果")
public class AsEnrollmentAssignOptionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前候选业务所属学期ID，跨服务候选时为空")
    private Long termId;

    @ApiModelProperty("当前候选业务所属学期名称，跨服务候选时为空")
    private String termName;

    @ApiModelProperty("课程类别")
    private List<CategoryOption> categories = new ArrayList<>();

    @ApiModelProperty("课程班")
    private List<AsAssignableCourseClassVO> courseClasses = new ArrayList<>();

    @ApiModelProperty("非课程去向选项")
    @JsonIgnore
    private List<DestinationOption> destinationOptions = new ArrayList<>();

    @Data
    public static class CategoryOption implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long categoryId;
        private Integer destinationType;
        private String categoryName;
    }

    @Data
    public static class DestinationOption implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("去向类型：2回家 3培训机构/校外机构 4其他")
        private Integer destinationType;

        @ApiModelProperty("去向名称")
        private String destinationName;
    }
}
