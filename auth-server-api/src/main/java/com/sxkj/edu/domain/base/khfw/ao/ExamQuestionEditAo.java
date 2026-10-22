package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.domain.base.khfw.dto.ExamQuestionItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExamQuestionEditAo {

    @ApiModelProperty(value = "id主键  编辑必传",required = true)
    @NotNull(message = "id主键不能为空")
    private Long id;

    @ApiModelProperty(value = "试卷名称",required = true)
    @NotBlank(message = "试卷名称不能为空")
    @ChineseTitleAnno
    private String title;

    @ApiModelProperty(value = "试卷描述")
    private String introduce;

    @ApiModelProperty(value = "适用角色JSON（多个） 1-家长 2-教师 3-管理员")
    private String applicableRoleJson;

    @ApiModelProperty("评价指标体系ID")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty(value = "试题库列表集合",required = true)
    @NotEmpty(message = "试题库列表集合集合不能为空")
    List<ExamQuestionItemDto> items;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;
}
