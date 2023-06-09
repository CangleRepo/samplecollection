package com.ljzh.samplecollection.framwork.aspect;

import com.ljzh.samplecollection.framwork.annotation.VerifyPage;
import com.ljzh.samplecollection.framwork.constant.ResponseEnum;
import com.ljzh.samplecollection.framwork.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/***
 * @title PageAround
 * @description 校验分页项
 * @author rubby
 * @version 1.0.0
 * @create 2023/06/09 下午 02:15
 **/
@Log4j2
@Component
@Aspect
public class PageAround {

    @Around(value = "@annotation(com.ljzh.samplecollection.framwork.annotation.VerifyPage)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        VerifyPage annotation = method.getAnnotation(VerifyPage.class);
        int pageNumIndex = getKeyIndex(parameters, "pageNum");
        int pageSizeIndex = getKeyIndex(parameters, "pageSize");
        int pageNum = 0;
        int pageSize = 0;
        if (ArrayUtils.isEmpty(args)) {
            throw new CustomException(ResponseEnum.PARAM_ERROR);
        } else {
            if (pageNumIndex != -1 && pageSizeIndex != -1) {
                pageNum = (int) args[pageNumIndex];
                pageSize = (int) args[pageSizeIndex];
                if (pageNum < 1) {
                    pageNum = annotation.pageNum();
                }
                if (pageSize < 1) {
                    pageSize = annotation.pageSize();
                }
                args[pageNumIndex] = pageNum;
                args[pageSizeIndex] = pageSize;
                return joinPoint.proceed(args);
            } else {
                throw new CustomException(ResponseEnum.PARAM_ERROR);
            }
        }
    }


    private int getKeyIndex(Parameter[] parameters, String key) {
        if (parameters == null || key == null) {
            return -1;
        }
        for (int i = 0; i < parameters.length; i++) {
            if (key.equals(parameters[i].getName())) {
                return i;
            }
        }
        return -1;
    }
}
