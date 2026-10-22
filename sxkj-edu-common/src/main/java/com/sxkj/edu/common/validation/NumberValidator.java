package com.sxkj.edu.common.validation;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号校验
 * @author jiangjj
 */

public class NumberValidator implements ConstraintValidator<NumberAnno, String> {

    @Override
    public void initialize(NumberAnno annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        // 校验手机
        return ValidationUtils.numValid(value);
    }

}
