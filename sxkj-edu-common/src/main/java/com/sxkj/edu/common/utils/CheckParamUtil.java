package com.sxkj.edu.common.utils;


import com.alibaba.fastjson.JSONObject;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.exception.ParamCheckException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *@Description : 检查参数工具类
 *@Author : JIANGJJ
 *@Date : 2022-12-27 11:22
 *@Company : SXKJ
 *@Version : 1.0
 **/
public class CheckParamUtil {

    /**
     * 检查 String 类型的参数是否传递
     *
     * @param param   [String] 参数
     * @param message [String] 返回信息
     */
    public static void checkStringParam(String param, String message) {
        if (Args.isEmpty(param)) {
            throw new BaseBusinessException(param, message);
        }
    }


    /**
     * 检查 Object 类型参数是否传递
     * <P> 一般用来检测对象 </P>
     *
     * @param o       [Object] Param
     * @param message [String] 返回信息
     */
    public static void checkObjectParam(Object o, String message) {
        if (o == null ) {
            throw new ParamCheckException(Integer.parseInt(ResultCode.PARA_ERROR.code()), message);
        }
    }

    public static void checkLongParam(Long id, String message) {
        if (id == null || id == 0) {
            throw new ParamCheckException(Integer.parseInt(ResultCode.PARA_ERROR.code()), message);
        }
    }

    public static void checkIntegerParam(Integer id, String message) {
        if (id == null ) {
            throw new ParamCheckException(Integer.parseInt(ResultCode.PARA_ERROR.code()), message);
        }
    }

    public static void checkIntegerTypeNot0Param(Integer status, String message) {
        if (status == null || status == 0) {
            throw new ParamCheckException(Integer.parseInt(ResultCode.PARA_ERROR.code()), message);
        }
    }

    public static Boolean checkIntegerStatusParam(Integer status) {
        Boolean bs = false;
        if (status != null && status > 0) {
            bs = true;
        }
        return bs;
    }

    /**
     * long id的有效判断
     * @param id
     * @return
     */
    public static Boolean checkLongIdParam(Long id) {
        Boolean bs = false;
        if (id != null && id > 0) {
            bs = true;
        }
        return bs;
    }

    public static boolean checkPortalFlightQueryParam(JSONObject request) {
        boolean bs = false;
        if (request == null || request.size() == 0
                || request.containsKey("flight_date") == false || request.containsKey("query_type") == false
                || request.containsKey("source_type") == false || request.containsKey("page_size") == false
                || request.containsKey("page_index") == false) {
            bs = true;
        }
        return bs;
    }

    public static Boolean checkLongIdIsNull(Long id) {
        Boolean bs = false;
        if (id == null || id == 0) {
            bs = true;
        }
        return bs;
    }

    /**
     * 检查 List 类型的参数是否传递
     *
     * @param param   [String] 参数
     * @param message [String] 返回信息
     */
    public static void checkListParam(List param, String message) {
        if (CollectionUtils.isEmpty(param)) {
            throw new ParamCheckException(Integer.parseInt(ResultCode.PARA_ERROR.code()), message);
        }
    }

    public static void checkStringLen(String str, int len, String message) {
        if (!StringUtils.isEmpty(str) && str.length() > len ) {
            throw new ParamCheckException(Integer.parseInt(ResultCode.PARA_ERROR.code()), message);
        }
    }

    public static void checkIntMax(Integer integer, int max, String message) {
        if (integer != null && integer > max ) {
            throw new ParamCheckException(Integer.parseInt(ResultCode.PARA_ERROR.code()), message);
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            System.out.print(3);
        }
        System.out.println();
    }

}
