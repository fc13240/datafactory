/*
* @class Station
* @version 1.0
* @since 2018-03-13
* @copyright
*/
package com.moonkin.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点信息
 *
 * @author xuduo
 * @version 1.0
 * @since 2018-03-13
 */
public class Station implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5968542318841102793L;

    /**
     * 观测时间
     */
    private Date time;

    /**
     * 站号
     */
    private String stationId;

    /**
     * 站名
     */
    private String stationName;

    /**
     * 站点类型（0：基本站；1：区域站；2：乡村代表站；3：交通站；4：雨量站；5：城市站）
     */
    private String stationType;

    /**
     * 站点级别
     */
    private Integer stationLevel;

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
     * 所属省或直辖市或自治区
     */
    private String province;

    /**
     * 所属市或地区
     */
    private String city;

    /**
     * 所属县或县级市
     */
    private String county;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 放大级别
     */
    private Integer magnification;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    public Integer getStationLevel() {
        return stationLevel;
    }

    public void setStationLevel(Integer stationLevel) {
        this.stationLevel = stationLevel;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getMagnification() {
        return magnification;
    }

    public void setMagnification(Integer magnification) {
        this.magnification = magnification;
    }

    @Override
    public String toString() {
        return "Station{" +
                "time=" + time +
                ", stationId='" + stationId + '\'' +
                ", stationName='" + stationName + '\'' +
                ", stationType='" + stationType + '\'' +
                ", stationLevel=" + stationLevel +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", seaheight=" + seaheight +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", magnification=" + magnification +
                '}';
    }
}