package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 评分等级枚举
 */
@Getter
@AllArgsConstructor
public enum ScoreLevelEnum {
    EXCELLENT(new BigDecimal("4.5"), "优秀"),
    GOOD(new BigDecimal("4.0"), "良好"),
    PASS(new BigDecimal("3.0"), "合格"),
    FAIL(new BigDecimal("0"), "不合格");

    private final BigDecimal minScore;
    private final String desc;

    public static String getLevelByScore(BigDecimal score) {
        if (score == null) {
            return FAIL.getDesc();
        }
        for (ScoreLevelEnum level : values()) {
            if (score.compareTo(level.getMinScore()) >= 0) {
                return level.getDesc();
            }
        }
        return FAIL.getDesc();
    }
}
