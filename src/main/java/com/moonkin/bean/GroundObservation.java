package com.moonkin.bean;

import java.io.Serializable;

public class GroundObservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String StationCode;     //站号
    private String dateTime;       //世界时间
    private String iw;        //风速指示码，0,1表示风速以米/秒为单位，3,4表示风速以海里/小时为单位
    private String rainCode;       //降水组指示码 ，1为有降水组；3为无降水组；4为有降水而没有观测
    private String ix;       //现在和过去天气组是否编报指示码，1为人式站编报；4为自动站编报；2、3为人工站不编报；5、6为自动站不编报
    private String lch;       //最低云底高度(-或/表示云层高度不明)
    private String vis;       //海面有效能见度(单位：编码)
    private String totalCloud;    //总云量(单位：编码)
    private String wd;      //风向(单位：编码)
    private String ws;     //风速(单位：m/s)
    private String t;     //气温(单位：摄氏度)
    private String dewt;    //露点温度(单位：摄氏度)
    private String rh;    //相对湿度
    private String p;     //气压(单位：hPa)
    private String seaP;    //海平面气压(单位：hPa)
    private String trend;    //过去3小时气压变化倾向
    private String p3h;    //3小时变压(单位：hPa)
    private String rain;    //降水量(单位：mm)
    private String ww;    //现在天气(单位：编码)
    private String cwlw1;            //过去天气1
    private String cwlw2;            //过去天气1
    private String lcl;    //低云量(单位：编码)
    private String lcs;    //低云状(单位：编码)
    private String mcs;   //中云状(单位：编码)
    private String hcs;   //高云状(单位：编码)
    private String p24h;      //过去24小时变压(单位：hPa)
    private String t24h;    //过去24小时变温(单位：摄氏度)
    private String t24hMax;    //过去24小时最高温(单位：摄氏度)
    private String t24hMin;   //过去24小时最低温(单位：摄氏度)
    private String tMin;   //地面最低温(单位：摄氏度)
    private String rain24h;  //'过去24小时降水量(单位：mm)

    /**
     * @return the stationCode
     */
    public String getStationCode() {
        return StationCode;
    }

    /**
     * @param stationCode the stationCode to set
     */
    public void setStationCode(String stationCode) {
        StationCode = stationCode;
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @return the trend
     */
    public String getTrend() {
        return trend;
    }

    /**
     * @param trend the trend to set
     */
    public void setTrend(String trend) {
        this.trend = trend;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return the iw
     */
    public String getIw() {
        return iw;
    }

    /**
     * @param iw the iw to set
     */
    public void setIw(String iw) {
        this.iw = iw;
    }

    /**
     * @return the rainCode
     */
    public String getRainCode() {
        return rainCode;
    }

    /**
     * @param rainCode the rainCode to set
     */
    public void setRainCode(String rainCode) {
        this.rainCode = rainCode;
    }

    /**
     * @return the ix
     */
    public String getIx() {
        return ix;
    }

    /**
     * @param ix the ix to set
     */
    public void setIx(String ix) {
        this.ix = ix;
    }

    /**
     * @return the h
     */
    public String getLch() {
        return lch;
    }

    /**
     * @param lch the h to set
     */
    public void setLch(String lch) {
        this.lch = lch;
    }

    /**
     * @return the vis
     */
    public String getVis() {
        return vis;
    }

    /**
     * @param vis the vis to set
     */
    public void setVis(String vis) {
        this.vis = vis;
    }

    /**
     * @return the totalCloud
     */
    public String getTotalCloud() {
        return totalCloud;
    }

    /**
     * @param totalCloud the totalCloud to set
     */
    public void setTotalCloud(String totalCloud) {
        this.totalCloud = totalCloud;
    }

    /**
     * @return the wd
     */
    public String getWd() {
        return wd;
    }

    /**
     * @param wd the wd to set
     */
    public void setWd(String wd) {
        this.wd = wd;
    }

    /**
     * @return the ws
     */
    public String getWs() {
        return ws;
    }

    /**
     * @param ws the ws to set
     */
    public void setWs(String ws) {
        this.ws = ws;
    }

    /**
     * @return the t
     */
    public String getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(String t) {
        this.t = t;
    }

    /**
     * @return the dewt
     */
    public String getDewt() {
        return dewt;
    }

    /**
     * @param dewt the dewt to set
     */
    public void setDewt(String dewt) {
        this.dewt = dewt;
    }

    /**
     * 获取rh
     *
     * @return rh rh
     */
    public String getRh() {
        return rh;
    }

    /**
     * 设置rh
     *
     * @param rh
     */
    public void setRh(String rh) {
        this.rh = rh;
    }

    /**
     * @return the p
     */
    public String getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(String p) {
        this.p = p;
    }

    /**
     * @return the seaP
     */
    public String getSeaP() {
        return seaP;
    }

    /**
     * @param seaP the seaP to set
     */
    public void setSeaP(String seaP) {
        this.seaP = seaP;
    }

    /**
     * @return the p3h
     */
    public String getP3h() {
        return p3h;
    }

    /**
     * @param p3h the p3h to set
     */
    public void setP3h(String p3h) {
        this.p3h = p3h;
    }

    /**
     * @return the rain
     */
    public String getRain() {
        return rain;
    }

    /**
     * @param rain the rain to set
     */
    public void setRain(String rain) {
        this.rain = rain;
    }

    /**
     * @return the ww
     */
    public String getWw() {
        return ww;
    }

    /**
     * @param ww the ww to set
     */
    public void setWw(String ww) {
        this.ww = ww;
    }

    /**
     * 获取cwlw1
     *
     * @return cwlw1 cwlw1
     */
    public String getCwlw1() {
        return cwlw1;
    }

    /**
     * 设置cwlw1
     *
     * @param cwlw1
     */
    public void setCwlw1(String cwlw1) {
        this.cwlw1 = cwlw1;
    }

    /**
     * 获取cwlw2
     *
     * @return cwlw2 cwlw2
     */
    public String getCwlw2() {
        return cwlw2;
    }

    /**
     * 设置cwlw2
     *
     * @param cwlw2
     */
    public void setCwlw2(String cwlw2) {
        this.cwlw2 = cwlw2;
    }

    /**
     * @return the lcl
     */
    public String getLcl() {
        return lcl;
    }

    /**
     * @param lcl the lcl to set
     */
    public void setLcl(String lcl) {
        this.lcl = lcl;
    }

    /**
     * @return the lcs
     */
    public String getLcs() {
        return lcs;
    }

    /**
     * @param lcs the lcs to set
     */
    public void setLcs(String lcs) {
        this.lcs = lcs;
    }

    /**
     * @return the mcs
     */
    public String getMcs() {
        return mcs;
    }

    /**
     * @param mcs the mcs to set
     */
    public void setMcs(String mcs) {
        this.mcs = mcs;
    }

    /**
     * @return the hcs
     */
    public String getHcs() {
        return hcs;
    }

    /**
     * @param hcs the hcs to set
     */
    public void setHcs(String hcs) {
        this.hcs = hcs;
    }

    /**
     * @return the p24h
     */
    public String getP24h() {
        return p24h;
    }

    /**
     * @param p24h the p24h to set
     */
    public void setP24h(String p24h) {
        this.p24h = p24h;
    }

    /**
     * @return the t24h
     */
    public String getT24h() {
        return t24h;
    }

    /**
     * @param t24h the t24h to set
     */
    public void setT24h(String t24h) {
        this.t24h = t24h;
    }

    /**
     * @return the t24hMax
     */
    public String getT24hMax() {
        return t24hMax;
    }

    /**
     * @param t24hMax the t24hMax to set
     */
    public void setT24hMax(String t24hMax) {
        this.t24hMax = t24hMax;
    }

    /**
     * @return the t24hMin
     */
    public String getT24hMin() {
        return t24hMin;
    }

    /**
     * @param t24hMin the t24hMin to set
     */
    public void setT24hMin(String t24hMin) {
        this.t24hMin = t24hMin;
    }

    /**
     * @return the tMin
     */
    public String gettMin() {
        return tMin;
    }

    /**
     * @param tMin the tMin to set
     */
    public void settMin(String tMin) {
        this.tMin = tMin;
    }

    /**
     * @return the rain24h
     */
    public String getRain24h() {
        return rain24h;
    }

    /**
     * @param rain24h the rain24h to set
     */
    public void setRain24h(String rain24h) {
        this.rain24h = rain24h;
    }
}
