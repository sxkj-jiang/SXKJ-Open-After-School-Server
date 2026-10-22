package com.sxkj.edu.domain.base.khfw.vo;

import com.sxkj.edu.domain.base.khfw.dto.QuestionItemObjectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author Administrator
 * @Date 2024/10/12 15:47
 */
@Data
public class QuestionItemVO {

    @ApiModelProperty("题目id主键")
    private Long id;

    @ApiModelProperty("试题编号")
    private String examNumber;

    @ApiModelProperty("题型 1-单选题 2-多选题 3-文本题")
    private Integer questionType;

    @ApiModelProperty("问题类型name")
    private String questionTypeName;

    @ApiModelProperty("题目")
    private String title;

    @ApiModelProperty("适用角色JSON")
    private String applicableRoleJson;

    @ApiModelProperty("适用角色名称")
    private String applicableRoleName;

    @ApiModelProperty("所属维度")
    private String dimension;

    @ApiModelProperty("排序")
    private Integer itemSort;

//    @ApiModelProperty("选项")
//    private List<QuestionItemObjectDto> items;

}
