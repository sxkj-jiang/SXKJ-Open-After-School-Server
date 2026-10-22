package com.sxkj.edu.domain.sys.entity.mult;

/**
 * @Description: TOperateInstructions
 * @Author yanghui
 * @Date 2025年01月17日
 */

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作指引表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_operate_instructions")
@ApiModel(value="OperateInstructions对象", description="操作指引表")
public class TOperateInstructionsEntity extends BaseModel {

    @ApiModelProperty(value = "平台:1 教务端 2 学习平台-web 3 学习平台-移动")
    private Integer belongPlate;

    @ApiModelProperty(value = "用户类型：管理员-0,教师-1,学生-2,财务-3,4-院系领导,5-外部单位")
    private Integer roleType;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "文档内容")
    private String docContent;

    @ApiModelProperty(value = "说明")
    private String insDescribe;

    @ApiModelProperty(value = "附件信息")
    private String fileInfo;

    @ApiModelProperty(value = "状态 0-待发布 1-已发布")
    private Integer publishStatus;

}
