package com.ucsmy.ucas.manage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ucsmy.component.mybatis.page.PageRequest;
import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.manage.entity.ManageUserProfile;
import com.ucsmy.ucas.manage.ext.UserProfilePojo;

@Mapper
public interface ManageUserProfileMapper {
	
	PageResult<ManageUserProfile> queryUserProfile(@Param("name") String name, PageRequest pageRequest);
	
	UserProfilePojo queryUserProfileByUserId(@Param("userId") String userId);
	
	ManageUserProfile queryUserProfileByLoginName(@Param("loginName") String loginName);
	
	int chenckUserProfileByMobilephone(@Param("mobilephone") String mobilephone);
	
	int chenckUserProfileByEmail(@Param("email") String email);
	
	int saveUserProfile( ManageUserProfile manageUserProfile);
	
	int updateUserProfile(ManageUserProfile manageUserProfile);
	
	int deleteUserProfile(@Param("userId") String userId);
}
