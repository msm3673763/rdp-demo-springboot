package com.ucsmy.ucas.config.feign;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/24

 * Contributors:
 *      - initial implementation
 */

import feign.Logger;
import org.springframework.cloud.netflix.feign.FeignLoggerFactory;

/**
 * 自定义Feign Logger工厂类
 *
 * @author chenqilin
 * @since 2017/7/24
 */

public class MyFeignLoggerFactory implements FeignLoggerFactory {
    private Logger logger;

    public  MyFeignLoggerFactory() {}

    public MyFeignLoggerFactory(Logger logger) {
        this.logger = logger;
    }

    public Logger create(Class<?> type) {
        return (Logger) (this.logger != null ? this.logger : new MySlf4jLogger(type));
    }
}