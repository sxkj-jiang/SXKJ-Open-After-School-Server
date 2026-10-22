package com.sxkj.edu.common.config.heartbeat;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class HeartBeatConfig {

    /**
     * 延迟执行时间
     */
    private Long delayHandlerTime;

    /**
     * 间隔执行时间
     */
    private Long intervalTime;
}
