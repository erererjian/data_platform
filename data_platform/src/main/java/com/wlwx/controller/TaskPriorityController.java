package com.wlwx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wlwx.back.system.SystemInit;
import com.wlwx.back.task.Task;
import com.wlwx.back.util.ResultMsg;
import com.wlwx.model.TaskInfo;
import com.wlwx.service.TaskService;
import com.wlwx.service.UserService;

/**
 * 任务优先级
 * @author zjj
 * @date 2017年7月12日 下午2:55:55
 */
@Controller
@RequestMapping("/sms/priority")
public class TaskPriorityController {
	
	private static final Logger LOGGER = Logger.getLogger(TaskPriorityController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	
	/**
	 * 查询队列中的任务
	 * @date 2017年7月12日 下午3:20:23
	 * @return
	 */
	@RequestMapping(value = {"/listTasks.json"}, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> listTasks(HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			String params = request.getParameter("params");

			Map<String, Object> map = JSON.parseObject(params,
					new TypeReference<Map<String, Object>>() {
					});

			String customInfo = map.get("custom_info").toString();
			ResultMsg resultMsg = userService.checkCustom(customInfo);//验证用户信息

			if (resultMsg.isSuccess()) {
				
				int taskStatus = TaskInfo.NEW;//新建或等待
				if (map.get("task_status") != null) taskStatus = (int) map.get("task_status");
				
				List<Task> tasks = SystemInit.taskService.getTaskQueue().getQueue();
				List<TaskInfo> taskInfos = new ArrayList<>();
				
				for (int i = 0; i < tasks.size(); i++) {
					if (tasks.get(i).getState() == taskStatus) {//根据任务状态查询任务
						TaskInfo taskInfo = taskService.getById(tasks.get(i).getTask_id());
						taskInfos.add(taskInfo);
					}
				}
				
				result.put("success", true);
				result.put("message", "获取任务列表成功");
				result.put("data", taskInfos);
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
	
	
	/**
	 * 调整任务优先级
	 * @date 2017年7月13日 上午8:47:59
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"/moveTask.json"}, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> moveTask(HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		try {
			String params = request.getParameter("params");

			Map<String, Object> map = JSON.parseObject(params,
					new TypeReference<Map<String, Object>>() {
					});

			String customInfo = map.get("custom_info").toString();
			ResultMsg resultMsg = userService.checkCustom(customInfo);//验证用户信息

			if (resultMsg.isSuccess()) {
				if (map.get("source_task_id") != null 
						&& map.get("dest_task_id") != null) {//判断源目标位置与任务位置不得为空
					String sourceTaskId = map.get("source_task_id").toString();
					String destTaskId = map.get("dest_task_id").toString();
					
					int sourceIndex = -1;
					int destIndex = -1;
					
					List<Task> tasks = SystemInit.taskService.getTaskQueue().getQueue();
					
					for (int i = 0; i < tasks.size(); i++) {
						if (TaskInfo.NEW == tasks.get(i).getState()) {
							String taskId = tasks.get(i).getTask_id();
							if (sourceTaskId.equals(taskId)) //获取源任务的位置
								sourceIndex = i;
							if (destTaskId.equals(taskId)) //获取目标任务的位置
								destIndex = i;
						}
					}
					if (sourceIndex != -1 && destIndex != -1) {
						SystemInit.taskService.getTaskQueue().getQueue().add(destIndex,
								SystemInit.taskService.getTaskQueue().getQueue().remove(sourceIndex));
						result.put("success", true);
						result.put("message", "调整任务优先级成功");
					} else {
						result.put("success", false);
						result.put("message", "任务已经正在运行或者完成，不得调整");
					}
				} else {
					result.put("success", false);
					result.put("message", "源任务ID和目标任务ID不得为空");
				}
			} else {
				result.put("success", false);
				result.put("message", resultMsg.getMsg());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result.put("success", false);
			result.put("message", "调整任务优先级失败");
		}
		
		return result;
	}
}