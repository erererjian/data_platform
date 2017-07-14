package com.wlwx.azkaban;

import com.alibaba.fastjson.JSONObject;
import com.wlwx.back.util.PlatformUtil;

import java.util.Map;

import org.springframework.http.HttpMethod;

/**
 * azkaban工具类
 * @author zjj
 * @date 2017年7月11日 下午4:46:50
 */
public class AzkabanUtil {
	public static final String URL = "url";
	public static final String MANAGER_URL = "managerUrl";
	public static final String EXECUTOR_URL = "executorUrl";

	public static final String PREPARING  = "PREPARING";   //准备
	public static final String RUNNING    = "RUNNING";     //运行
	public static final String FAILED     = "FAILED";      //失败
	public static final String CANCELLED  = "CANCELLED";   //取消
	public static final String SUCCEEDED  = "SUCCEEDED";   //成功

	public static final int HADOOP01 = 1;
	public static final int HADOOP03 = 2;

	/**
	 * 用户登录
	 * @date 2017年7月11日 下午4:47:46
	 * @param username
	 * @param password
	 * @return
	 */
	public static JSONObject actionLogin(String username, String password) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(URL), "action=login&username="
				+ username + "&password=" + password, HttpMethod.POST);
		return JSONObject.parseObject(result);
	}

	/**
	 * 创建项目
	 * @date 2017年7月11日 下午4:48:04
	 * @param sessionId
	 * @param name
	 * @param description
	 * @return
	 */
	public static JSONObject createProject(String sessionId, String name,
			String description) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(MANAGER_URL),
				"action=create&session.id=" + sessionId + "&name=" + name
						+ "&description=" + description, HttpMethod.POST);
		return JSONObject.parseObject(result);
	}

	/**
	 * 删除项目
	 * @date 2017年7月11日 下午4:48:20
	 * @param sessionId
	 * @param project
	 * @return
	 */
	public static String deleteProject(String sessionId, String project) {
		return HttpsRequest.httpsRequest(PlatformUtil.getProperties(MANAGER_URL)
				+ "?delete=true&session.id=" + sessionId + "&project="
				+ project, "", HttpMethod.GET);
	}

	/**
	 * 查看项目中的所有工作流
	 * @date 2017年7月11日 下午4:50:44
	 * @param sessionId
	 * @param project
	 * @return
	 */
	public static JSONObject fetchProjectFlows(String sessionId, String project) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(MANAGER_URL)
				+ "?ajax=fetchprojectflows&session.id=" + sessionId
				+ "&project=" + project, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 查询工作流详情
	 * @date 2017年7月11日 下午4:51:23
	 * @param sessionId
	 * @param project
	 * @param flow
	 * @return
	 */
	public static JSONObject fetchFlowGraph(String sessionId, String project,
			String flow) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(MANAGER_URL)
				+ "?ajax=fetchflowgraph&session.id=" + sessionId + "&project="
				+ project + "&flow=" + flow, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 获取工作流的执行历史
	 * @date 2017年7月11日 下午4:51:37
	 * @param sessionId
	 * @param project
	 * @param flow
	 * @param start
	 * @param length
	 * @return
	 */
	public static JSONObject fetchFlowExecutions(String sessionId,
			String project, String flow, String start, String length) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(MANAGER_URL)
				+ "?ajax=fetchFlowExecutions&session.id=" + sessionId
				+ "&project=" + project + "&flow=" + flow + "&start=" + start
				+ "&length=" + length, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 获取正在执行的工作流
	 * @date 2017年7月11日 下午4:52:00
	 * @param sessionId
	 * @param project
	 * @param flow
	 * @return
	 */
	public static JSONObject getRunningExecIds(String sessionId,
			String project, String flow) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(EXECUTOR_URL)
				+ "?ajax=getRunning&session.id=" + sessionId + "&project="
				+ project + "&flow=" + flow, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 执行一个工作流
	 * @date 2017年7月11日 下午4:52:22
	 * @param sessionId
	 * @param project
	 * @param flow
	 * @param params
	 * @return
	 */
	public static JSONObject executeFlow(String sessionId, String project,
			String flow, Map<String, Object> params) {
		String paramStr = "";
		for (String key : params.keySet()) {
			paramStr = paramStr + "&flowOverride[" + key + "]="
					+ params.get(key);
		}
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(EXECUTOR_URL)
				+ "?ajax=executeFlow&session.id=" + sessionId + "&project="
				+ project + "&flow=" + flow + paramStr, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 取消工作流
	 * @date 2017年7月11日 下午4:52:35
	 * @param sessionId
	 * @param execid
	 * @return
	 */
	public static JSONObject cancelFlow(String sessionId, String execid) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(EXECUTOR_URL)
				+ "?ajax=cancelFlow&session.id=" + sessionId + "&execid="
				+ execid, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 暂停一个工作流
	 * @date 2017年7月11日 下午4:52:48
	 * @param sessionId
	 * @param execid
	 * @return
	 */
	public static JSONObject pauseFlow(String sessionId, String execid) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(EXECUTOR_URL)
				+ "?ajax=pauseFlow&session.id=" + sessionId + "&execid="
				+ execid, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 取消一个工作流
	 * @date 2017年7月11日 下午4:52:59
	 * @param sessionId
	 * @param execid
	 * @return
	 */
	public static JSONObject resumeFlow(String sessionId, String execid) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(EXECUTOR_URL)
				+ "?ajax=resumeFlow&session.id=" + sessionId + "&execid="
				+ execid, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}

	/**
	 * 查看一个工作流的信息
	 * @date 2017年7月11日 下午4:53:11
	 * @param sessionId
	 * @param execid
	 * @return
	 */
	public static JSONObject fetchExecFlow(String sessionId, String execid) {
		String result = HttpsRequest.httpsRequest(PlatformUtil.getProperties(EXECUTOR_URL)
				+ "?ajax=fetchexecflow&session.id=" + sessionId + "&execid="
				+ execid, "", HttpMethod.GET);
		return JSONObject.parseObject(result);
	}
}