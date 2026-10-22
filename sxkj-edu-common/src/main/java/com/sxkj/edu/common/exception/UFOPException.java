package com.sxkj.edu.common.exception;

/**
 *@Description : 文件异常
 *@Author : jiangjj$
 *@Date : 20240914$ $
 *@Company : SXKJ
 *@Version : 1.0
 **/
public class UFOPException extends RuntimeException {
    public UFOPException(Throwable cause) {
        super("统一文件操作平台出现异常", cause);
    }

    public UFOPException(String message) {
        super(message);
    }

    public UFOPException(String message, Throwable cause) {
        super(message, cause);
    }
}
