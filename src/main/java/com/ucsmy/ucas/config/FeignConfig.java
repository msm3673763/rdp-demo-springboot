package com.ucsmy.ucas.config;

import com.ucsmy.ucas.config.feign.MyFeignLoggerFactory;
import com.ucsmy.ucas.config.feign.MySpringFormEncoder;
import feign.Logger;
import feign.codec.Encoder;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * Feign配置
 * Created by cui on 2017/6/13.
 */
@Configuration
public class FeignConfig {

    /**
     * 自定义LoggerFactory，过滤multipart/form-data请求的日志记录
     */
    @Bean
    public MyFeignLoggerFactory getLoggerFactory() {
        // 不传参，使用MySlf4jLogger
        return new MyFeignLoggerFactory();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Encoder SpringFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters){
        return new MySpringFormEncoder(messageConverters);
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
        sslContext.init(null, new TrustManager[] { tm }, null);
        return new okhttp3.OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool())
                .sslSocketFactory(sslContext.getSocketFactory(), tm)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

}
