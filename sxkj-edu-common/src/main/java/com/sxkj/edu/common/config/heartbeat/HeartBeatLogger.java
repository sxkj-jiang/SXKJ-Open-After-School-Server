package com.sxkj.edu.common.config.heartbeat;


public interface HeartBeatLogger {

    void error(String string);

    void error(String string, Throwable e);

    void info(String string);
}
