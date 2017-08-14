package com.ucsmy.ucas.manage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ucsmy.component.mybatis.page.PageRequest;
import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.manage.entity.ManageOrganization;
import com.ucsmy.ucas.manage.ext.UcasClientOrganizationUser;
import com.ucsmy.ucas.manage.ext.UcasClientUserProfileWithOrganization;

/**
 * Created by ucs_panwenbo on 2017/4/14.
 */
@Mapper
public interface ManageOrganizationMapper {

    List<ManageOrganization> getOrganizationList();

    ManageOrganization getOrganizationById(String id);

    int updateOrganization(ManageOrganization manageOrganization);

    int insertOrganization(ManageOrganization manageOrganization);

    int deleteOrganization(String id);

    int countChildren(String id);

    int deleteBatchByUserIds(Map<String, Object> map);

    int insertBatch(List<UcasClientOrganizationUser> list);

    int deleteBatch(Map<String, Object> map);

    PageResult<UcasClientUserProfileWithOrganization> queryUserWithOrganization(@Param("id")String id, PageRequest pageRequest);

    PageResult<UcasClientUserProfileWithOrganization> queryUserWithoutOrganization(@Param("id")String id, PageRequest pageRequest);

}
