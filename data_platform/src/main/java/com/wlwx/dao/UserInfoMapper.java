package com.wlwx.dao;

import com.wlwx.model.UserInfo;

import java.util.Map;

public abstract interface UserInfoMapper {
	
	public abstract UserInfo get(Map<String, Object> paramMap);

	/**
	 * 根据用户ID获取用户信息
	 * @date 2017年7月13日 下午4:46:16
	 * @param userId
	 * @return
	 */
	public abstract UserInfo getById(String userId);
}