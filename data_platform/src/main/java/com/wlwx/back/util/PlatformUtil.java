package com.wlwx.back.util;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.util.Date;
import java.util.Map;

public class PlatformUtil {
	public static final String FILE_URL = File.separator + "home"
			+ File.separator + "zhengjj";

	public static String mapToJson(Map<String, Object> map) {
		return JSON.toJSONString(map);
	}

	public static String getDistanceTime(Date one, Date two) {
		long day = 0L;
		long hour = 0L;
		long min = 0L;
		long sec = 0L;

		long time1 = one.getTime();
		long time2 = two.getTime();
		long diff;
		if (time1 < time2)
			diff = time2 - time1;
		else {
			diff = time1 - time2;
		}
		day = diff / 86400000L;
		hour = diff / 3600000L - day * 24L;
		min = diff / 60000L - day * 24L * 60L - hour * 60L;
		sec = diff / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min
				* 60L;
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}

}