package com.sxkj.edu.common.utils;


import java.util.Base64;

/**
 * @Author Administrator
 * @Date 2024/12/5 9:28
 */
public class ImageType {
    
    public static String checkImageBase64Type(String baseImage64Date){
        byte[] imageBytes = Base64.getDecoder().decode(baseImage64Date);

        if (imageBytes.length >= 2 && imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8) {
            return "JPEG";
        }
        if (imageBytes.length >= 8 &&
                imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50 &&
                imageBytes[2] == (byte) 0x4E && imageBytes[3] == (byte) 0x47 &&
                imageBytes[4] == (byte) 0x0D && imageBytes[5] == (byte) 0x0A &&
                imageBytes[6] == (byte) 0x1A && imageBytes[7] == (byte) 0x0A) {
            return "PNG";
        }
        // 检查GIF格式
        if (imageBytes.length >= 4 &&
                imageBytes[0] == (byte) 0x47 && imageBytes[1] == (byte) 0x49 &&
                imageBytes[2] == (byte) 0x46 && imageBytes[3] == (byte) 0x38) {
            return "GIF";
        }
        // 未知格式
        return "Unknown";

    }
}
