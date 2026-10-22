package com.sxkj.edu.domain.base.khfw.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @Author Administrator
 * @Date 2024/10/11 22:39
 */
@Setter
@Getter
@ToString
public class ExamQuestionPageAo extends PageQuery {
    
    @ApiModelProperty("试卷名称/编码")
    private String titleOrExamNumber;
    
    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("角色 1-家长 2-教师 3-管理员")
    private Integer roleNum;




}
