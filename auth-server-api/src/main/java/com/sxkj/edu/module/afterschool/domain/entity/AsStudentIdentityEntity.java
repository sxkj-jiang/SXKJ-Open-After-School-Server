package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生跨学期稳定身份实体。
 *
 * <p>稳定身份以学校为边界，不随学期、学号、年级和班级变化。</p>
 */
@Data
@TableName("t_as_student_identity")
public class AsStudentIdentityEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校。
     */
    private Long schoolId;

    /**
     * 一年级基准入学年份，用于跨学期计算学生应在年级。
     */
    private Integer enrollmentYear;

    /**
     * 当前规范学号。
     */
    private String studentNo;

    /**
     * 学生姓名。
     */
    private String studentName;

    /**
     * 性别：0男，1女。
     */
    private Integer gender;

    /**
     * 身份证号。
     */
    private String idCard;

    /**
     * 头像文件编号。
     */
    private Long avatarFileId;

}
