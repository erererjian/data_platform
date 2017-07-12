package com.wlwx.dao;

import com.wlwx.model.TaskInfo;

import java.util.List;
import java.util.Map;

public interface TaskInfoMapper {
	
	/**
	 * 查询任务列表
	 * @date 2017年7月12日 上午10:31:16
	 * @return
	 */
	public List<TaskInfo> listTasks();

	/**
	 * 新增任务
	 * @date 2017年7月12日 上午10:31:04
	 * @param paramTaskInfo
	 */
	public void insertTask(TaskInfo paramTaskInfo);

	/**
	 * 修改任务
	 * @date 2017年7月12日 上午10:30:56
	 * @param paramMap
	 */
	public void update(Map<String, Object> paramMap);

	/**
	 * 根据任务ID查找任务
	 * @date 2017年7月12日 上午10:30:03
	 * @param taskId
	 * @return
	 */
	public TaskInfo getById(String taskId);
}