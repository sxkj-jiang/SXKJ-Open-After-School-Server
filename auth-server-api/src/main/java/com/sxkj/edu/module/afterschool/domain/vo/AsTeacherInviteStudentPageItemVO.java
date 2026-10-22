package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邀请选课可选学生分页查询（优化版）SQL JOIN 结果。
 * <p>继承 {@link AsStudentEntity} 以便直接传入 buildStudentSnapshots 复用快照逻辑，
 * 同时携带 JOIN 关联的年级名称和班级名称，避免再逐条查询。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("邀请选课可选学生分页项（优化版）")
public class AsTeacherInviteStudentPageItemVO extends AsStudentEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("年级名称（JOIN 关联）")
    private String gradeName;

    @ApiModelProperty("班级名称（JOIN 关联）")
    private String className;
}
