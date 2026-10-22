package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExamQuestionRecordPageAo extends PageQuery {

    @NotNull(message = "试卷id不能为空")
    @ApiModelProperty(value = "试卷id",required = true)
    private Long examLibraryId;

    @ApiModelProperty(value = "用户id或姓名")
    private String queryInfo;

}
