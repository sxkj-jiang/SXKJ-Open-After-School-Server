package com.sxkj.edu.common.utils;


import com.sxkj.edu.common.config.GrobProperties;
import okhttp3.*;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangjianjun
 * @version 1.0
 * @Description ok3 client 单例工具类
 * @Classname OkhttpUtil
 * @company SXKJ
 * @Date 2020/1/21 16:38
 */
@Component
public class OkHttpUtil {

    private final Logger LOGGER = LoggerFactory.getLogger(OkHttpUtil.class);

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private final MediaType MEDIA_TYPE_URL = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static OkHttpUtil instance;
    private OkHttpClient okHttpClient;

    private final int TIMEOUT = 10;

    private OkHttpUtil() {
        okHttpClient = new OkHttpClient.Builder()
                //10秒连接超时
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                //10m秒写入超时
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                //10秒读取超时
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpUtil getInstance() {
        if (instance == null) {
            synchronized (LOCKER) {
                if (instance == null) {
                    instance = new OkHttpUtil();
                }
            }
        }
        return instance;
    }

    public String doGet(String url) {
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestForGet(url);
        return commonRequest(request);
    }


    public String doGetByParamsByEsb(String url, Map<String, String> params) {
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestForGet(url, params);
        return commonRequest(request);
    }

    /**
     * ok3 portal service query
     *
     * @param url
     * @param json
     * @return
     */
    public String doPostPortalQuery(String url, String json) {
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestByPortalInter(url, json);
        return commonRequest(request);
    }


    public String doPostJson(String url, String json) {
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestForPostJson(url, json);
        return commonRequest(request);
    }

    public String doPostForm(String url, Map<String, String> params) {
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestForPostForm(url, params);
        return commonRequest(request);
    }

    /**
     * esb 统一调用格式
     *
     * @param url
     * @param params
     * @return
     */
    public String doPostCommonQuery(String url, Map<String, String> params) {
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestCommonApi(url, params);
        return commonRequest(request);
    }


    /**
     * huawei iam in http
     *
     * @param url
     * @return
     */
    public String doGetHwIam(String url, String appId, String name, String pwd, String remoteUrl) {
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = sendLoginIam(url, appId, name, pwd, remoteUrl);
        return commonRequest(request);
    }

    public String doGetHwIamCheck(String url,String tokenId, String appId, String remoteUrl) {
        if (isBlankUrl(tokenId)) {
            return null;
        }
        Request request = sendLoginIamCheck(url, appId, tokenId, remoteUrl);
        return commonRequest(request);
    }

    private Boolean isBlankUrl(String url) {
        if (StringUtils.isBlank(url)) {
            LOGGER.info("url is not blank");
            return true;
        } else {
            return false;
        }
    }

    private String commonRequest(Request request) {
        String re = "";
        try {
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()) {
                re = response.body().string();
            } else {
                LOGGER.info("request failure url:{};message:{}", request.url().toString(), response.message());
            }
        } catch (Exception e) {
            LOGGER.error("request execute failure", e);
        }
        return re;
    }

    private Request getRequestForPostJson(String url, String json) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }


    /**
     * post 带有消息头 api
     *
     * @param url
     * @param json
     * @return
     */
    private Request getRequestByPortalInter(String url, String json) {
        RequestBody body = RequestBody.create(MEDIA_TYPE_URL, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return request;
    }


    /**
     * ok3 post params request
     *
     * @param url
     * @param params
     * @return
     */
    private Request getRequestForPostForm(String url, Map<String, String> params) {
        RequestBody requestBody = getRequestBody(params);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }


    private Request getRequestCommonApi(String url, Map<String, String> params) {
        RequestBody requestBody = getRequestBody(params);
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
        return request;
    }


    private Request setRequestPostFormCorona(String url, Map<String, String> params) {
        RequestBody requestBody = getRequestBody(params);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    private RequestBody getRequestBody(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addEncoded(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();
        return requestBody;
    }

    private Request sendLoginIam(String url, String appId, String name, String pwd, String remoteUrl) {
        Request request = new Request.Builder()
                .url(url)
                .header("appId", appId)
                .header("userName", name)
                .header("password", pwd)
                .header("authnMethod", "")
                .header("remoteIp", remoteUrl)
                .get()
                .build();
        return request;
    }

    private Request sendLoginIamCheck(String url,String appId, String tokenId, String remoteUrl) {
        Request request = new Request.Builder()
                .url(url)
                .header("tokenId", appId)
                .header("appId", tokenId)
                .header("remoteIp", remoteUrl)
                .get()
                .build();
        return request;
    }

    private Request getRequestForGet(String url, Map<String, String> params) {
        Request request = new Request.Builder()
                .url(getUrlStringForGet(url, params))
                .build();
        return request;
    }



    private Request getRequestForGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }

    private String getUrlStringForGet(String url, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(url);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    urlBuilder.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (Exception e) {
                    urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return urlBuilder.toString();
    }

}
