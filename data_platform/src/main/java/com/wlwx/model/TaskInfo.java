package com.wlwx.model;

import java.util.Date;

public class TaskInfo {
	
	public static final int NEW = 1;
	public static final int WAIT = 2;
	public static final int RUNNING = 3;
	public static final int TERMINATED = 4;
	public static final int SUCCESS = 5;
	public static final int FAIL = 6;
	
	private String task_id;
	private String user_id;
	private Date create_time;
	private Date start_time;
	private Date end_time;
	private String spend_time;
	private int priority;
	private String model_id;
	private int task_status;
	private String task_param;
	private long exec_id;
	private String file_path;

	public String getTask_id() {
		return this.task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public Date getEnd_time() {
		return this.end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public String getSpend_time() {
		return this.spend_time;
	}

	public void setSpend_time(String spend_time) {
		this.spend_time = spend_time;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getModel_id() {
		return this.model_id;
	}

	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}

	public int getTask_status() {
		return this.task_status;
	}

	public void setTask_status(int task_status) {
		this.task_status = task_status;
	}

	public String getTask_param() {
		return this.task_param;
	}

	public void setTask_param(String task_param) {
		this.task_param = task_param;
	}

	public long getExec_id() {
		return this.exec_id;
	}

	public void setExec_id(long exec_id) {
		this.exec_id = exec_id;
	}

	public String getFile_path() {
		return this.file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
}