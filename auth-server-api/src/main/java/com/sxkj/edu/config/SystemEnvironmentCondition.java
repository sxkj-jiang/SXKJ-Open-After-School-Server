package com.sxkj.edu.config;

import com.sxkj.edu.common.base.constants.SystemEnvironmentEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 是否是正式环境
 *
 * @author jiangjj
 * @date 2020/08/27 08:56
 */
public class SystemEnvironmentCondition implements Condition {

    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return ! SystemEnvironmentEnum.PROD.equalsValue(systemEnvironment);
    }
}
