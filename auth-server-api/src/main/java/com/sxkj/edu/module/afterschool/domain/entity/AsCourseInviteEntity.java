package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程邀请链接/二维码批次实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_course_invite")
public class AsCourseInviteEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 课程班ID
     */
    private Long courseClassId;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请链接
     */
    private String inviteUrl;

    /**
     * 二维码图片文件ID，复用 t_base_file
     */
    private Long inviteQrFileId;

    /**
     * 确认邀请时选中的学生ID集合JSON
     */
    @TableField("invite_student_ids")
    private String inviteStudentIds;

    /**
     * 是否需要家长确认报名，冗余课程班配置：0否 1是
     */
    private Integer parentConfirmRequired;

    /**
     * 选中人数
     */
    private Integer selectedCount;

    /**
     * 邀请状态：1有效 2失效
     */
    private Integer inviteStatus;

    /**
     * 过期时间
     */
    private Date expireTime;
}
