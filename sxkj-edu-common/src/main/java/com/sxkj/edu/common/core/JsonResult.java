package com.sxkj.edu.common.core;

import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseErrorCodeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 统一的Spring mvc响应结果封装对象
 *
 */
@Slf4j
@Data
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求成功
     */
    private static final boolean REQUEST_SUCCESS = true;

    /**
     * 请求失败
     */
    private static final boolean REQUEST_FAIL = false;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 错误提示语
     */
    private String msg;


    public JsonResult() {
    }

    public JsonResult(Boolean success, T data, String code, String msg) {
        this.success = success;
        this.data = data;
        this.code = new Integer(code);
        this.msg = msg;
    }

    public JsonResult(Boolean success, T data, String code, String msg,String errorCode) {
        this.success = success;
        this.data = data;
        this.code = new Integer(code);
        this.errorCode = new Integer(errorCode);
        this.msg = msg;
    }

    /**
     * 成功，不用返回数据
     *
     * @return
     */
    public static <T> JsonResult<T> buildSuccess() {
        return new JsonResult<>(REQUEST_SUCCESS, null, ResultCode.OK.code(), null);
    }

    /**
     * 成功，但是查无数据
     *
     * @return
     */
    public static <T> JsonResult<T> buildSuccess(String message) {
        return new JsonResult<>(REQUEST_SUCCESS, null, ResultCode.OK.code(), message);
    }

    /**
     * 成功，但是查无数据
     *
     * @return
     */
    public static <T> JsonResult<T> buildDataSuccess(T data) {
        return new JsonResult<>(REQUEST_SUCCESS, data, ResultCode.OK.code(), "success");
    }

    /**
     * 成功，返回数据
     *
     * @param data
     * @return
     */
    public static <T> JsonResult<T> buildSuccess(T data) {
        return new JsonResult<>(REQUEST_SUCCESS, data, ResultCode.OK.code(), null);
    }

    /**
     * 成功，返回数据+提示
     *
     * @param data
     * @return
     */
    public static <T> JsonResult<T> buildSuccess(T data,String message) {
        return new JsonResult<>(REQUEST_SUCCESS, data, ResultCode.OK.code(), message);
    }

    /**
     * 失败，固定状态码
     *
     * @param errorMsg
     * @return
     */
    public static <T> JsonResult<T> buildError(String errorMsg) {
        return new JsonResult<>(REQUEST_FAIL, null, ResultCode.PARA_ERROR.code(), errorMsg);
    }

    /**
     * 失败，并且返回操作数据
     *
     * @param errorMsg
     * @return
     */
    public static <T> JsonResult<T> buildError(T data,String resultCode, String errorMsg) {
        return new JsonResult<>(REQUEST_FAIL, data, resultCode, errorMsg);
    }

    /**
     * 失败，自定义错误码和信息
     *
     * @param resultCode 错误码
     * @param errorMsg  错误提示
     * @return
     */
    public static <T> JsonResult<T> buildError(String resultCode, String errorMsg) {
        return new JsonResult<>(REQUEST_FAIL, null, resultCode, errorMsg);
    }

    /**
     * 失败，枚举类定义错误码和信息
     *
     * @param baseErrorCodeEnum
     * @return
     */
    public static <T> JsonResult<T> buildError(BaseErrorCodeEnum baseErrorCodeEnum) {
        return new JsonResult<>(REQUEST_FAIL, null, baseErrorCodeEnum.getErrorCode(), baseErrorCodeEnum.getErrorMsg());
    }

}
