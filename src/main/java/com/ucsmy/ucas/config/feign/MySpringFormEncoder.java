package com.ucsmy.ucas.config.feign;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/6/19

 * Contributors:
 *      - initial implementation
 */

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.form.FormEncoder;
import feign.form.spring.SpringMultipartEncodedDataProcessor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * 处理 MyltipartFile 类型body
 *
 * @author ucs_leijinming
 * @since 2017/6/19
 */

public class MySpringFormEncoder extends FormEncoder {

    private final SpringEncoder delegate;

    public MySpringFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this(new SpringEncoder(messageConverters));
    }

    public MySpringFormEncoder(SpringEncoder delegate) {
        this.delegate = delegate;
    }

    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if(!bodyType.equals(MultipartFile.class)) {
            this.delegate.encode(object, bodyType, template);
        } else {
            MultipartFile file = (MultipartFile)object;
            Map data = Collections.singletonMap(file.getName(), object);
            (new SpringMultipartEncodedDataProcessor()).process(data, template);
        }
    }
}