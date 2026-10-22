package com.sxkj.edu.common.exception;

/**
 * @ClassName ParamCheckException
 * @Description
 * @Author Von
 * @Date2019/12/17 17:48
 * @Version 1.0
 **/
public class ParamCheckException extends BaseBusinessException {
    public ParamCheckException(String message) {
        super(message);
    }

    public ParamCheckException(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
