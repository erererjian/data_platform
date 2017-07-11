package com.wlwx.dao;

import com.wlwx.model.UserInfo;
import java.util.Map;

public abstract interface UserInfoMapper
{
  public abstract UserInfo get(Map<String, Object> paramMap);
}