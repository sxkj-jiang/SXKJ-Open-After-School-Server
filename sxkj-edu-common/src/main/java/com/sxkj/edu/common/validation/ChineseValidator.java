package com.sxkj.edu.common.validation;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 姓名校验
 * @author
 *
 */

public class ChineseValidator implements ConstraintValidator<ChineseAnno, String> {

    @Override
    public void initialize(ChineseAnno annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果姓名为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        // 校验字母加横线
        return ValidationUtils.chineseValid(value);
    }

}
