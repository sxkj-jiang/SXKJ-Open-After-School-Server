package com.sxkj.edu.config.auth.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;

/**
 * 机器指纹：综合主机名 / 操作系统 / 首块非回环网卡 MAC，做 SHA-256 得到稳定 machineId。
 * 用于把 license 绑定到具体设备，防止被拷贝到别的机器使用。
 */
public final class MachineFingerprint {

    private MachineFingerprint() {}

    public static String generate() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(InetAddress.getLocalHost().getHostName());
        } catch (Exception e) {
            sb.append("unknown-host");
        }
        sb.append('|').append(System.getProperty("os.name"))
          .append('|').append(System.getProperty("os.arch"));
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (ni.isLoopback() || !ni.isUp()) continue;
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    sb.append('|').append(hex(mac));
                    break;
                }
            }
        } catch (Exception ignore) {
            // best-effort
        }
        return sha256(sb.toString());
    }

    public static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            StringBuilder h = new StringBuilder();
            for (byte b : md.digest(s.getBytes(StandardCharsets.UTF_8))) {
                h.append(String.format("%02x", b));
            }
            return h.toString();
        } catch (Exception e) {
            return Integer.toHexString(s.hashCode());
        }
    }

    private static String hex(byte[] b) {
        StringBuilder h = new StringBuilder();
        for (byte x : b) h.append(String.format("%02x", x));
        return h.toString();
    }
}
