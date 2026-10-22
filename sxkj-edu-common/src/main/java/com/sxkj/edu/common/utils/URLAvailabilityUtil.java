package com.sxkj.edu.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * [ 地址是否有效 ]
 *
 * @version 1.0
 * @Author: zh
 * @DateTime: 2023 年 12 月 18 日
 * @since JDK1.8
 **/
@Slf4j
public class URLAvailabilityUtil {
    private static URL url;
    private static HttpURLConnection con;
    private static int state = -1;

    /**
     * 功能：检测当前URL是否可连接或是否有效,
     * 描述：最多连接网络 2 次, 如果 2 次都不成功，视为该地址不可用
     *
     * @param urlStr 指定URL网络地址
     * @return URL
     */
    public synchronized static Integer isConnect(String urlStr) {
        int num = 0;
        if (urlStr == null || urlStr.length() <= 0) {
            return null;
        }
        while (num < 2) {
            try {
                url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                state = con.getResponseCode();
                if (state == 200) {
                    return state;
                } else {
                    num++;
                }
            } catch (Exception ex) {
                log.error("call URLAvailabilityUtil isConnect exec error,request:urlStr:{}",urlStr, ex);
                return null;
            }
        }
        return state;
    }

}
