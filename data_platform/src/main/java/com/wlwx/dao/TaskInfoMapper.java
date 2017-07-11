package com.wlwx.dao;

import com.wlwx.model.TaskInfo;
import java.util.List;
import java.util.Map;

public abstract interface TaskInfoMapper
{
  public abstract List<TaskInfo> listTasks();

  public abstract void insertTask(TaskInfo paramTaskInfo);

  public abstract void update(Map<String, Object> paramMap);
}