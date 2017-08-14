package com.ucsmy.ucas.manage.service;

import com.ucsmy.component.mybatis.page.PageResult;
import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.manage.entity.ManageUserAccount;
import com.ucsmy.ucas.manage.entity.ManageUserProfile;
import com.ucsmy.ucas.manage.ext.UserProfilePojo;

public interface ManageUserProfileService {

	PageResult<ManageUserProfile> queryUserProfile(String name, int pageNum, int pageSize);

	UserProfilePojo queryUserProfileByUserId(String userId);
	
	ManageUserProfile queryUserProfileByLoginName(String loginName);
	
	int chenckUserProfileByMobilephone(String mobilephone);
	
	int chenckUserProfileByEmail(String email);
	
	int saveUserProfile(ManageUserProfile manageUserProfile);
	
	int updateUserProfile(ManageUserProfile manageUserProfile);
	
	int deleteUserProfile(String userId);

	AosResult add(ManageUserProfile profile, ManageUserAccount userAccount);

	AosResult update(ManageUserProfile profile, ManageUserAccount userAccount);

	AosResult delete(String userId);

	}
