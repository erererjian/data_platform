package com.wlwx.back.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlwx.azkaban.AzkabanUtil;
import com.wlwx.back.system.SystemInit;
import com.wlwx.model.AzUserInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 平台工具类
 * @author zjj
 * @date 2017年7月12日 上午9:07:27
 */
public class PlatformUtil {
	
	public static final String FILE_URL = File.separator + "home"
			+ File.separator + "zhengjj";

	/**
	 * 任务状态转换
	 * @date 2017年7月12日 上午10:39:10
	 * @param taskStatus
	 * @return
	 */
	public static String statusToStr(int taskStatus){
		String statusDesc;
		switch (taskStatus) {
		case 1:
			statusDesc = "新建";
			break;
		case 2:
			statusDesc = "等待";
			break;
		case 3:
			statusDesc = "正在运行";
			break;
		case 4:
			statusDesc = "终止";
			break;
		default:
			statusDesc = "完成";
			break;
		}
		return statusDesc;
	}
	
	/**
	 * 将map转成json字符串
	 * @date 2017年7月12日 上午9:07:54
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map<String, Object> map) {
		return JSON.toJSONString(map);
	}

	/**
	 * 获取两个时间的时间差
	 * @date 2017年7月12日 上午9:08:14
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String getDistanceTime(Date begin, Date end) throws Exception{
		if (begin != null && end != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Date beginFormat = sdf.parse(sdf.format(begin));//忽略毫秒数
			Date endFormat = sdf.parse(sdf.format(end));//忽略毫秒数
			
			long between = (endFormat.getTime() - beginFormat.getTime()) / 1000;// 除以1000是为了转换成秒
			
			long day = between / (24 * 3600);
			long hour = between % (24 * 3600) / 3600;
			long minute = between % 3600 / 60;
			long second = between % 60;
			return day + "天" + hour + "小时" + minute + "分" + second + "秒";
		} else 
			return "";
	}
	
	/**
	 * 重置userSession
	 * @date 2017年7月12日 上午10:09:19
	 * @param azUserInfo
	 * @param userId
	 */
	public synchronized static void resetSession(AzUserInfo azUserInfo,String userId){
		JSONObject jo = AzkabanUtil.actionLogin(azUserInfo.getAz_user_name(),//重新登录
				AesUtil.decrypt(azUserInfo.getAz_user_password()));
		if ((jo.get("error") == null) && ("success".equals(jo.getString("status")))) {//设置新的sessionID
			SystemInit.sessionMap.put(userId,jo.getString("session.id"));
		}
	}
	
	public static void main(String[] args) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date begin = sdf.parse("2017-07-12 09:14:00:111");
		Date end = sdf.parse("2017-07-12 09:15:03:173");
		System.out.println(getDistanceTime(begin, end));
	}

}