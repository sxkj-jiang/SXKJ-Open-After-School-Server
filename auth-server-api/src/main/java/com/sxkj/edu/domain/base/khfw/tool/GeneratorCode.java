package com.sxkj.edu.domain.base.khfw.tool;

import com.sxkj.edu.config.RedisDao;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author Administrator
 * @Date 2024/12/5 16:53
 */
public enum GeneratorCode {
    INSTANCE;

    public String createCode(RedisDao redisDao, String code, int length) {
        Long autoNum = redisDao.incr(code, 1);
        return StringUtils.leftPad(String.valueOf(autoNum), length, "0");
    }


}
