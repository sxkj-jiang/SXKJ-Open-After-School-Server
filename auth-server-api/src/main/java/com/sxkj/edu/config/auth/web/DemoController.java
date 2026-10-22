package com.sxkj.edu.config.auth.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DemoController {

    private final LicenseStatusProvider provider;

    public DemoController(LicenseStatusProvider provider) {
        this.provider = provider;
    }

    @GetMapping("/api/health")
    public String health() {
        return "ok";
    }

    /** 查看本地 license 状态（是否有效、剩余天数、过期时间等） */
    @GetMapping("/api/license")
    public Map<String, Object> license() {
        return provider.status();
    }
}
