package com.sxkj.edu.domain.ao;

import com.sxkj.edu.domain.dto.SchoolManageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SchoolManageDeleteAO对象", description = "学校信息表")
public class SchoolManageDeleteAO {
    private Long id;

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "学校类型")
    private String schoolType;

    @ApiModelProperty(value = "管理员ID")
    private Long managerId;

    @ApiModelProperty(value = "管理员姓名")
    private String managerName;

    @ApiModelProperty(value = "管理员手机号")
    private String managerPhone;

    @ApiModelProperty(value = "学校logo图片路径")
    private String schoolLogo;

    @ApiModelProperty(value = "学校荣誉录")
    private String schoolHonor;

    @ApiModelProperty(value = "学校发展历程")
    private String schoolProcess;

    @ApiModelProperty(value = "学校简介")
    private String schoolBrief;

    @ApiModelProperty(value = "学校位置/来校路线")
    private String schoolAddress;

    public static SchoolManageDTO convert(SchoolManageDeleteAO ao) {
        if (ao == null) return null;
        SchoolManageDTO dto = new SchoolManageDTO();
        dto.setSchoolName(ao.getSchoolName());
        dto.setSchoolType(ao.getSchoolType());
        dto.setManagerId(ao.getManagerId());
        dto.setManagerName(ao.getManagerName());
        dto.setManagerPhone(ao.getManagerPhone());
        dto.setSchoolLogo(ao.getSchoolLogo());
        dto.setSchoolHonor(ao.getSchoolHonor());
        dto.setSchoolProcess(ao.getSchoolProcess());
        dto.setSchoolBrief(ao.getSchoolBrief());
        dto.setSchoolAddress(ao.getSchoolAddress());
        return dto;
    }

}