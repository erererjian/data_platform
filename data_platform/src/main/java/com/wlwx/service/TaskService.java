package com.wlwx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlwx.back.util.PlatformUtil;
import com.wlwx.dao.TaskInfoMapper;
import com.wlwx.model.TaskInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	private static final Logger LOGGER = Logger.getLogger(TaskService.class);
	
	@Autowired
	private TaskInfoMapper taskInfoMapper;

	/**
	 * 查询任务列表
	 * @date 2017年7月12日 上午10:29:30
	 * @return
	 */
	public PageInfo<TaskInfo> listTasks() {
		PageHelper.startPage(1, 3);
		List<TaskInfo> taskList = taskInfoMapper.listTasks();
		PageInfo<TaskInfo> pageInfo = new PageInfo<>(taskList);
		return pageInfo;
	}

	/**
	 * 根据任务ID查找任务
	 * @date 2017年7月12日 上午10:29:13
	 * @param taskId
	 * @return
	 */
	public TaskInfo getById(String taskId) {
		return taskInfoMapper.getById(taskId);
	}

	/**
	 * 修改任务
	 * @date 2017年7月12日 上午10:42:37
	 * @param taskInfo
	 */
	public void update(TaskInfo taskInfo) {
		Map<String, Object> params = new HashMap<>();
		Date endTime = new Date();
		String spendTime;
		try {
			spendTime = PlatformUtil.getDistanceTime(taskInfo.getStart_time(), endTime);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			spendTime = "";
		}
		params.put("task_id", taskInfo.getTask_id());
		params.put("task_status", TaskInfo.TERMINATED);
		params.put("end_time", endTime);
		params.put("spend_time", spendTime);
		taskInfoMapper.update(params);
	}
}