package com.sxkj.edu.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.enums.JudgeEnum;
import com.sxkj.edu.common.utils.SmartStringUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.entity.UserOperateLogEntity;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 *@Description : 操作日志记录处理,对所有OperateLog注解的Controller进行操作日志监控
 *@Author : JIANGJJ
 *@Date : 2022-9-7 10:34
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Slf4j
@Aspect
@Component
public class SmartOperateLogAspect {

    @Autowired
    private LogService logService;

    /**
     * 对注解OperateLog的类进行切面
     */
    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void logPointCut() {
    }

    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            OperateLog operateLog = this.getAnnotationLog(joinPoint);
            if (operateLog == null) {
                return;
            }
            RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
            if (requestToken == null) {
                return;
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String operateMethod = className + "." + methodName;
            Object[] args = joinPoint.getArgs();
            StringBuilder sb = new StringBuilder();
            for (Object obj : args) {
                if(obj!=null&&obj.getClass()!=null){
                    sb.append(obj.getClass().getSimpleName());
                }
                sb.append("[");
                // 解决序列化 fastjson版本问题 SerializerFeature.IgnoreNonFieldGetter
                sb.append(JSON.toJSONString(obj, SerializerFeature.IgnoreNonFieldGetter));
                sb.append("]");
            }
            String params = sb.toString();
            String failReason = null;
            Integer result = JudgeEnum.YES.getValue();
            if (e != null) {
                result = JudgeEnum.NO.getValue();
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw, true);
                e.printStackTrace(pw);
                failReason = sw.toString();
                pw.flush();
                pw.close();
                sw.flush();
                sw.close();
            }
            UserOperateLogEntity operateLogEntity =
                UserOperateLogEntity.builder().userId(requestToken.getRequestUserId()).userName(requestToken.getEmployeeBO().getActualName()).url(request.getRequestURI()).method(operateMethod).param(params).failReason(failReason).result(result).build();
            ApiOperation apiOperation = this.getApiOperation(joinPoint);
            if (apiOperation != null) {
                operateLogEntity.setContent(apiOperation.value());
            }
            Api api = this.getApi(joinPoint);
            if (api != null) {
                String[] tags = api.tags();
                operateLogEntity.setModule(SmartStringUtil.join(tags, ","));
            }
            operateLogEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());

            String accessTerminal = request.getHeader("Access-Terminal");
            if(null != accessTerminal){
                operateLogEntity.setAccessTerminal(Integer.parseInt(accessTerminal));
            }
            logService.addLog(operateLogEntity);
        } catch (Exception exp) {
            log.error("保存操作日志异常:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    private OperateLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        OperateLog classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), OperateLog.class);

        if (method != null) {
            return classAnnotation;
        }
        return null;
    }

    /**
     * swagger API
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private Api getApi(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Api classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Api.class);

        if (method != null) {
            return classAnnotation;
        }
        return null;
    }

    /**
     * swagger ApiOperation
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private ApiOperation getApiOperation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(ApiOperation.class);
        }
        return null;
    }

}
