package com.sxkj.edu.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class SmartIPUtil {

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    /**
     * 获取本机互联网ip
     *
     * @return
     */
    public static String getRemoteNetIp() {
        String ip = "";
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            ip = in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String getClientIp(HttpServletRequest request) {
        // 1. 尝试从 X-Forwarded-For 获取
        String ip = request.getHeader("X-Forwarded-For");
//        log.info("call getClientIp ip:{}", ip);

        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For 可能包含多个 IP: client, proxy1, proxy2
            // 第一个 IP 是原始客户端 IP
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index).trim();
            } else {
                return ip.trim();
            }
        }

        // 2. 尝试从 X-Real-IP 获取
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        // 3. 降级到远程地址
        return request.getRemoteAddr();
    }

    public static String getLocalHostIP() {
        // 本地IP，如果没有配置外网IP则返回它
        String localIp = null;
        // 外网IP
        String netIp = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            // 是否找到外网IP
            boolean finded = false;
            while (netInterfaces.hasMoreElements() && ! finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    // 外网IP
                    if (! ip.isSiteLocalAddress() && ! ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == - 1) {
                        netIp = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress() && ! ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == - 1) {
                        // 内网IP
                        localIp = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.getMessage();
        }
        if (netIp != null && ! "".equals(netIp)) {
            return netIp;
        } else {
            return localIp;
        }
    }

    public static String getRemoteIp(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = getClientIp(request);

        return ip;
    }

    private static String getXForwardedForIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        //ip 无效直接返回
        if (! ipValid(ip)) {
            return "";
        }
        if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (! ("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
            return ip;
        }
        return ip;
    }

    private static Boolean ipValid(String ip) {
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        return true;
    }

    private static String realIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return "";
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }



    public static void main(String[] args) {

    }

}
