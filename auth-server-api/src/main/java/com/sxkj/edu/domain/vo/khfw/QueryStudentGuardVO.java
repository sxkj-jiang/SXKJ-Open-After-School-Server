package com.sxkj.edu.domain.vo.khfw;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryStudentGuardVO {

    @ApiModelProperty("编号")
    private Long id;

    /**
     * 所属学校
     */
    @ApiModelProperty(value = "所属学校")
    private Long schoolId;

    /**
     * 监护人用户ID
     */
    @ApiModelProperty(value = "监护人用户ID")
    private Long guardianUserId;

    /**
     * 学生ID
     */
    @ApiModelProperty(value = "学生ID")
    private Long studentId;

    /**
     * 关系名称：妈妈/爸爸等
     */
    @ApiModelProperty(value = "关系名称：妈妈/爸爸等")
    private String relationName;

    /**
     * 家长姓名
     */
    @ApiModelProperty(value = "家长姓名")
    private String parentName;

    @ApiModelProperty(value = "家长姓名和关系")
    private String parentRelation;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "家长联系方式")
    private String phone;

    /**
     * 绑定状态：1有效 2解绑
     */
    @ApiModelProperty(value = "绑定状态：1有效 2解绑")
    private Integer bindStatus;


}
