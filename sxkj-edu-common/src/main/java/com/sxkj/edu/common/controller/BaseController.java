package com.sxkj.edu.common.controller;

import com.sxkj.edu.common.anno.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 *@Description : controller基础实现入口
 *@Author : JIANGJJ
 *@Date : 2022-9-6 15:50
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Slf4j
@OperateLog
@Validated
public class BaseController {

    /**
     * 下载 Excel 消息头
     * @param fileName
     * @param workbook
     * @param response
     */
    public void downloadExcel(String fileName, Workbook workbook, HttpServletResponse response) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

}
