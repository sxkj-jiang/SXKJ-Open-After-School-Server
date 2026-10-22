package com.sxkj.edu.common.base.constants;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 *@Description : 通用常量
 *@Author : JIANGJJ
 *@Date : 2022-9-6 11:11
 *@Company : SXKJ
 *@Version : 1.0
 **/
public class CommonConst {


    public static final class Page {
        public static final Integer SIZE = 10;
    }


    public static final class CommonCollection {

        public static final String IGNORE_H5_URL_MAPPING = "/h5/api";

        public static final Set<String> IGNORE_URL_MAPPING = ImmutableSet.of(IGNORE_H5_URL_MAPPING);

//        static {
//            IGNORE_URL_SET.add("/swagger");
//            IGNORE_URL_SET.add("/h5/api");
//            IGNORE_URL_SET.add("/doc.html");
//            IGNORE_URL_SET.add("/webjars");
//            IGNORE_URL_SET.add("/actuator/prometheus");
//        }


        public static Boolean contain(Set<String> ignoresSet, String uri) {
            if (CollectionUtils.isEmpty(ignoresSet)) {
                return false;
            }
            for (String ignoreUrl : ignoresSet) {
                if (uri.contains(ignoreUrl)) {
                    return true;
                }
            }
            return false;
        }
    }

}
