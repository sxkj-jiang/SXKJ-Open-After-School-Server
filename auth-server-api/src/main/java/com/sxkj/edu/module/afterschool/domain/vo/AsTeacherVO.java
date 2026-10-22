package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 教师VO - 视图对象
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsTeacherVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教师ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("教师工号")
    private String teacherNo;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("性别：0男 1女")
    private Integer gender;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("学校阶段:1-小学/2-初中/3-高中")
    private String schoolStage;

    @ApiModelProperty("岗位名称")
    private String positionName;

    @ApiModelProperty("任职状态：1在职 2离职")
    private Integer employeeStatus;

    @ApiModelProperty("用户状态：1启用 0禁用（来源t_base_user.status）")
    private Integer status;

    @ApiModelProperty("审批状态：0待注册 1已通过 2已驳回 3待审核")
    private Integer registerStatus;

    @ApiModelProperty("是否班主任：0否 1是")
    private Integer isHeadTeacher;

    @ApiModelProperty("教师头像")
    private String imgUrl;

    @ApiModelProperty("头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("课程班名称集合")
    private String courseClassNames;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("自然班级名称")
    private String className;

    @ApiModelProperty("教学科目")
    private String subjectNames;

    @ApiModelProperty("教学科目信息集合")
    private List<AsSubjectVO> subjectList;

    @ApiModelProperty("任课班级（多个）")
    private List<Long> classIdList;

    @ApiModelProperty("任课班级名称字符串")
    private String classIdListStr;

}
