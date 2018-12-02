package com.moonkin.bean;

import java.io.Serializable;

public class MiCaps2Data implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/**
     * 站号
     */
    private String stationId;
	/**
	 * 数据时间
	 */
	private String datetime;
    /**
     * 经度
     */
    private Float longitude;
    /**
     * 纬度
     */
    private Float latitude;
    /**
     * 海拔高度
     */
    private Float seaheight;
    /**
     * 站点级别
     */
    private Integer stationLevel;
    /**
     * 高度
     */
    private Float height;
    /**
     * 温度
     */
    private Float t;
    /**
     * 温度露点差
     */
    private Float ttd;
    /**
     * 风向
     */
    private Float windDir;
    /**
     * 风速
     */
    private Float windSpeed;
    /**
     * 要素名称
     */
    private String element;

    /**
     * 要素值
     */
    private String value;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public Float getSeaheight() {
		return seaheight;
	}
	public void setSeaheight(Float seaheight) {
		this.seaheight = seaheight;
	}
	public Integer getStationLevel() {
		return stationLevel;
	}
	public void setStationLevel(Integer stationLevel) {
		this.stationLevel = stationLevel;
	}
	public Float getHeight() {
		return height;
	}
	public void setHeight(Float height) {
		this.height = height;
	}
	public Float getT() {
		return t;
	}
	public void setT(Float t) {
		this.t = t;
	}
	public Float getTtd() {
		return ttd;
	}
	public void setTtd(Float ttd) {
		this.ttd = ttd;
	}
	public Float getWindDir() {
		return windDir;
	}
	public void setWindDir(Float windDir) {
		this.windDir = windDir;
	}
	public Float getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(Float windSpeed) {
		this.windSpeed = windSpeed;
	}
    
	
	@Override
    public String toString() {
        return "MiCaps2Data{" +
                "stationId='" + stationId + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", seaheight=" + seaheight +
                ", stationLevel=" + stationLevel +
                ", height=" + height +
                ", windDir=" + windDir +
                ", windSpeed=" + windSpeed +
                ", t=" + t +
                ", ttd=" + ttd +
                '}';
    }
}
