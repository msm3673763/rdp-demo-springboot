package com.ucsmy.ucas.manage.service;

import com.ucsmy.ucas.commons.aop.result.AosResult;
import com.ucsmy.ucas.manage.entity.ManageRole;
import com.ucsmy.ucas.manage.entity.ManageUserAccount;
import com.ucsmy.ucas.manage.entity.ManageUserProfile;

import java.util.HashMap;

public interface ManageUserAccountService {

	ManageUserAccount findByUserName(String userName);

	ManageUserAccount queryUserAccount(HashMap<String, Object> map);

	ManageUserAccount queryUserAccountByUserId(String userId);

	int chenckUserAccountByAccount(String account);

	int saveUserAccount(ManageUserAccount manageUserAccount);

	int updateUserAccount(ManageUserAccount manageUserAccount);

	AosResult updateUserAccountPassword(String userId, String password, String newPassword);

	AosResult updateUserAccountPassword(String userId, String oldPassword ,String password, String newPassword);

	int deleteUserAccount(String userId);

	void updateUserInfo(ManageUserProfile profile, ManageUserAccount userAccount, ManageRole role );

	String  getAccountByOprenid(String openid);

	boolean isBindedOauthByUserId(String username);

}
