package com.sxkj.edu.common.config.heartbeat;



/**
 *@Description : 心跳处理
 *@Author : JIANGJJ
 *@Date : 2022-9-7 14:46
 *@Company : SXKJ
 *@Version : 1.0
 **/
public interface HeartBeatRecordCommendInterface {
    /**
     * 处理
     * @param heartBeatRecord
     */
    void handler(HeartBeatRecordDTO heartBeatRecord);

}
