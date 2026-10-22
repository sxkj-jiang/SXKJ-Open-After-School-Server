package com.sxkj.edu.config;

import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.helper.SpringContextHolder;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SmartRequestTokenUtil {

    private static final ThreadLocal<RequestTokenBO> REQUEST_USER_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, Object>> THREAD_VARIABLES = ThreadLocal.withInitial(HashMap::new);

    public static void setUser(HttpServletRequest request, RequestTokenBO requestToken) {
        request.setAttribute(GrobProperties.getInstance().USER_KEY, requestToken);
        REQUEST_USER_THREAD_LOCAL.set(requestToken);
    }

    public static RequestTokenBO getThreadLocalUser() {
        return REQUEST_USER_THREAD_LOCAL.get();
    }

    public static RequestTokenBO getRequestUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                return (RequestTokenBO) request.getAttribute(GrobProperties.getInstance().USER_KEY);
            }
        }
        return null;
    }

    public static Long getRequestUserId() {
        RequestTokenBO requestUser = getRequestUser();
        if (null == requestUser) {
            return null;
        }
        return requestUser.getRequestUserId();
    }

    public static void setThreadVariable(String name, Object value) {
        THREAD_VARIABLES.get().put(name, value);
    }

    public static <T> T getThreadVariable(String name) {
        return (T) THREAD_VARIABLES.get().get(name);
    }

    public static Map<String, Long> getUserSchoolByName() {
        return getThreadVariable("set_user_school_info");
    }

    public static Long getRequestUserStuSchoolId() {
        RequestTokenBO requestUser = getRequestUser();
        if (requestUser == null) {
            return 0L;
        }
        String loginName = requestUser.getEmployeeBO().getLoginName();
        Boolean isNotAdmin = requestUser.getEmployeeBO().getIsSuperman();
        if (isNotAdmin) {
            return getSchoolIdByLoginName(loginName);
        } else {
            Long schId = requestUser.getEmployeeBO().getSchoolId();
            return schId;
        }
    }


    private static Long getSchoolIdByLoginName(String loginName) {
        RedisCache redisCache = SpringContextHolder.getBean(RedisCache.class);
        String schoolId = (String) redisCache.hget("set_user_school_info", loginName);
        if (StringUtils.isBlank(schoolId)) return null;
        return Long.valueOf(schoolId);
    }

    /**
     * 清理当前线程的ThreadLocal数据，防止内存泄漏。
     * 必须在请求结束时调用（如拦截器的afterCompletion）。
     */
    public static void clear() {
        REQUEST_USER_THREAD_LOCAL.remove();
        THREAD_VARIABLES.remove();
    }
}
