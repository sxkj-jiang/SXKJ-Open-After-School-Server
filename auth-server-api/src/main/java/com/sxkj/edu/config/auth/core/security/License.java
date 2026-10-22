package com.sxkj.edu.config.auth.core.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.nio.charset.StandardCharsets;

/**
 * 授权文件模型（与服务端一致）。签名覆盖除 signature 外的全部字段。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class License {

    private String machineId;
    private long issuedAt;
    private long expiresAt;
    private String kind;     // INITIAL / RENEW
    private String signature;

    public License() {}

    public License(String machineId, long issuedAt, long expiresAt, String kind, String signature) {
        this.machineId = machineId;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.kind = kind;
        this.signature = signature;
    }

    /** 参与签名的规范串（两端必须一致） */
    @JsonIgnore
    public String toSign() {
        return machineId + "|" + issuedAt + "|" + expiresAt + "|" + kind;
    }

    @JsonIgnore
    public byte[] toSignBytes() {
        return toSign().getBytes(StandardCharsets.UTF_8);
    }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }
    public long getIssuedAt() { return issuedAt; }
    public void setIssuedAt(long issuedAt) { this.issuedAt = issuedAt; }
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}
