package com.wlwx.back.util;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PlatformUtil {
	public static final SimpleDateFormat SDF_NORMAL = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat SDF_STANDARD = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss:SSS");
	public static final SimpleDateFormat SDF_STRING = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

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

	public static void main(String[] args) throws Exception {
		Date startTime = SDF_NORMAL.parse("2017-07-11 10:26:51");
		Date endTime = SDF_NORMAL.parse("2017-07-11 10:27:57");

		System.out.println(getDistanceTime(startTime, endTime));
	}
}