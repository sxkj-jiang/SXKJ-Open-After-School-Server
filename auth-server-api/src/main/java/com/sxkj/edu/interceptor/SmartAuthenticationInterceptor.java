package com.sxkj.edu.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sxkj.edu.common.anno.AccessLimit;
import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.SmartIPUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.login.LoginTokenService;
import com.sxkj.edu.service.BaseBintangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *@Description : 登录拦截器
 *@Author : JIANGJJ
 *@Date : 2022-9-6 11:00
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Component
@Slf4j
public class SmartAuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Value("${access-control-allow-origin}")
    private String accessControlAllowOrigin;

    @Resource
    private LoginTokenService loginTokenService;

    @Resource
    private BaseBintangService baseBintangService;

    @Resource
    private RedisCache redisCache;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //放行的Uri前缀
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String target = uri.replaceFirst(contextPath, "");

        // 跨域配置
        crossDomainConfig(response);
        boolean isHandlerMethod = handler instanceof HandlerMethod;
        if (!isHandlerMethod) {
            return true;
        }
        if (baseBintangService.isExemptUrl(target)) {
            return true;
        }

        // 不需要登录的注解
        Boolean isNoNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class) != null;
        if (isNoNeedLogin) {
            return true;
        }

        String xHeaderToken = request.getHeader(CoreConstant.TOKEN_NAME);
        String xRequestToken = request.getParameter(CoreConstant.TOKEN_NAME);
        String xAccessToken = null != xHeaderToken ? xHeaderToken : xRequestToken;
        if (null == xAccessToken) {
            this.outputResult(response, ResultCode.LOGIN_ERROR.code(),"您的登录已过期，请重新登录");
            return false;
        }

        RequestTokenBO requestToken = loginTokenService.getEmployeeTokenInfo(xAccessToken);
        if (null == requestToken) {
            this.outputResult(response, ResultCode.LOGIN_ERROR.code(),"您的登录已过期，请重新登录");
            return false;
        }

        RolePermission permission = ((HandlerMethod) handler).getMethodAnnotation(RolePermission.class);
        if(null == permission){
            Class<?> clazz = ((HandlerMethod) handler).getBeanType();
            permission = clazz.getAnnotation(RolePermission.class);
        }
        if(null == permission){
            SmartRequestTokenUtil.setUser(request, requestToken);
            return true;
        }
        Set<Long> methodRoleSet = Arrays.stream(permission.roles()).boxed().collect(Collectors.toSet());
        if(methodRoleSet.isEmpty()){
            SmartRequestTokenUtil.setUser(request, requestToken);
            return true;
        }

        Set<Long> userRoleSet = requestToken.getEmployeeBO().getRoleSet();

        if(methodRoleSet.stream().anyMatch(userRoleSet::contains)){
            SmartRequestTokenUtil.setUser(request, requestToken);
            return true;
        }

        //获取方法中的注解,看是否有该注解
        AccessLimit accessLimit = ((HandlerMethod) handler).getMethodAnnotation(AccessLimit.class);
        // 自定义注解实现
        if (accessLimit != null) {
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

            String ip = SmartIPUtil.getRemoteIp(request);
            //从redis中获取用户访问的次数
            Integer count = (Integer) redisCache.get(CoreConstant.Redis_logistics_ip_key + ip);
            if (count == null) {
                //第一次访问
                redisCache.set(CoreConstant.Redis_logistics_ip_key + ip, 1, seconds);
            } else if (count < maxCount) {
                //加1
                count = count + 1;
                redisCache.set(CoreConstant.Redis_logistics_ip_key + ip, count, seconds);
            } else {
                //超出访问次数
                log.warn("访问过快ip  ===> " + ip + " 且在   " + seconds + " 秒内超过最大限制  ===> " + maxCount + " 次数达到    ====> " + count);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                JSONObject result = new JSONObject();
                result.put("status", ResultCode.AUTH_MORE_ERROR.code());
                result.put("message", "操作太快了,请稍后在试!");
                Object obj = JSONObject.toJSON(result);
                response.getWriter().write(JSONObject.toJSONString(obj));
                return false;
            }
        }
        this.outputResult(response, ResultCode.NOT_HAVE_PRIVILEGES.code(),"暂无权限访问");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        SmartRequestTokenUtil.clear();
    }

    /**
     * 配置跨域
     *
     * @param response
     */
    private void crossDomainConfig(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication,Origin, X-Requested-With,Access-Terminal, Content-Type, " + "Accept, x-access-token,X-Info");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
    }


    /**
     * 错误输出
     *
     * @param response
     * @throws IOException
     */
    private void outputResult(HttpServletResponse response, String code,String message) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("message", message);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonObject.toJSONString());
        response.flushBuffer();
    }
}
