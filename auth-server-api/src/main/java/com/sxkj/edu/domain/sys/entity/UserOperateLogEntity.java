package com.sxkj.edu.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_operate_log")
public class UserOperateLogEntity extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 请求结果 0失败 1成功
     */
    private Integer result;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 访问端 1-校友管理端 2-校友小程序端
     */
    private Integer accessTerminal;



}
