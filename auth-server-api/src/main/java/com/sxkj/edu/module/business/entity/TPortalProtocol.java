package com.sxkj.edu.module.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 基础管理-协议管理
 * @Author yanghui
 * @Date 2025年09月15日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_portal_protocol")
public class TPortalProtocol extends BaseModel {

    private String protocolName; // 协议名称

    private String protocolCode; // 协议编号

    private Integer protocolType; // 协议类型 1-用户协议 2-隐私政策

    private String versionNum; // 版本号

    private Integer isEnable; // 是否启用 0-否 1-是

    private String useCases; // 使用场景（多个用,拼接） 1-用户注册 2-捐赠模块 3-企业赞助 4-社群管理 5-企业认证

    private String protocolContent; // 协议内容
}
