package com.sxkj.edu.domain.ao;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.common.validation.Mobile;
import com.sxkj.edu.common.validation.NumberAnno;
import com.sxkj.edu.domain.dto.SchoolManageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "SchoolManageUpdateAO对象", description = "学校信息表")
public class SchoolManageUpdateAO {

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "学校名称", required = true)
    @ChineseTitleAnno
    @NotBlank(message = "学校名称不能为空")
    private String schoolName;

    @ApiModelProperty(value = "学校代码，为4位数值，如4301-湖南大学", required = true)
    @NumberAnno
    @Length(min = 4,max = 4)
    @NotBlank(message = "学校代码不能为空")
    private String schoolCode;

    @ApiModelProperty(value = "学校类型:1-大学，2-职业学院，3-中学、4-小学", required = true)
    @NotBlank(message = "学校类型不能为空")
    private String schoolType;

    @ApiModelProperty(value = "管理员ID", required = true)
    private Long managerId;

    @ApiModelProperty(value = "管理员姓名", required = true)
    @ChineseTitleAnno
    private String managerName;

    @ApiModelProperty(value = "管理员联系方式", required = true)
    @Mobile
    private String managerPhone;

    @ApiModelProperty(value = "学校logo图片路径",required = true)
    @NotBlank(message = "学校logo图片路径不能为空")
    private String schoolLogo;

    @ApiModelProperty(value = "学校荣誉录")
    private String schoolHonor;

    @ApiModelProperty(value = "学校发展历程")
    private String schoolProcess;

    @ApiModelProperty(value = "学校简介",required = true)
    @NotBlank(message = "学校简介不能为空")
    private String schoolBrief;

    @ApiModelProperty(value = "学校位置/来校路线")
    private String schoolAddress;

    public static SchoolManageDTO convert(SchoolManageUpdateAO ao) {
        if (ao == null) return null;
        SchoolManageDTO dto = new SchoolManageDTO();
        BeanUtils.copyProperties(ao, dto);
        return dto;
    }

}