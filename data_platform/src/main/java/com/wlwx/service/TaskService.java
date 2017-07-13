package com.wlwx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlwx.back.util.PlatformUtil;
import com.wlwx.dao.ModelInfoMapper;
import com.wlwx.dao.TaskInfoMapper;
import com.wlwx.model.ModelInfo;
import com.wlwx.model.TaskInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskService {

	private static final Logger LOGGER = Logger.getLogger(TaskService.class);
	
	@Autowired
	private TaskInfoMapper taskInfoMapper;
	@Autowired
	private ModelInfoMapper modelInfoMapper;

	/**
	 * 查询任务列表
	 * @param map 
	 * @date 2017年7月12日 上午10:29:30
	 * @return
	 */
	public PageInfo<TaskInfo> listTasks(Map<String, Object> map) {
		int pageNum = 0;
		int pageSize = 10;
		if (map.get("page_num") != null) 
			pageNum = ((Integer) map.get("page_num")).intValue();//第几页
		if (map.get("page_size") != null) 
			pageSize = ((Integer) map.get("page_size")).intValue();//每页几条
		
		PageHelper.startPage(pageNum, pageSize);
		
		List<TaskInfo> taskList = taskInfoMapper.listTasks(map);//查询任务列表
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
	public void updateTask(TaskInfo taskInfo) {
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
	
	/**
	 * 根据模板ID获取模板信息
	 * @date 2017年7月13日 下午4:50:34
	 * @param modelId
	 * @return
	 */
	public ModelInfo getModelById(String modelId){
		return modelInfoMapper.get(modelId);
	}
	
	/**
	 * 修改任务
	 * @authod zjj
	 * @date 2017年7月13日 下午4:37:06
	 */
	public void update(Map<String, Object> params){
		taskInfoMapper.update(params);
	}

	/**
	 * 查找数据库未完成的任务
	 * @date 2017年7月13日 下午4:29:03
	 * @return
	 */
	public List<TaskInfo> getNotFinishTasks() {
		return taskInfoMapper.getNotFinishTasks();
	}
}