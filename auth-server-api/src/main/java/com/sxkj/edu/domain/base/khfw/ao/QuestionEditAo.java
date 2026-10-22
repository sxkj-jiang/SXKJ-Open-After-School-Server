package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.domain.base.khfw.dto.QuestionItemObjectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 *@Description : 考试-题库管理
 *@Author : JIANGJJ
 *@Date : 2025-6-16 16:52
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class QuestionEditAo implements Serializable {

    @ApiModelProperty(value = "修改时，id必传")
    private Long id;

    @ApiModelProperty(value = "题型 1-单选题 2-多选题 3-文本题", required = true)
    @NotNull(message = "题型不能为空")
    private Integer questionType;
    
    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @ApiModelProperty(value = "题目选项集合",required = true)
    private List<QuestionItemObjectDto> items;

    @ApiModelProperty(value = "评价指标体系ID",required = true)
    @NotNull(message = "评价指标体系ID不能为空")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty(value = "指标配置JSON(单个)",required = true)
    @NotBlank(message = "指标配置JSON不能为空")
    private String indicatorConfig;

    @ApiModelProperty(value = "是否必填 0-否 1-是",required = true)
    @NotNull(message = "是否必填不能为空")
    private Integer isRequired;

    @ApiModelProperty(value = "适用角色JSON（多个） 1-家长 2-教师 3-管理员",required = true)
    @NotBlank(message = "适用角色JSON不能为空")
    private String applicableRoleJson;

    @ApiModelProperty(value = "关联习题ID，更新传值",required = true)
    private Long textContentId;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;
}
