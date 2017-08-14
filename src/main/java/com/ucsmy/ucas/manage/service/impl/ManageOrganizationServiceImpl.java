package com.ucsmy.ucas.manage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucsmy.component.mybatis.page.PageRequest;
import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.commons.operation.log.Logger;
import com.ucsmy.ucas.manage.dao.ManageOrganizationMapper;
import com.ucsmy.ucas.manage.entity.ManageOrganization;
import com.ucsmy.ucas.manage.ext.UcasClientOrganizationUser;
import com.ucsmy.ucas.manage.ext.UcasClientUserProfileWithOrganization;
import com.ucsmy.ucas.manage.service.ManageOrganizationService;

/**
 * Created by ucs_panwenbo on 2017/4/14.
 */
@Service
public class ManageOrganizationServiceImpl implements ManageOrganizationService {
    @Autowired
    ManageOrganizationMapper manageOrganizationMapper;

    @Override
    // 查询操作不记录日志
    //@Logger(printSQL = true)
    public List<ManageOrganization> getOrganizationList() {
        return manageOrganizationMapper.getOrganizationList();
    }

    @Override
    // 查询操作不记录日志
    //@Logger(printSQL = true)
    public ManageOrganization getOrganizationById(String id) {
        return manageOrganizationMapper.getOrganizationById(id);
    }

    @Override
    @Logger(printSQL = true)
    public int updateOrganization(ManageOrganization manageOrganization) {
        return manageOrganizationMapper.updateOrganization(manageOrganization);
    }

    @Override
    @Logger(printSQL = true)
    public int insertOrganization(ManageOrganization manageOrganization) {
        return manageOrganizationMapper.insertOrganization(manageOrganization);
    }

    @Override
    @Logger(printSQL = true)
    public int deleteOrganization(String id) {
        return manageOrganizationMapper.deleteOrganization(id);
    }

    @Override
    // 查询操作不记录日志
    //@Logger(printSQL = true)
    public int countChildren(String id) {
        return manageOrganizationMapper.countChildren(id);
    }

    @Override
    @Logger(printSQL = true)
    public int deleteBatchByUserIds(Map<String, Object> map) {
        return manageOrganizationMapper.deleteBatchByUserIds(map);
    }

    @Override
    @Logger(printSQL = true)
    public int insertBatch(List<UcasClientOrganizationUser> list) {
        return manageOrganizationMapper.insertBatch(list);
    }

    @Override
    @Logger(printSQL = true)
    public int deleteBatch(Map<String, Object> map) {
        return manageOrganizationMapper.deleteBatch(map);
    }

    @Override
    // 查询操作不记录日志
    //@Logger(printSQL = true)
    public PageResult<UcasClientUserProfileWithOrganization> queryUserWithOrganization(String id, int page, int size) {
        return manageOrganizationMapper.queryUserWithOrganization(id, new PageRequest(page,size));
    }

    @Override
    // 查询操作不记录日志
    //@Logger(printSQL = true)
    public PageResult<UcasClientUserProfileWithOrganization> queryUserWithoutOrganization(String id, int page, int size) {
        return manageOrganizationMapper.queryUserWithoutOrganization(id, new PageRequest(page,size));
    }
}
