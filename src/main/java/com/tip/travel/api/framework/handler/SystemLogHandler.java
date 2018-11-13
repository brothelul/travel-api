package com.tip.travel.api.framework.handler;

import com.alibaba.dubbo.common.json.JSON;
import com.tip.travel.api.framework.annotation.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Aspect
@Component
public class SystemLogHandler {
    private static Logger logger = LoggerFactory.getLogger(SystemLogHandler.class);

    @Before(value = "@annotation(log)")
    public void beforeHandleSystemLog(JoinPoint joinPoint, Log log){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        request.setAttribute("startTime", System.currentTimeMillis());
    }

    @AfterReturning(value = "@annotation(log)", returning = "result")
    public void handleSystemLog(JoinPoint joinPoint, Log log, Object result){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        long startTime = (long) request.getAttribute("startTime");
        logger.info("============================= start aop ===============================");
        try {
            String json = JSON.json(result);
            Object[] args = joinPoint.getArgs();
            String in = JSON.json(args);
            logger.info("inParams {}", in);
            logger.info("time {} ms", System.currentTimeMillis() - startTime);
            logger.info("outParams {}", json);
            logger.info("request url {}", request.getRequestURL());
            logger.info("ip {}", request.getRemoteAddr());
        } catch (IOException e) {
            e.printStackTrace();
        }

        joinPoint.getArgs();
        logger.info("============================= end aop ===============================");

    }
}
