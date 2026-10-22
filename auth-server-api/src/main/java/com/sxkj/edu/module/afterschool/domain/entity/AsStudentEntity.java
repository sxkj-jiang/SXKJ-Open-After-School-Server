package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务学生档案实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_student")
public class AsStudentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 跨学期稳定学生身份编号。
     */
    private Long studentIdentityId;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 性别：0男 1女
     */
    private Integer gender;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 头像文件ID
     */
    private Long avatarFileId;

    /**
     * 学期id
     */
    private Long termId;

    /**
     * 所属年级，对应 t_educl_grade.id
     */
    private Long gradeId;

    /**
     * 所属行政班，对应 t_educl_class.id
     */
    private Long classId;

    /**
     * 学籍状态：1在籍，2不在籍
     */
    private Integer schoolRollStatus;

    /**
     * 审核状态：0待注册 1已通过 2已驳回 3待审核
     */
    private Integer registerStatus;

    /**
     * 学生账号，当前原型主要由家长绑定
     */
    private Long userId;

    @ApiModelProperty("审核意见")
    private String auditRemark;

    /**
     * 毕业年份
     */
    private Integer graduationYear;

}
