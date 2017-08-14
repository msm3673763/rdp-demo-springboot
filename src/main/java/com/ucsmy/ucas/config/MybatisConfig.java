package com.ucsmy.ucas.config;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ucsmy.ucas.commons.operation.log.MybatisSqlLogInterceptor;

/**
 * Created by xiaojianyuan on 2017.7.20.
 */
@Configuration
public class MybatisConfig {
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@PostConstruct
	public void addMybatisInterceptors(){
		sqlSessionFactory.getConfiguration().addInterceptor(new MybatisSqlLogInterceptor());
	}
}
