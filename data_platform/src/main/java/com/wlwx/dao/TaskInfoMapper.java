package com.wlwx.dao;

import com.wlwx.model.TaskInfo;

import java.util.List;
import java.util.Map;

public interface TaskInfoMapper {
	
	/**
	 * 查询任务列表
	 * @param map 
	 * @date 2017年7月12日 上午10:31:16
	 * @return
	 */
	public List<TaskInfo> listTasks(Map<String, Object> map);

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

	/**
	 * 查找数据库未完成的任务
	 * @date 2017年7月13日 下午4:29:34
	 * @return
	 */
	public List<TaskInfo> getNotFinishTasks();

	/**
	 * 定时删除历史数据
	 * @date 2017年7月17日 上午10:25:33
	 */
	public int delHistoryData();

	/**
	 * 获取需要删除历史数据任务ID
	 * @date 2017年7月17日 上午11:54:35
	 * @return
	 */
	public List<String> getDelTaskIds();
}