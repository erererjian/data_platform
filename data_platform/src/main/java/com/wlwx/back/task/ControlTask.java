package com.wlwx.back.task;

import com.wlwx.back.system.SystemInit;
import com.wlwx.back.util.ResultMsg;
import org.apache.log4j.Logger;

/**
 * 任务控制类
 * @author zjj
 * @date 2017年7月11日 下午4:54:50
 */
public class ControlTask {
	public static final Logger LOGGER = Logger.getLogger(ControlTask.class);

	/**
	 * 开始任务
	 * @date 2017年7月11日 下午4:54:58
	 * @param exportTask
	 * @return
	 */
	public static ResultMsg startTask(ExportTask exportTask) {
		int taskFlag = curTask(exportTask);
		ResultMsg resultMsg = null;
		if (taskFlag == -1)
			resultMsg = new ResultMsg(true, "添加成功，马上执行");
		else if (taskFlag == -2)
			resultMsg = new ResultMsg(true, "添加成功，等待执行！");
		else {
			resultMsg = new ResultMsg(false, "添加失败，任务已存在");
		}
		return resultMsg;
	}

	public static int curTask(ExportTask et) {
		int startStatus = 0;
		ExportTask exportTask = null;
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
	public static void cancelTask(String taskId) {
		ExportTask exportTask = null;
		exportTask = getTask(taskId);
		if (exportTask != null) {
			if (exportTask.getState() == 1)
				SystemInit.taskService.getTaskQueue().finishTask(exportTask);
			else
				SystemInit.taskService.getTaskQueue().setTaskState(taskId, 4);
		}
	}
	
	/**
	 * 获取一个任务
	 * @date 2017年7月11日 下午4:56:17
	 * @param taskId
	 * @return
	 */
	public static ExportTask getTask(String taskId) {
		ExportTask exportTask = null;
		exportTask = (ExportTask) SystemInit.taskService.getTaskQueue()
			.getTaskById(taskId);
		return exportTask;
	}

}