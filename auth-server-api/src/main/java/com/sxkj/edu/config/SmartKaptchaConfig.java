package com.sxkj.edu.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.sxkj.edu.common.utils.kaptcha.KaptchaNoise;
import com.sxkj.edu.common.utils.kaptcha.KaptchaWordRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 *@Description : 验证码配置
 *@Author : JIANGJJ
 *@Date : 2022-9-7 14:51
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Configuration
public class SmartKaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.border.color", "34,114,200");
        properties.setProperty("kaptcha.image.width", "125");
        properties.setProperty("kaptcha.image.height", "45");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Arial Narrow,Serif,Helvetica,Tahoma,Times New Roman,Verdana");
        properties.setProperty("kaptcha.textproducer.font.size", "38");

        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");

        properties.setProperty("kaptcha.word.impl", KaptchaWordRenderer.class.getName());
        properties.setProperty("kaptcha.noise.impl", KaptchaNoise.class.getName());

        Config config=new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
