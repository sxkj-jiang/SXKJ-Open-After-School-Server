package com.sxkj.edu.config.auth.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxkj.edu.config.auth.LicenseServerClient;
import com.sxkj.edu.config.auth.util.MachineFingerprint;
import com.sxkj.edu.config.auth.util.PublicIpUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PublicKey;

/**
 * 本地授权管理器：启动前调用 ensureValidLicense()。
 * <ul>
 *   <li>无 license 文件 -> 向服务端申请初始 license（远程签发）</li>
 *   <li>license 有效但未过期 -> 直接通过</li>
 *   <li>license 已过期 -> 必须调用服务端 renew 远程重新授权，拿不到则拒绝启动</li>
 *   <li>签名校验失败（文件被篡改/机器不匹配）-> 拒绝启动</li>
 *   <li>启动后向服务端注册，供其远程监控本机公网 IP</li>
 * </ul>
 */
public class LicenseManager {

    private final String serverUrl;
    private final Path licenseFile;
    private final PublicKey publicKey;
    private final String machineId;
    private final LicenseServerClient client;

    public LicenseManager(String serverUrl, Path licenseDir, PublicKey publicKey, String machineId) {
        this.serverUrl = serverUrl.replaceAll("/$", "");
        this.licenseFile = licenseDir.resolve("license.lic");
        this.publicKey = publicKey;
        this.machineId = machineId;
        this.client = new LicenseServerClient(this.serverUrl);
    }

    /** 确保持有有效 license；任何不满足都抛异常，由调用方拒绝启动 */
    public License ensureValidLicense() throws Exception {
        License lic = load();
        if (lic != null) {
            if (!LicenseVerifier.verify(lic, publicKey)) {
                throw new IllegalStateException("license 验签失败：文件可能已被篡改");
            }
            if (!machineId.equals(lic.getMachineId())) {
                throw new IllegalStateException("license 与当前机器不匹配（machineId 不一致）");
            }
        } else {
            lic = issue(); // 首次 -> 远程签发
        }
        registerForMonitoring(lic);
        return lic;
    }

    private License issue() throws Exception {
        License lic = client.issue(machineId);
        checkOwned(lic, "初始");
        save(lic);
        return lic;
    }


    private void checkOwned(License lic, String tag) {
        if (!LicenseVerifier.verify(lic, publicKey)) {
            throw new IllegalStateException("服务端下发的" + tag + " license 验签失败（可能被中间人篡改）");
        }
        if (!machineId.equals(lic.getMachineId())) {
            throw new IllegalStateException(tag + " license 的 machineId 与当前机器不一致");
        }
    }

    private void registerForMonitoring(License lic) {
        try {
            client.register(machineId, MachineFingerprint.hostname(),
                    "1.0.0", PublicIpUtil.detect());
        } catch (Exception e) {
            // 监控上报失败不阻断启动，仅告警
            System.err.println("[License] 监控上报失败（不影响启动）: " + e.getMessage());
        }
    }

    private License load() throws Exception {
        if (!Files.exists(licenseFile)) return null;
        return new ObjectMapper().readValue(licenseFile.toFile(), License.class);
    }

    private void save(License lic) throws Exception {
        Files.createDirectories(licenseFile.getParent());
        new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValue(licenseFile.toFile(), lic);
    }
}
