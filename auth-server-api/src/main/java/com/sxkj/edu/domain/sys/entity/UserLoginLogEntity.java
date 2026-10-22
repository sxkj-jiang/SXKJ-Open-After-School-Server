package com.sxkj.edu.domain.sys.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.*;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_login_log")
public class UserLoginLogEntity extends BaseEntity {

    /**
     * 员工id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户ip
     */
    private String remoteIp;

    /**
     * 用户端口
     */
    private Integer remotePort;

    /**
     * 浏览器
     */
    private String remoteBrowser;

    /**
     * 操作系统
     */
    private String remoteOs;

    /**
     * 登录状态
     */
    private Integer loginStatus;

    /**
     * 访问端 1-教务端 2-学习平台 3-移动端
     */
    private String accessTerminal;

    /**
     * 登录类型 1-登录 2-登出
     */
    private Integer loginType;



}
