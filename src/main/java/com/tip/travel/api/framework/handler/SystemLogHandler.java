package com.tip.travel.api.framework.handler;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import com.tip.travel.api.framework.annotation.Log;
import com.tip.travel.common.domain.SystemLog;
import com.tip.travel.common.service.SystemLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Aspect
@Component
public class SystemLogHandler {
    private static Logger logger = LoggerFactory.getLogger(SystemLogHandler.class);
    @Reference(version = "1.0.0")
    private SystemLogService systemLogService;


    @Around(value = "@annotation(log)")
    public Object handleSystemLog(ProceedingJoinPoint joinPoint, Log log) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SystemLog systemLog = new SystemLog();
        systemLog.setActionType(log.logType());
        systemLog.setRequestUrl(request.getRequestURI());
        systemLog.setToken(request.getHeader("token"));
        systemLog.setIpAddress(request.getRemoteHost());
        systemLog.setMethodName(joinPoint.getTarget().getClass().getName());
        systemLog.setStartDatetime(new Date());
        long startTime = System.currentTimeMillis();
        String exception = null;
        String inParams = null;
        String outParams = null;
        Object data = null;
        try {
            data = joinPoint.proceed();
            return data;
        } catch (Throwable throwable) {
            logger.warn("request api failed", throwable);
            exception = throwable.getLocalizedMessage();
            throw throwable;
        } finally {
            try {
                inParams = JSON.json(joinPoint.getArgs());
                outParams = JSON.json(data);
                long endTime = System.currentTimeMillis();
                long costTime = endTime -startTime;
                systemLog.setTimeCost(costTime);
                systemLog.setInParams(inParams);
                systemLog.setOutParams(outParams);
                systemLog.setEndDatetime(new Date());
                systemLog.setException(exception);
                systemLogService.saveSystemLog(systemLog);
            } catch (Exception e){
                logger.warn("save log failed", e);
            }
        }
    }
}
