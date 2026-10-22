package com.sxkj.edu.common.validation;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 学校编码校验
 * @author jiangjj
 */

public class SchoolCodeValidator implements ConstraintValidator<SchoolCodeAnno, String> {

    @Override
    public void initialize(SchoolCodeAnno annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        // 校验手机
        return ValidationUtils.numValid5To16(value);
    }

}
