package com.sxkj.edu.common.config.heartbeat;

import lombok.Data;

import java.util.Date;


/**
 *@Description : 心跳记录日志
 *@Author : JIANGJJ
 *@Date : 2022-9-7 14:46
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class HeartBeatRecordDTO {

    /**
     * 项目名字
     */
    private String projectPath;
    /**
     * 服务器ip
     */
    private String serverIp;
    /**
     * 进程号
     */
    private Integer processNo;
    /**
     * 进程开启时间
     */
    private Date processStartTime;
    /**
     * 心跳当前时间
     */
    private Date heartBeatTime;


}
