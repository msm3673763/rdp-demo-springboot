package com.ucsmy.ucas.manage.service;

import java.util.List;
import java.util.Map;

import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.manage.entity.ManageOrganization;
import com.ucsmy.ucas.manage.ext.UcasClientOrganizationUser;
import com.ucsmy.ucas.manage.ext.UcasClientUserProfileWithOrganization;

/**
 * Created by ucs_panwenbo on 2017/4/14.
 */
public interface ManageOrganizationService {

     List<ManageOrganization> getOrganizationList();

     ManageOrganization getOrganizationById(String id);

     int updateOrganization(ManageOrganization manageOrganization);

     int insertOrganization(ManageOrganization manageOrganization);

     int deleteOrganization(String id);

     int countChildren(String id);

     int deleteBatchByUserIds(Map<String,Object> map);

     int deleteBatch(Map<String,Object> map);

     int insertBatch( List<UcasClientOrganizationUser> list);

     PageResult<UcasClientUserProfileWithOrganization> queryUserWithOrganization(String id, int page, int size);

     PageResult<UcasClientUserProfileWithOrganization> queryUserWithoutOrganization(String id, int page, int size);

}
