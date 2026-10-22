package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 课后类别视图对象
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@ApiModel("课后类别视图对象")
public class AsCourseCategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类别ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("父类别ID")
    private Long parentId;

    @ApiModelProperty("类别名称")
    private String categoryName;

    @ApiModelProperty("类别编码")
    private String categoryCode;

    @ApiModelProperty("状态(1启用 0停用)")
    private Integer enabledStatus;

    @ApiModelProperty("排序号")
    private Integer sortNo;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("子类别列表")
    private List<AsCourseCategoryVO> children = new ArrayList<>();
}
