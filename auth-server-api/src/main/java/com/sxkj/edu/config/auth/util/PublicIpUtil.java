package com.sxkj.edu.config.auth.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 尽力探测本机公网 IP（best-effort，失败返回 null，不阻断启动）。
 * 结果会随注册请求上报，便于服务端核对「远程监控到的 IP」与「客户端自报 IP」。
 */
public final class PublicIpUtil {

    private static final String[] ENDPOINTS = {
            "https://api.ipify.org", "https://ifconfig.me/ip"
    };

    private PublicIpUtil() {}

    public static String detect() {
        for (String ep : ENDPOINTS) {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(ep).openConnection();
                conn.setConnectTimeout(2000);
                conn.setReadTimeout(2000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                if (code == 200) {
                    String body = readAll(conn.getInputStream());
                    if (body != null && !body.trim().isEmpty()) {
                        return body.trim();
                    }
                }
            } catch (Exception ignore) {
                // 尝试下一个端点
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return null;
    }

    private static String readAll(InputStream in) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            buf.write(b, 0, n);
        }
        in.close();
        return new String(buf.toByteArray(), StandardCharsets.UTF_8);
    }
}
