package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 班主任课后报名学期分组对象。
 *
 * @author wangyouhuai
 * @date 2026-06-14
 */
@Data
@ApiModel("班主任课后报名学期分组对象")
public class AsHeadteacherEnrollmentTermGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("本学期报名/待分配学生列表")
    private List<AsHeadteacherEnrollmentStudentRowVO> studentList = new ArrayList<>();
}
