package com.sxkj.edu.config.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxkj.edu.config.auth.core.security.License;
import com.sxkj.edu.config.auth.core.security.LicenseVerifier;
import com.sxkj.edu.config.auth.core.security.PublicKeyAssembler;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 运行时读取本地 license 状态，供接口暴露（不依赖 Spring 启动前的校验逻辑）。
 */
@Component
public class LicenseStatusProvider {

    private final Path licenseFile = Paths.get(
            System.getProperty("user.home"), ".anti-reverse", "license", "license.lic");
    private final ObjectMapper om = new ObjectMapper();

    public Map<String, Object> status() {
        Map<String, Object> m = new LinkedHashMap<>();
        if (!Files.exists(licenseFile)) {
            m.put("status", "NO_LICENSE");
            return m;
        }
        try {
            License lic = om.readValue(licenseFile.toFile(), License.class);
            boolean ok = LicenseVerifier.verify(lic, PublicKeyAssembler.assemble());
            long now = System.currentTimeMillis();
            m.put("status", ok ? (lic.getExpiresAt() > now ? "VALID" : "EXPIRED") : "INVALID_SIGNATURE");
            m.put("machineId", lic.getMachineId());
            m.put("kind", lic.getKind());
            m.put("issuedAt", Instant.ofEpochMilli(lic.getIssuedAt()).toString());
            m.put("expiresAt", Instant.ofEpochMilli(lic.getExpiresAt()).toString());
            m.put("daysLeft", (lic.getExpiresAt() - now) / 86_400_000L);
        } catch (Exception e) {
            m.put("status", "ERROR");
            m.put("error", e.getMessage());
        }
        return m;
    }
}
