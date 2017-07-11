package com.wlwx.model;

import java.util.Date;

public class AzUserInfo {
	private String az_user_id;
	private String az_user_name;
	private String az_user_password;
	private Date az_create_time;

	public String getAz_user_id() {
		return this.az_user_id;
	}

	public void setAz_user_id(String az_user_id) {
		this.az_user_id = az_user_id;
	}

	public String getAz_user_name() {
		return this.az_user_name;
	}

	public void setAz_user_name(String az_user_name) {
		this.az_user_name = az_user_name;
	}

	public String getAz_user_password() {
		return this.az_user_password;
	}

	public void setAz_user_password(String az_user_password) {
		this.az_user_password = az_user_password;
	}

	public Date getAz_create_time() {
		return this.az_create_time;
	}

	public void setAz_create_time(Date az_create_time) {
		this.az_create_time = az_create_time;
	}
}