package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.domain.base.khfw.dto.ExamQuestionItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author Administrator
 * @Date 2024/10/12 11:23
 */
@Setter
@Getter
@ToString
public class ExamQuestionAddAo {

    @ApiModelProperty("id主键  编辑必传")
    private Long id;
    
    @ApiModelProperty(value = "试卷名称",required = true)
    @NotBlank(message = "试卷名称不能为空")
    @ChineseTitleAnno
    private String title;

    @ApiModelProperty(value = "试卷描述")
    private String introduce;

    @ApiModelProperty(value = "适用角色JSON（多个） 1-家长 2-教师 3-管理员",required = true)
    @NotBlank(message = "适用角色JSON不能为空")
    private String applicableRoleJson;

    @ApiModelProperty(value = "评价指标体系ID",required = true)
    @NotNull(message = "评价指标体系ID不能为空")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty(value = "题目数量",required = true)
    @NotNull(message = "题目数量不能为空")
    private Integer questNum;

    @ApiModelProperty(value = "试题库列表集合",required = true)
    @NotEmpty(message = "试题库列表集合不能为空")
    List<ExamQuestionItemDto> items;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;
}
