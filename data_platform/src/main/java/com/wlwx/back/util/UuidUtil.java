package com.wlwx.back.util;

import java.util.UUID;

/**
 * UUID工具类
 * @author zjj
 * @date 2017年7月12日 上午9:07:05
 */
public class UuidUtil {
	
	/**
	 * 获取32位uuid
	 * @date 2017年7月12日 上午9:06:52
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}