package com.wlwx.model;

public class UserInfo {
	private String user_id;
	private int custom_uid;
	private int custom_source;
	private String az_user_id;
	private int user_priority;
	private String user_password;
	private String user_phone;
	private String user_email;

	public String getUser_id() {
		return this.user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getCustom_uid() {
		return this.custom_uid;
	}

	public void setCustom_uid(int custom_uid) {
		this.custom_uid = custom_uid;
	}

	public int getCustom_source() {
		return this.custom_source;
	}

	public void setCustom_source(int custom_source) {
		this.custom_source = custom_source;
	}

	public String getAz_user_id() {
		return this.az_user_id;
	}

	public void setAz_user_id(String az_user_id) {
		this.az_user_id = az_user_id;
	}

	public int getUser_priority() {
		return this.user_priority;
	}

	public void setUser_priority(int user_priority) {
		this.user_priority = user_priority;
	}

	public String getUser_password() {
		return this.user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getUser_phone() {
		return this.user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_email() {
		return this.user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
}