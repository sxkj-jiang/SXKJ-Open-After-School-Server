package com.sxkj.edu.common.validation;

import cn.hutool.core.collection.CollUtil;
import com.sxkj.edu.common.utils.SmartVerificationUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author jjj
 */
public class ValidationUtils {

    private static final Pattern PATTERN_MOBILE = Pattern.compile("^1[3-9]\\d{9}$");

    private static final Pattern PATTERN_CHINESE = Pattern.compile("^1[\u4e00]\\d{9}$");

    private static final Pattern PATTERN_URL = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");


    public static boolean isMobile(String mobile) {
        return StringUtils.hasText(mobile)
                && PATTERN_MOBILE.matcher(mobile).matches();
    }

    public static boolean userNameValid(String username) {
        boolean matches = Pattern.matches(SmartVerificationUtil.CHINESE_CHECK, username);
        return matches;
    }

    public static boolean checkLoginNameValid(String username) {
        boolean matches = Pattern.matches(SmartVerificationUtil.ENG_NUMBER_REGEXP, username);
        return matches;
    }

    public static boolean passwordValid(String username) {
        boolean matches = Pattern.matches(SmartVerificationUtil.PWD_REGEXP, username);
        return matches;
    }

    public static boolean isURL(String url) {
        return StringUtils.hasText(url)
                && PATTERN_URL.matcher(url).matches();
    }

    /**
     * 校验字母加-
     * @param data
     * @return
     */
    public static boolean englishNum(String data) {
        return StringUtils.hasText(data)
                && Pattern.matches(SmartVerificationUtil.ENGLISH_NUM_XH, data);
    }

    /**
     * 校验代码类
     * @param data
     * @return
     */
    public static boolean checkCodeMethod(String data) {
        return StringUtils.hasText(data)
                && Pattern.matches(SmartVerificationUtil.CODE_CHECK_STATIS, data);
    }

    /**
     * 校验字母加下划线
     * @param data
     * @return
     */
    public static boolean englishChineseReg(String data) {
        return StringUtils.hasText(data)
                && Pattern.matches(SmartVerificationUtil.ENGLISH_NUM_XH, data);
    }

    /**
     * 校验4位至16位的数字
     * @param data
     * @return
     */
    public static boolean numValid(String data) {
        return StringUtils.hasText(data)
                && Pattern.matches(SmartVerificationUtil.NUMBER, data);
    }

    /**
     * 校验5位至16位的数字
     * @param data
     * @return
     */
    public static boolean numValid5To16(String data) {
        return StringUtils.hasText(data)
                && Pattern.matches(SmartVerificationUtil.SCHOOL_CODE_NUMBER, data);
    }

    /**
     * 校验4位至16位的数字
     * @param data
     * @return
     */
    public static boolean chineseValid(String data) {
        return StringUtils.hasText(data)
                && Pattern.matches(SmartVerificationUtil.CHINESE_CHECK, data);
    }

    public static void validate(Validator validator, Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (CollUtil.isNotEmpty(constraintViolations)) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public static boolean chineseTitleValid(String data) {
        if(data.length() >= 2 && StringUtils.hasText(data)
                && Pattern.matches(SmartVerificationUtil.CHINESE_TITLE_CHECK, data)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(chineseValid("绘画"));
    }

}
