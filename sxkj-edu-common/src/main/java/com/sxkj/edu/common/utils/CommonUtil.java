package com.sxkj.edu.common.utils;

/**
 * @Description: CommonUtil
 * @Author yanghui
 * @Date 2024年09月06日
 */
public class CommonUtil {

    /**
     * 阿拉伯数字转汉字
     * @param number
     * @return
     */
    public static String formatUploadBatch(Integer number) {
        String[] units = {"", "十", "百", "千"};
        String[] digits = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        StringBuilder result = new StringBuilder();
        int unit = 0;
        while (number > 0) {
            int digit = number % 10;
            if (digit != 0) {
                result.insert(0, units[unit]);
                result.insert(0, digits[digit]);
            } else {
                if (result.length() > 0 && result.charAt(0) != '零') {
                    result.insert(0, digits[digit]);
                }
            }
            number /= 10;
            unit++;
        }
        if (result.length() == 0) {
            result.append("零");
        }
        return "第" + result.toString() + "次";
    }
}
