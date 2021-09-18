package com.user.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CITIES_MASTER")
public class CitiesMasterEntity {

	@Id
	@GeneratedValue
	@Column(name = "CITY_ID")
	private Integer cityid;

	@Column(name = "CITY_NAME")
	private String cityName;

	@Column(name = "STATE_ID")
	private Integer stateId;

	public Integer getCityid() {
		return cityid;
	}

	public void setCityid(Integer cityid) {
		this.cityid = cityid;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

}