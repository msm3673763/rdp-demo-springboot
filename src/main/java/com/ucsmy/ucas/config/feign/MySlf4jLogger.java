package com.ucsmy.ucas.config.feign;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/24

 * Contributors:
 *      - initial implementation
 */

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * 自定义Feign Logger，过滤Content-Type为multipart/form-data的请求log
 *
 * @author chenqilin
 * @since 2017/7/24
 */

public class MySlf4jLogger extends Logger {

    private final static String MULTIPART_REQUEST = "multipart/form-data";

    private final org.slf4j.Logger logger;

    public MySlf4jLogger() {
        this(Logger.class);
    }

    public MySlf4jLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public MySlf4jLogger(String name) {
        this(LoggerFactory.getLogger(name));
    }

    MySlf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (this.logger.isDebugEnabled() && !isMultipartFileRequest(request)) {
            super.logRequest(configKey, logLevel, request);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        return this.logger.isDebugEnabled() ? super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime) : response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format(methodTag(configKey) + format, args));
        }
    }

    /**
     * 判断请求是否文件上传类型
     *
     * @param request 请求
     */
    private boolean isMultipartFileRequest(Request request) {
        boolean isMultipartRequest = false;
        Iterator bodyLength = request.headers().keySet().iterator();
        String bodyText;
        while(bodyLength.hasNext()) {
            bodyText = (String)bodyLength.next();
            Iterator headers = Util.valuesOrEmpty(request.headers(), bodyText).iterator();
            while(headers.hasNext()) {
                String value = (String) headers.next();
                if (value.contains(MULTIPART_REQUEST)) {
                    isMultipartRequest = true;
                }
            }
        }
        return isMultipartRequest;
    }
}