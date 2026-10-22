package com.sxkj.edu.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *@Description : 基础异常类
 *@Author : JIANGJJ
 *@Date : 2022-12-27 11:27
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class BaseBusinessException extends RuntimeException {

    protected Integer DEFAULT_CODE = 500;

    /**
     * 异常码
     */
    protected Integer errorCode;

    /**
     * 异常信息
     */
    protected String errorMsg;

    public BaseBusinessException(Throwable cause) {
        super(cause);
    }

    public BaseBusinessException(Integer errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BaseBusinessException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = message;
    }

    public BaseBusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = Integer.parseInt(errorCode);
        this.errorMsg = message;
    }

    public BaseBusinessException(String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = DEFAULT_CODE;
    }

    public BaseBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = DEFAULT_CODE;
        this.errorMsg = message;
    }


}
