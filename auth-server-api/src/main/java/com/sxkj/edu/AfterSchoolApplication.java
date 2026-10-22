package com.sxkj.edu;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.sxkj.edu.config.auth.core.security.License;
import com.sxkj.edu.config.auth.core.security.LicenseManager;
import com.sxkj.edu.config.auth.core.security.PublicKeyAssembler;
import com.sxkj.edu.config.auth.util.MachineFingerprint;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.time.Instant;

/**
 *@Description : 项目启动类
 *@Author : JIANGJJ
 *@Date : 2022-9-6 11:08
 *@Company : SXKJ
 *@Version : 1.0
 **/
@SpringBootApplication(scanBasePackages = {"com.sxkj.edu"})
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Import(FdfsClientConfig.class)
@MapperScan(basePackages = {"com.sxkj.edu.module.afterschool.mapper"})
@Slf4j
public class AfterSchoolApplication {

    public static void main(String[] args) {
        // 授权服务器地址：默认本机 9090，可用环境变量 LICENSE_SERVER_URL 覆盖
//        String serverUrl = System.getenv().getOrDefault("LICENSE_SERVER_URL", "http://localhost:19090");
//        String machineId = MachineFingerprint.generate();
//        Path licenseDir = Paths.get(System.getProperty("user.home"), ".anti-reverse", "license");
//
//        try {
//            PublicKey pub = PublicKeyAssembler.assemble();
//            LicenseManager mgr = new LicenseManager(serverUrl, licenseDir, pub, machineId);
//            License lic = mgr.ensureValidLicense();
//            log.info("[License] 校验通过 | 类型=" + lic.getKind()
//                    + " | 过期时间=" + Instant.ofEpochMilli(lic.getExpiresAt())
//                    + " | 机器=" + machineId);
//        } catch (Exception e) {
//            System.err.println("[License] 启动被拒绝: " + e.getMessage());
//            e.printStackTrace();
//            System.exit(1);
//            return;
//        }
        SpringApplication.run(AfterSchoolApplication.class, args);
    }
}
