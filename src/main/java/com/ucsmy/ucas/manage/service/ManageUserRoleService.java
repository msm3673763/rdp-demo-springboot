package com.ucsmy.ucas.manage.service;

import java.util.Map;

import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.manage.entity.ManageUserRole;

public interface ManageUserRoleService {
	
	PageResult<ManageUserRole> queryUserRoleList(String roleId, String account, int pageNum, int pageSize);
	
	PageResult<ManageUserRole> queryUnbindUserList(String roleId, String account, int pageNum, int pageSize);
	
//	int queryUserCountByUserIds(Map<String, Object> map);	
	
	int insertUserRole(ManageUserRole manageUserRole);
	
	int deleteUserRoleByUserIds(String[] ids);
	
	int deleteUserRoleByIds(String id);

	AosResult insertUserRole(String roleId, String userIds);
	
	AosResult deleteUserRoles(String ids);
	
}
