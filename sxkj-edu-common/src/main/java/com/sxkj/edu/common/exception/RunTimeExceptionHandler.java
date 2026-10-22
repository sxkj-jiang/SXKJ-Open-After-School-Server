package com.sxkj.edu.common.exception;

import com.sxkj.edu.common.core.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 *@Description : 全局异常处理展示
 *@Author : JIANGJJ
 *@Date : 2023-3-28 11:33
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Slf4j
@RestControllerAdvice
public class RunTimeExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseBusinessException.class)
    public JsonResult handleRRException(BaseBusinessException e) {
        log.error("异常统一管理处打印异常信息, errorCode ={}, errorMessage = {}：", e.getErrorCode(), e.getMessage());
        if (e.getErrorCode() == null) {
            return JsonResult.buildError(e.getErrorMsg());
        }
        return JsonResult.buildError(e.getErrorCode().toString(), e.getErrorMsg());
    }

    /**
     * 参数校验异常统一打印
     *
     * @param e ConstraintViolationException 异常
     * @return ResponseData
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResult handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().iterator().next().getMessage();
        log.error("grob RunTimeExceptionHandler 检验异常打印：{}", message);
        return JsonResult.buildError("403", message);
    }

    /**
     * 参数校验异常统一打印
     * <p> 针对 RequestBody 内的参数 </p>
     *
     * @param exception MethodArgumentNotValidException 异常
     * @return ResponseData
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        assert fieldError != null;
        log.error("grob RunTimeExceptionHandler 检验异常打印：{}", fieldError.getField() + ":" + fieldError.getDefaultMessage());
        return JsonResult.buildError("403", fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public JsonResult handleException(Exception e) {
        // 控制台打印错误信息
        e.printStackTrace();
        return JsonResult.buildError(e.getMessage());
    }

}
