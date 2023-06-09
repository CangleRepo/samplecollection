package com.ljzh.samplecollection.framwork.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.ljzh.samplecollection.framwork.constant.ResponseEnum;
import com.ljzh.samplecollection.framwork.exception.CustomException;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = Exception.class)
    public BaseResponse handleException(Exception e) {
        log.error("exception :{} ", e.getMessage());
        e.printStackTrace();
        BaseResponse response = new BaseResponse();
        response.setCode(ResponseEnum.SERVICE_INNER_ERROR.code);
        response.setDesc(ResponseEnum.SERVICE_INNER_ERROR.desc);
        return response;
    }

    /**
     * 数据校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        log.warn("exception :{} ", e.getMessage());
        BaseResponse response = new BaseResponse();
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        response.setCode(ResponseEnum.PARAMS_CHECK_ERR.code);
        response.setDesc(message);
        return response;
    }

    @ExceptionHandler(CustomException.class)
    public BaseResponse handleCommonException(CustomException e) {
        e.printStackTrace();
        log.error("CommonException :{} ", e.getResponseEnum().desc);
        return ResponseUtils.getResponseCode(e.getResponseEnum());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public BaseResponse bindJsonException(InvalidFormatException e) {
        e.printStackTrace();
        log.error("CustomDescException :{} ", e.getMessage());
        return ResponseUtils.getErrorResponse(ResponseEnum.JSON_DATABIND.code, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse jsonParseError(HttpMessageNotReadableException e) {
        e.printStackTrace();
        log.error("CustomDescException :{} ", e.getMessage());

        return ResponseUtils.getErrorResponse(ResponseEnum.JSON_DATABIND.code, ResponseEnum.JSON_DATABIND.desc + e.getMessage());

    }

}


