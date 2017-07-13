package com.wlwx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wlwx.back.util.AesUtil;
import com.wlwx.back.util.MD5Util;
import com.wlwx.back.util.ResultMsg;
import com.wlwx.dao.AzUserInfoMapper;
import com.wlwx.dao.UserInfoMapper;
import com.wlwx.model.AzUserInfo;
import com.wlwx.model.UserInfo;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {
	private static final Logger LOGGER = Logger.getLogger(UserService.class);

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private AzUserInfoMapper azUserInfoMapper;
	
	/**
	 * 根据用户ID获取用户信息
	 * @date 2017年7月13日 下午4:46:06
	 * @param UserId
	 * @return
	 */
	public UserInfo getById(String UserId){
		return userInfoMapper.getById(UserId);
	}
	
	/**
	 * 获取az用户
	 * @date 2017年7月13日 下午4:42:23
	 * @param azUserId
	 * @return
	 */
	public AzUserInfo getAzUser(String azUserId){
		return azUserInfoMapper.get(azUserId);
	}

	/**
	 * 验证用户信息
	 * @date 2017年7月12日 上午11:46:16
	 * @param customInfo
	 * @return
	 */
	public ResultMsg checkCustom(String customInfo) {
		ResultMsg resultMsg = null;
		try {
			String decryptCustomInfo = AesUtil.decrypt(customInfo);
			Map<String, Object> customInfoMap = JSON.parseObject(
					decryptCustomInfo,
					new TypeReference<Map<String, Object>>() {
					});

			int customUid = ((Integer) customInfoMap.get("custom_uid")).intValue();
			int customSource = ((Integer) customInfoMap.get("custom_source")).intValue();
			String userPassword = (String) customInfoMap.get("user_password");
			Map<String, Object> params = new HashMap<>();
			params.put("custom_uid", Integer.valueOf(customUid));
			params.put("custom_source", Integer.valueOf(customSource));
			UserInfo userInfo = this.userInfoMapper.get(params);
			if (userInfo != null) {
				String dbPassword = userInfo.getUser_password();
				if (MD5Util.validPasswd(userPassword, dbPassword))
					resultMsg = new ResultMsg(true, "用户验证通过", userInfo);
				else
					resultMsg = new ResultMsg(false, "该用户密码有误");
			} else {
				resultMsg = new ResultMsg(false, "该用户不存在");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMsg = new ResultMsg(false, "用户验证失败");
		}

		return resultMsg;
	}
}