package com.ljzh.samplecollection.framwork.annotation;

import java.lang.annotation.*;

/***
 * @title Page
 * @description 分页注解
 * @author rubby
 * @version 1.0.0
 * @create 2023/06/09 下午 02:09
 **/

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyPage {

    int pageNum() default 1;

    int pageSize() default 10;
}
