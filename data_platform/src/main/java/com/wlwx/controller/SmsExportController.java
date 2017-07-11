package com.wlwx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wlwx.back.system.SystemInit;
import com.wlwx.back.task.ControlTask;
import com.wlwx.back.task.Task;
import com.wlwx.back.util.ResultMsg;
import com.wlwx.service.SmsExportService;
import com.wlwx.service.UserService;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据导出
 * @author zjj
 * @date 2017年7月11日 下午5:07:42
 */
@RestController
@RequestMapping("/sms/export")
public class SmsExportController {
	private static final Logger LOGGER = Logger.getLogger(SmsExportService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SmsExportService smsExportService;

	/**
	 * 创建数据导出任务
	 * @date 2017年7月11日 下午5:07:50
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/createTask.json"}, method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> createTask(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			String params = request.getParameter("params");

			Map<String, Object> map = JSON.parseObject(params,
					new TypeReference<Map<String, Object>>() {
					});

			String customInfo = (String) map.get("custom_info");
			ResultMsg resultMsg = userService.checkCustom(customInfo);

			if (resultMsg.isSuccess()) {
				map.put("user_info", resultMsg.getObj());
				ResultMsg resMsg = smsExportService.createTask(map);
				if (resMsg.isSuccess()) {
					result.put("success", true);
					result.put("message", "创建任务成功");
				} else {
					result.put("success", false);
					result.put("message", resMsg.getMsg());
				}
			} else {
				result.put("success", false);
				result.put("message", resultMsg.getMsg());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "创建任务失败");
		}
		return result;
	}

	/**
	 * 取消任务
	 * @date 2017年7月11日 下午5:08:00
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value = { "/cancelTask.json" }, method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> cancelTask(String taskId) {
		Map<String, Object> result = new HashMap<>();
		try {
			ControlTask.cancelTask(taskId);
			result.put("success", Boolean.valueOf(true));
			result.put("message", "取消任务成功");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.put("success", Boolean.valueOf(false));
			result.put("message", "取消任务失败");
		}
		return result;
	}

	
	/**
	 * 测试使用
	 * @date 2017年7月11日 下午5:08:18
	 * @return
	 */
	@RequestMapping(value = { "/runThreadNum" }, method = RequestMethod.POST)
	public @ResponseBody String getRunThreadNum() {
		StringBuffer sb = new StringBuffer();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		
		List<Task> queue = SystemInit.taskService.getTaskQueue().getQueue();
		for (int i = 0; i < queue.size(); i++) {
			sb.append("任务ID：" + ((Task) queue.get(i)).getTask_id() + " | ");
			sb.append("任务状态：" + (((Task) queue.get(i)).getState() == 1 ? "新建" : "运行")+ " | ");
			sb.append("优先级：" + ((Task) queue.get(i)).getPriority() + " | ");
			sb.append("创建时间：" + sdf.format(((Task) queue.get(i)).getCreate_time()) + " | ");
			sb.append("模板ID：" + ((Task) queue.get(i)).getModel_id() + " | ");
			sb.append("用户ID：" + ((Task) queue.get(i)).getUser_id() + "\n");
		}
		return sb.toString();
	}
}