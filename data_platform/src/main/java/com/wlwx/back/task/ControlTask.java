package com.wlwx.back.task;

import com.alibaba.fastjson.JSONObject;
import com.wlwx.azkaban.AzkabanUtil;
import com.wlwx.back.system.SystemInit;
import com.wlwx.back.util.PlatformUtil;
import com.wlwx.back.util.ResultMsg;
import com.wlwx.model.AzUserInfo;
import com.wlwx.model.TaskInfo;

import org.apache.log4j.Logger;

/**
 * 任务控制类
 * @author zjj
 * @date 2017年7月11日 下午4:54:50
 */
public class ControlTask {
	public static final Logger LOGGER = Logger.getLogger(ControlTask.class);

	private ControlTask(){
		super();
	}
	/**
	 * 开始任务
	 * @date 2017年7月11日 下午4:54:58
	 * @param exportTask
	 * @return
	 */
	public static ResultMsg startTask(ExportTask exportTask) {
		int taskFlag = curTask(exportTask);
		ResultMsg resultMsg;
		if (taskFlag == -1)
			resultMsg = new ResultMsg(true, "添加成功，马上执行");
		else if (taskFlag == -2)
			resultMsg = new ResultMsg(true, "添加成功，等待执行！");
		else 
			resultMsg = new ResultMsg(false, "添加失败，任务已存在");
		return resultMsg;
	}

	public static int curTask(ExportTask et) {
		int startStatus;
		ExportTask exportTask;
		exportTask = getTask(et.getTask_id());
		if (exportTask != null) {
			return exportTask.getState();
		}
		startStatus = SystemInit.taskService.runTask(et);
		return startStatus;
	}

	/**
	 * 取消任务
	 * @date 2017年7月11日 下午4:55:09
	 * @param taskId
	 */
	public static ResultMsg cancelTask(String taskId) {
		ResultMsg resultMsg = null;
		ExportTask exportTask = getTask(taskId);
		if (exportTask != null) {
			if (exportTask.getState() == TaskInfo.NEW){//如果任务未开始，直接移除
				SystemInit.taskService.getTaskQueue().finishTask(exportTask);
				resultMsg = new ResultMsg(true, "取消队列中的任务成功");
			} else if (exportTask.getState() == TaskInfo.RUNNING) {//任务已经开始需要调用azkaban取消任务接口
				/**
				 * 执行取消任务命令
				 */
				JSONObject jsonObject = AzkabanUtil.cancelFlow(
						SystemInit.sessionMap.get(exportTask.getUser_id()).toString(),
						exportTask.getExec_id()+"");
				
				if (jsonObject.get("error") != null) {//执行取消任务失败
					if ("session".equals(jsonObject.getString("error"))){//如果是因为session原因错误，重新登录后再次执行
						AzUserInfo azUserInfo = exportTask.getAzUserInfo();
						PlatformUtil.resetSession(azUserInfo, exportTask.getUser_id());//重置sessionID
						jsonObject = AzkabanUtil.cancelFlow(//重置session后再次取消任务
								SystemInit.sessionMap.get(exportTask.getUser_id()).toString(),
								exportTask.getExec_id()+"");
						if (jsonObject.get("error") != null) {//再次执行取消任务失败
							resultMsg = new ResultMsg(false, "取消任务失败【登录异常】");
						} else {
							SystemInit.taskService.getTaskQueue().setTaskState(taskId, TaskInfo.TERMINATED);
							resultMsg = new ResultMsg(true, "取消队列中的任务成功");
						}
					} else {
						resultMsg = new ResultMsg(false, "取消任务失败【非登录异常】");
					}
				}else {
					SystemInit.taskService.getTaskQueue().setTaskState(taskId, TaskInfo.TERMINATED);
					resultMsg = new ResultMsg(true, "取消队列中的任务成功");
				}
			}
		} else {
			resultMsg = new ResultMsg(false, "该任务在队列中不存在");
		}
		return resultMsg;
	}
	
	/**
	 * 获取一个队列中的任务任务
	 * @date 2017年7月11日 下午4:56:17
	 * @param taskId
	 * @return
	 */
	public static ExportTask getTask(String taskId) {
		ExportTask exportTask;
		exportTask = (ExportTask) SystemInit.taskService.getTaskQueue()
			.getTaskById(taskId);
		return exportTask;
	}

}