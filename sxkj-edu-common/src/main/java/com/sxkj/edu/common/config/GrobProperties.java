package com.sxkj.edu.common.config;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author jiangjianjun
 * @version 1.0
 * @Description 获取配置配置全局类，只适应静态参数，修改参数后无需重新部署
 * @Classname Properties
 * @company SXKJ
 * @Date 2020/3/2 11:07
 */
@Slf4j
public final class GrobProperties {

    private static final GrobProperties PROPERTIES = new GrobProperties();

    /**
     * 破解配置
     */
    public Boolean CRACK_OPEN = null;

    /**
     * aes加解密密钥
     */
    public String AES_KEY = null;
    /**
     * 用户key
     */
    public String USER_KEY = null;

    /**
     * 用户key
     */
    public String USER_ENCRPT = null;

    /**
     * 替换前缀
     */
    public String ZIP_REPLACE_PATH = null;
    /**
     * 地址替换后的结果
     */
    public String ZIP_REPLACE_RES_PATH = null;
    /**
     * 服务器解压路径
     */
    public String UNZIP_PATH = null;
    /**
     * fastdfs 压缩文件本地路径
     */
    public String ZIP_UPLOAD_PATH = null;

    // 默认新闻照片
    public String NEWS_PIC = "";
    // 默认活动照片
    public Long ACT_PIC = 0L;
    // 默认班级照片
    public String CLASS_PIC = "";
    // 默认男生头像
    public Long manDefUrlId = 0L;
    // 默认女生头像
    public Long womanDefUrlId = 0L;

    public Long isOpenWxPay = 0L;

    // 默认课程封面
    public Long COURSE_COVER = 0L;

    // 默认学校封面
    public Long SCHOOL_COVER = 0L;

    private GrobProperties() {
        java.util.Properties config = new java.util.Properties();
        InputStream inputStream = null;
        try {
            /* 注释：也可以直接在src/main/resources目录下新建配置文件，
                但是new FileInputStream("res/myCanal.properties")需要改为new FileInputStream("src/main/resources/application.properties") */
            String url = System.getProperty("user.dir") + File.separator + "configs/application.properties";
            log.info("server init GrobProperties path: "+url);
            inputStream = new FileInputStream(url);
            config.load(new InputStreamReader(inputStream, "UTF-8"));


            CRACK_OPEN = new Boolean(config.getProperty("crack.config.open"));
            AES_KEY = config.getProperty("aes.data.key");
            USER_KEY = config.getProperty("edu.user.key");
            USER_ENCRPT = config.getProperty("edu.user.encryptPassword");

            ZIP_UPLOAD_PATH = config.getProperty("upload.zip.path");
            ZIP_REPLACE_PATH = config.getProperty("upload.zip.file.replace.path");
            ZIP_REPLACE_RES_PATH = config.getProperty("upload.zip.replace.result.path");
            UNZIP_PATH = config.getProperty("upload.unzip.path");

            NEWS_PIC = config.getProperty("default.news.picure.url");
            ACT_PIC = new Long(config.getProperty("default.activ.picure.url"));
            CLASS_PIC = config.getProperty("default.class.picure.url");

            manDefUrlId = new Long(config.getProperty("default.logo.man.url"));
            womanDefUrlId = new Long(config.getProperty("default.logo.woman.url"));
            isOpenWxPay = new Long(config.getProperty("isOpenWxPay"));
            COURSE_COVER = new Long(config.getProperty("default.cover.resource.url"));
            SCHOOL_COVER = new Long(config.getProperty("default.school.cover.resource.url"));
            log.info("server init GrobProperties isOpenWxPay: " + isOpenWxPay);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static GrobProperties getInstance() {
        return PROPERTIES;
    }

}

