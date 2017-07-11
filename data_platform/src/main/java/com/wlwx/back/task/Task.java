package com.wlwx.back.task;

import com.wlwx.model.AzUserInfo;
import com.wlwx.model.ModelInfo;
import java.util.Date;
import java.util.Map;

public abstract class Task {
	public int state = 1;
	private String task_id;
	private String model_id;
	private String user_id;
	private Map<String, Object> task_param;
	private String course;
	private int priority;
	private int progress;
	private Date create_time;
	private Date start_time;
	private long exec_id;
	private AzUserInfo azUserInfo;
	private ModelInfo modelInfo;

	public long getExec_id() {
		return this.exec_id;
	}

	public void setExec_id(long exec_id) {
		this.exec_id = exec_id;
	}

	public AzUserInfo getAzUserInfo() {
		return this.azUserInfo;
	}

	public void setAzUserInfo(AzUserInfo azUserInfo) {
		this.azUserInfo = azUserInfo;
	}

	public ModelInfo getModelInfo() {
		return this.modelInfo;
	}

	public void setModelInfo(ModelInfo modelInfo) {
		this.modelInfo = modelInfo;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTask_id() {
		return this.task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getModel_id() {
		return this.model_id;
	}

	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Map<String, Object> getTask_param() {
		return this.task_param;
	}

	public void setTask_param(Map<String, Object> task_param) {
		this.task_param = task_param;
	}

	public String getCourse() {
		return this.course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getProgress() {
		return this.progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Date getCreate_time() {
		return this.create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getStart_time() {
		return this.start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public abstract void deal();
}