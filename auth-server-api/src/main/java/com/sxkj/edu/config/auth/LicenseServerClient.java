package com.sxkj.edu.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxkj.edu.config.auth.core.security.License;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 授权服务端 HTTP 客户端（HttpURLConnection，无 Spring 依赖，可在 Spring 启动前使用）。
 */
public class LicenseServerClient {

    private final String baseUrl;
    private final ObjectMapper om = new ObjectMapper();
    /** 服务端可能尚未就绪（客户端先于授权服务端启动），最多重试若干次并退避 */
    private static final int MAX_ATTEMPTS = 8;
    private static final long RETRY_BACKOFF_MS = 2000;
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 10000;

    public LicenseServerClient(String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
    }

    /** 初次申请授权（带重试，容忍授权服务端尚未启动） */
    public License issue(String machineId) throws Exception {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("machineId", machineId);
        Response resp = retryPost("/api/license/issue", body);
        if (resp.code != 200) {
            throw new IllegalStateException("申请授权失败，服务端返回 " + resp.code);
        }
        return om.readValue(resp.body, License.class);
    }

    /** 过期后续期（远程重新授权，带重试） */
    public License renew(String machineId, License old) throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("machineId", machineId);
        body.put("license", old);
        Response resp = retryPost("/api/license/renew", body);
        if (resp.code != 200) {
            throw new IllegalStateException("续期失败，服务端返回 " + resp.code + "：" + resp.body);
        }
        return om.readValue(resp.body, License.class);
    }

    /** 启动上报，供服务端远程监控本机公网 IP（带重试，失败仅告警） */
    public void register(String machineId, String hostname, String appVersion, String clientPublicIp) throws Exception {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("machineId", machineId);
        body.put("hostname", hostname);
        body.put("appVersion", appVersion);
        body.put("clientPublicIp", clientPublicIp == null ? "" : clientPublicIp);
        Response resp = retryPost("/api/license/register", body);
        if (resp.code != 200) {
            throw new IllegalStateException("监控上报失败，服务端返回 " + resp.code);
        }
    }

    /** 发送 POST 并在连接/网络失败时按退避重试 */
    private Response retryPost(String path, Object body) throws Exception {
        Exception last = null;
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            try {
                return post(path, om.writeValueAsString(body));
            } catch (Exception e) {
                last = e;
                if (i < MAX_ATTEMPTS) {
                    System.err.println("[License] 与授权服务端通信失败（第 " + i + " 次），" + RETRY_BACKOFF_MS + "ms 后重试: " + e.getMessage());
                    Thread.sleep(RETRY_BACKOFF_MS);
                }
            }
        }
        throw last == null ? new IllegalStateException("授权服务端通信失败") : last;
    }

    private Response post(String path, String json) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(baseUrl + path).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        byte[] payload = json.getBytes(StandardCharsets.UTF_8);
        conn.setFixedLengthStreamingMode(payload.length);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload);
        }
        int code = conn.getResponseCode();
        InputStream in = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String respBody = readAll(in);
        conn.disconnect();
        return new Response(code, respBody);
    }

    private static String readAll(InputStream in) throws Exception {
        if (in == null) return "";
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                buf.write(b, 0, n);
            }
            return new String(buf.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            in.close();
        }
    }

    /** 简单响应封装：状态码 + 响应体 */
    private static final class Response {
        final int code;
        final String body;

        Response(int code, String body) {
            this.code = code;
            this.body = body;
        }
    }
}
