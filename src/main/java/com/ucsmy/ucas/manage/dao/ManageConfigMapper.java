package com.ucsmy.ucas.manage.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ucsmy.component.mybatis.page.PageRequest;
import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.manage.entity.ManageConfig;

/**
 * Created by ucs_panwenbo on 2017/4/14.
 */
@Mapper
public interface ManageConfigMapper {
	PageResult<ManageConfig> queryConfig(@Param("paramKey") String  paramKey, PageRequest pageRequest);

    int addConfig(ManageConfig manageConfig);

    int editConfig(ManageConfig manageConfig);

    int deleteConfig(String id);

    ManageConfig getConfig(String id);

    ManageConfig queryByName(String paramKey);

    int isKeyExist(Map<String, Object> map);
}
