package com.wlwx.back.task;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.wlwx.azkaban.AzkabanUtil;
import com.wlwx.back.system.SystemInit;
import com.wlwx.back.util.AesUtil;
import com.wlwx.back.util.PlatformUtil;
import com.wlwx.dao.TaskInfoMapper;
import com.wlwx.model.AzUserInfo;
import com.wlwx.model.ModelInfo;
import com.wlwx.model.TaskInfo;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 导出任务，完整的一个任务流程
 * @author zjj
 * @date 2017年7月11日 下午4:56:49
 */
public class ExportTask extends Task {
	
	public static final Logger LOGGER = Logger.getLogger(ExportTask.class);
	
	private TaskInfoMapper taskInfoMapper;
	private String msg = "";

	public TaskInfoMapper getTaskInfoMapper() {
		return this.taskInfoMapper;
	}

	public void setTaskInfoMapper(TaskInfoMapper taskInfoMapper) {
		this.taskInfoMapper = taskInfoMapper;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void deal() {
		try {
			if (StringUtils.isEmpty(getTask_id())) {
				setState(TaskInfo.TERMINATED);
				setProgress(100);
				return;
			}
			Date startTime = new Date();
			ModelInfo modelInfo = getModelInfo();

			boolean success = false;
			for (int i = 0; i < 3; i++) {//尝试三次
				System.out.println("===================================== i = "+ i + "======================================");
				Object sessionObj = SystemInit.sessionMap.get(getUser_id());
				String sessionId = sessionObj != null ? sessionObj.toString(): ""; //获取sessionID

				JSONObject jsonObject = AzkabanUtil.executeFlow(sessionId, //执行任务
						modelInfo.getAz_project(), modelInfo.getAz_flow_id(),
						getTask_param());

				System.out.println("jsonObject = " + jsonObject);

				if (jsonObject.get("error") != null) {//执行任务失败
					if (!"session".equals(jsonObject.getString("error")))//如果不是因为session原因错误。直接退出任务
						break;
					
					AzUserInfo azUserInfo = getAzUserInfo();
					JSONObject jo = AzkabanUtil.actionLogin(azUserInfo.getAz_user_name(),//重新登录
							AesUtil.decrypt(azUserInfo.getAz_user_password()));
					System.out.println("jo = " + jo);
					if ((jo.get("error") == null) && ("success".equals(jo.getString("status")))) {//设置新的sessionID
						SystemInit.sessionMap.put(getUser_id(),jo.getString("session.id"));
					}
				} else {//任务执行成功
					setExec_id(jsonObject.getLongValue("execid"));
					success = true;
					//修改任务状态
					Map<String, Object> params = new HashMap<>();
					params.put("task_id", getTask_id());
					params.put("task_status", TaskInfo.RUNNING);
					params.put("start_time", startTime);
					params.put("exec_id", jsonObject.getLongValue("execid"));
					taskInfoMapper.update(params);
					break;
				}
			}
			if (!success) {
				System.out.println("任务失败");
				//任务失败，修改任务状态
				Map<String, Object> params = new HashMap<>();
				params.put("task_id", this.getTask_id());
				params.put("task_status", TaskInfo.FAIL);
				taskInfoMapper.update(params);
				return;
			}
			Object sessionObj = SystemInit.sessionMap.get(getUser_id());
			String sessionId = sessionObj != null ? sessionObj.toString() : "";
			while (true) {
				Task task = SystemInit.taskService.getTaskQueue().getTaskById(this.getTask_id());
				if (task.state == TaskInfo.TERMINATED) {//如果手动终止则修改状态
					Map<String, Object> params = new HashMap<>();
					Date endTime = new Date();

					params.put("task_id", this.getTask_id());
					params.put("task_status", task.state);
					params.put("end_time", endTime);
					params.put("spend_time",
							PlatformUtil.getDistanceTime(startTime, endTime));
					this.taskInfoMapper.update(params);
					break;
				}

				JSONObject jsonObject = AzkabanUtil.fetchExecFlow(sessionId, getExec_id() + "");
				String status = jsonObject.getString("status");
				if ((!AzkabanUtil.PREPARING.equals(status))
						&& (!AzkabanUtil.RUNNING.equals(status))) {//判断任务是否执行完成
					Map<String, Object> params = new HashMap<>();

					params.put("task_id", getTask_id());
					if ("FAILED".equals(status)) { //失败
						params.put("task_status", TaskInfo.FAIL);
					} else if ("CANCELLED".equals(status)) {//取消
						params.put("task_status", TaskInfo.TERMINATED);
					} else if ("SUCCEEDED".equals(status)) {//成功
						String filePath = PlatformUtil.FILE_URL
								+ File.separator + getUser_id()
								+ File.separator + modelInfo.getModel_name()
								+ File.separator
								+ getTask_param().get("create_time").toString()
								+ File.separator + "download" + File.separator
								+ modelInfo.getModel_name() + ".zip";
						params.put("task_status", TaskInfo.SUCCESS);
						params.put("file_path", filePath);
					}
					Date endTime = new Date();
					params.put("end_time", endTime);
					params.put("spend_time",
							PlatformUtil.getDistanceTime(startTime, endTime));
					taskInfoMapper.update(params);
					break;
				}

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setState(TaskInfo.TERMINATED);
			setCourse("扫描任务出现错误：" + e);

			LOGGER.error("ExportTask deal() error:" + e.getMessage());
		}
	}
}