package com.wlwx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wlwx.dao.TaskInfoMapper;
import com.wlwx.model.TaskInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	@Autowired
	private TaskInfoMapper taskInfoMapper;

	public PageInfo<TaskInfo> listTasks() {
		PageHelper.startPage(1, 3);
		List<TaskInfo> taskList = taskInfoMapper.listTasks();
		PageInfo<TaskInfo> pageInfo = new PageInfo<>(taskList);
		return pageInfo;
	}
}