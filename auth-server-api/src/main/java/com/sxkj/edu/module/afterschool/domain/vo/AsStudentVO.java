package com.sxkj.edu.module.afterschool.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学生信息VO - 用于返回学生详情
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsStudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID")
    private Long id;

    @ApiModelProperty("学生跨学期稳定身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("学生监护人ID")
    private Long guardianId;

    @ApiModelProperty("学校id")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("性别：0-男，1-女")
    private Integer gender;

    @ApiModelProperty("出生日期")
    private String birthday;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("家长姓名")
    private String parentName;

    @ApiModelProperty("家长关系")
    private String relationName;

    @ApiModelProperty("家长手机号")
    private String parentPhone;

    @ApiModelProperty("家庭住址")
    private String address;

    @ApiModelProperty("头像id")
    private Long avatarFileId;

    @ApiModelProperty("头像")
    private String imgUrl;

    @ApiModelProperty("备注")
    private String remark;


    /**
     * 学籍状态：1在籍，2毕业，3休学，4转学
     */
    @ApiModelProperty("学籍状态：1在籍 2毕业 3休学 4转学")
    private Integer schoolRollStatus;

    /**
     * 注册状态：0待注册 1已注册
     */
    @ApiModelProperty("审核状态：0待注册 1已通过 2已驳回 3待审核")
    private Integer registerStatus;

    @ApiModelProperty("家长用户id")
    private Long parentId;

    @ApiModelProperty("入学年份")
    private Integer enrollmentYear;

    @ApiModelProperty("毕业年份")
    private Integer graduationYear;
}
