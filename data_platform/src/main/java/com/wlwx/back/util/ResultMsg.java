package com.wlwx.back.util;

public class ResultMsg {
	
	private boolean success;
	private String msg;
	private Object obj;

	public ResultMsg() {
		super();
	}

	public ResultMsg(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public ResultMsg(boolean success, String msg, Object obj) {
		this.success = success;
		this.msg = msg;
		this.obj = obj;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return this.obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}