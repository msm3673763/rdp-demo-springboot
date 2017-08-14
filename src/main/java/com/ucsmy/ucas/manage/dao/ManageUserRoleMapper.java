package com.ucsmy.ucas.manage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ucsmy.component.mybatis.page.PageRequest;
import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.manage.entity.ManageUserRole;

@Mapper
public interface ManageUserRoleMapper {

	PageResult<ManageUserRole> queryUserRoleList(@Param("roleId") String roleId,
                                                   @Param("account") String account, PageRequest pageRequest);

	PageResult<ManageUserRole> queryUnbindUserList(@Param("roleId") String roleId,
                                                     @Param("account") String account, PageRequest pageRequest);

	Long queryUserCountByUserIds(@Param("ids") String[] ids);

	int insertUserRole(ManageUserRole manageUserRole);

	int deleteUserRoleByUserIds(@Param("ids") String[] ids);

	int deleteUserRoleByIds(String id);
	
	int deleteUserRoles(@Param("ids") String[] ids);
}
