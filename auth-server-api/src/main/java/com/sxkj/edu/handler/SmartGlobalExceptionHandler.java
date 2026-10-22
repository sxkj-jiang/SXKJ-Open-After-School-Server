package com.sxkj.edu.handler;

import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.exception.BaseBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description : 全局异常拦截
 * @Author : JIANGJJ
 * @Date : 2022-9-6 10:59
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Slf4j
@ControllerAdvice
public class SmartGlobalExceptionHandler {

    @Autowired
    private Environment env;


    /**
     * 添加全局异常处理流程
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JsonResult exceptionHandler(Exception e) {

        // http 请求方式错误
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return JsonResult.buildError(ResultCode.REQUEST_METHOD_ERROR.code(), ResultCode.getMapByCode(ResultCode.REQUEST_METHOD_ERROR.code()));
        }

        // 参数类型错误
        if (e instanceof TypeMismatchException) {
            return JsonResult.buildError(ResultCode.PARA_CONVERT_ERROR.code(), ResultCode.getMapByCode(ResultCode.PARA_CONVERT_ERROR.code()));
        }

        // json 格式错误
        if (e instanceof HttpMessageNotReadableException) {
            return JsonResult.buildError(ResultCode.JSON_FORMAT_ERROR.code(), ResultCode.getMapByCode(ResultCode.JSON_FORMAT_ERROR.code()));
        }

        // 参数校验未通过
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            FieldError fieldError = bindingResult.getFieldError();
            String message = fieldError == null ? "参数校验失败" : fieldError.getDefaultMessage();
            return JsonResult.buildError(ResultCode.ERROR_PARAM.code(), message);
        }

        if (e instanceof BaseBusinessException) {
//            return JsonResult.buildError(ResultCode.PARA_ERROR.code(), e.getMessage());
            return JsonResult.buildError(ResultCode.PARA_ERROR.code(), ResultCode.getMapByCode(ResultCode.PARA_ERROR.code()));
        }
        List<String> envList = Arrays.asList(env.getActiveProfiles());
        if (envList.contains("dev") || envList.contains("online")) {
            // 开发环境
            e.printStackTrace();
        }

//        return JsonResult.buildError(ResultCode.PARA_ERROR.code(), e.getMessage());
        return JsonResult.buildError(ResultCode.PARA_ERROR.code(), ResultCode.getMapByCode(ResultCode.PARA_ERROR.code()));
    }
}
