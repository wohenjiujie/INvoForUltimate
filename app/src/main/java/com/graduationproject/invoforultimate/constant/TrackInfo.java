package com.graduationproject.invoforultimate.constant;

/**
 * Created by INvo
 * on 2019-11-03.
 */
public class TrackInfo {
    private long serviceID;
    private long terminalID;
    private long trackID;
    private String desc;
    private String date;
    private String time;
    private int timeConsuming;
//    private double distance;

    public long getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(long terminalID) {
        this.terminalID = terminalID;
    }

    public long getTrackID() {
        return trackID;
    }

    public void setTrackID(long trackID) {
        this.trackID = trackID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /*public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }*/

    public int getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(int timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public long getServiceID() {
        return serviceID;
    }

    public void setServiceID(long serviceID) {
        this.serviceID = serviceID;
    }
}
