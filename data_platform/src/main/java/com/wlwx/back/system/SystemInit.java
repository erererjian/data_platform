package com.wlwx.back.system;

import com.wlwx.application.SpringContextUtil;
import com.wlwx.azkaban.AzkabanUtil;
import com.wlwx.back.task.ControlTask;
import com.wlwx.back.task.ExportTask;
import com.wlwx.back.task.ThreadPoolService;
import com.wlwx.back.util.PlatformUtil;
import com.wlwx.model.AzUserInfo;
import com.wlwx.model.ModelInfo;
import com.wlwx.model.TaskInfo;
import com.wlwx.model.UserInfo;
import com.wlwx.service.TaskService;
import com.wlwx.service.UserService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * 系统初始化类
 * @author zjj
 * @date 2017年7月11日 下午4:54:05
 */
public class SystemInit {
	public static final Logger LOGGER = Logger.getLogger(SystemInit.class);

	public static Map<String, Object> sessionMap = new ConcurrentHashMap<>();
	public static ThreadPoolService taskService;

	/**
	 * 开启任务线程
	 * @date 2017年7月11日 下午4:54:15
	 */
	public static synchronized void start() {
		try {
			//启动任务线程
			taskService = new ThreadPoolService(3);
			taskService.start();
			
			//恢复未完成任务
			resumeTasks();
			
		} catch (Exception e) {
			LOGGER.error("SystemInit start() error", e);
		}
	}

	/**
	 * 关闭任务线程
	 * @date 2017年7月11日 下午4:54:27
	 */
	public static synchronized void stop() {
		try {
			taskService.stop(true);
		} catch (Exception e) {
			LOGGER.error("SystemInit stop() error", e);
		}
	}
	
	/**
	 * 恢复数据库中未完成的任务
	 * @authod zjj
	 * @date 2017年7月13日 下午4:51:37
	 */
	private static void resumeTasks(){
		
		TaskService tService = (TaskService) SpringContextUtil.getBean("taskService");
		UserService uService = (UserService) SpringContextUtil.getBean("userService");
		
		List<TaskInfo> taskInfos = tService.getNotFinishTasks();//查找数据库未完成的任务
		
		for (TaskInfo taskInfo : taskInfos) {
			UserInfo userInfo = uService.getById(taskInfo.getUser_id());
			
			/**
			 * 创建任务队列的任务信息
			 */
			ExportTask exportTask = new ExportTask();
			
			exportTask.setTask_id(taskInfo.getTask_id());
			exportTask.setUser_id(taskInfo.getUser_id());
			exportTask.setCreate_time(taskInfo.getCreate_time());
			exportTask.setStart_time(taskInfo.getStart_time());
			exportTask.setPriority(taskInfo.getPriority());
			exportTask.setModel_id(taskInfo.getModel_id());
			exportTask.setState(taskInfo.getTask_status());
			exportTask.setTaskService(tService);
			exportTask.setExec_id(taskInfo.getExec_id());
			exportTask.setNew(false);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			System.out.println("创建时间" + sdf.format(taskInfo.getCreate_time()));
			Map<String, Object> taskParam = PlatformUtil.jsonToMap(taskInfo.getTask_param());
			taskParam.put("user_id", userInfo.getUser_id());
			taskParam.put("user_source", userInfo.getCustom_source());
			taskParam.put("create_time", sdf.format(taskInfo.getCreate_time()));
			taskParam.put("useExecutor", AzkabanUtil.HADOOP03);
			taskParam.put("task_id", taskInfo.getTask_id());
			exportTask.setTask_param(taskParam);

			AzUserInfo azUserInfo = uService.getAzUser(userInfo.getAz_user_id());
			if (azUserInfo != null) {
				exportTask.setAzUserInfo(azUserInfo);
				ModelInfo modelInfo = tService.getModelById(taskInfo.getModel_id());

				if (modelInfo != null) {
					exportTask.setModelInfo(modelInfo);
					ControlTask.startTask(exportTask);
				}
			}			
		}
	}
}