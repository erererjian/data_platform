package com.wlwx.model;

public class ModelInfo {
	private String model_id;
	private String model_name;
	private String az_user_id;
	private String az_project;
	private String az_flow_id;

	public String getModel_id() {
		return this.model_id;
	}

	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}

	public String getModel_name() {
		return this.model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getAz_user_id() {
		return this.az_user_id;
	}

	public void setAz_user_id(String az_user_id) {
		this.az_user_id = az_user_id;
	}

	public String getAz_project() {
		return this.az_project;
	}

	public void setAz_project(String az_project) {
		this.az_project = az_project;
	}

	public String getAz_flow_id() {
		return this.az_flow_id;
	}

	public void setAz_flow_id(String az_flow_id) {
		this.az_flow_id = az_flow_id;
	}
}