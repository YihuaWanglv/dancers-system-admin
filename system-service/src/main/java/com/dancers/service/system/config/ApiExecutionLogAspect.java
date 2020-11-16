package com.dancers.service.system.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

/**
 * 打印每个api请求的信息
 */
@Aspect
@Component
public class ApiExecutionLogAspect {
    private static Logger log = LoggerFactory.getLogger(ApiExecutionLogAspect.class);

    @Around("execution(* com.dancers.service.system.api..*(..))")
    public Object profileExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String apiName = className + "." + methodName;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestId = UUID.randomUUID().toString();
        log.info(">>>请求进入>>>\n REQUESTED_ID: {}\n HOST: {} HTTP_METHOD: {}\n URI: {}\n API: {}\n ARGUMENTS: {}\n",
                requestId,
                request.getHeader("host"),
                request.getMethod(),
                request.getRequestURI(),
                apiName,
                Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("<<<请求完成<<<\nEXECUTION_TIME: {} ms [REQUESTED_ID: {}] [API: {}]", elapsedTime, requestId, apiName);

        return result;
    }
}
