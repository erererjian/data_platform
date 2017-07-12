package com.wlwx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageInfo;
import com.wlwx.back.util.ResultMsg;
import com.wlwx.model.TaskInfo;
import com.wlwx.service.TaskService;
import com.wlwx.service.UserService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 任务列表
 * @author zjj
 * @date 2017年7月11日 下午5:08:29
 */
@Controller
@RequestMapping({ "/sms/manage" })
public class TaskController {
	private static final Logger LOGGER = Logger.getLogger(TaskController.class);

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;

	/**
	 * 查询任务列表
	 * @date 2017年7月12日 上午11:23:49
	 * @return
	 */
	@RequestMapping(value = {"/listTasks.json"}, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> listTasks(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			String params = request.getParameter("params");

			Map<String, Object> map = JSON.parseObject(params,
					new TypeReference<Map<String, Object>>() {
					});

			String customInfo = map.get("custom_info").toString();
			ResultMsg resultMsg = userService.checkCustom(customInfo);//验证用户信息

			if (resultMsg.isSuccess()) {
				PageInfo<TaskInfo> pageInfo = taskService.listTasks(map);
				result.put("success", true);
				result.put("message", "获取任务列表成功");
				result.put("pageInfo", pageInfo);
			} else {
				result.put("success", false);
				result.put("message", resultMsg.getMsg());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "获取任务列表失败");
		}
		return result;
	}
}