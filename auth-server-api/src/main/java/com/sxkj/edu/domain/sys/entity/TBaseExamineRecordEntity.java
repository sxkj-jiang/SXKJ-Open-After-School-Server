package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * @Description : 公共审核记录表
 * @Author : JIANGJJ
 * @Date : 2022-12-24 11:46
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_base_examine_record")
public class TBaseExamineRecordEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("审核来源")
    private String examType;
    
    @ApiModelProperty("审核人")
    private String examUser;
    
    @ApiModelProperty("审核意见")
    private String examAdvice;

    // 审核状态:1-已审核，2-待审核，3-已拒绝 4-撤回
    @ApiModelProperty("审核状态:1-已审核，2-待审核，3-已拒绝,4-撤回")
    private Integer examStatus;
    
    @ApiModelProperty("被审核表主键")
    private Long originId;
    
    @ApiModelProperty("审核时间")
    private Date examDate;
    
    @ApiModelProperty("是否有效，1-有效，其他无效")
    private Integer isValid;
}
