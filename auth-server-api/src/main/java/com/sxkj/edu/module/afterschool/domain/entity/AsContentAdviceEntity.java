package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.base.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 意见反馈实体
 *
 * @author system
 * @date 2026-06-20
 */
@Data
@TableName("t_content_advice")
public class AsContentAdviceEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 来源：1-教师端、2-家长端、3-班主任
     */
    private Integer sourceType;

    /**
     * 意见内容
     */
    private String adviceName;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 反馈用户ID
     */
    private Long userId;

    /**
     * 分类，来源字典表
     */
    private Long categoryId;

    /**
     * 反馈人
     */
    private String author;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 附件地址,多个用,拼接
     */
    private String fileUrl;

    /**
     * 状态,1-已处理，0-未处理
     */
    private Integer status;

    /**
     * 处理意见
     */
    private String checkContent;
}
