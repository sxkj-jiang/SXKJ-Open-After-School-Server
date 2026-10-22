package com.sxkj.edu.common.validation;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.IdcardUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 身份证校验
 * @author jiangjj
 */

public class PersonCardValidator implements ConstraintValidator<PersonCard, String> {

    @Override
    public void initialize(PersonCard annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果身份证号为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(value)) {
            return true;
        }
        // 校验身份证号
        boolean bs = IdcardUtil.isValidCard(value);
        return bs;
    }

}
