package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务教师档案实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_teacher")
public class AsTeacherEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 教师工号
     */
    private String teacherNo;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 性别：0男 1女
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 学段,1：小学 2：初中 3：高中
     */
    private Integer schoolStage;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 任职状态：1在职 2离职
     */
    private Integer employeeStatus;

    /**
     * 审核状态：0待注册 1已通过 2已驳回 3待审核
     */
    private Integer registerStatus;

    /**
     * 是否班主任(展示冗余，实际以绑定表为准)：0否 1是
     */
    private Integer isHeadTeacher;

    // 入职年份
    private Long registYear;

    @ApiModelProperty("头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("审核意见")
    private String auditRemark;
}
