package com.graduationproject.invoforultimate.constant;

/**
 * Created by INvo
 * on 2019-11-03.
 */
public class TrackInfo<T> {
    private T serviceID;
    private T terminalID;
    private T trackID;
    private T desc;
    private T date;
    private T time;
    private T timeConsuming;
//    private double distance;

    public T getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(T terminalID) {
        this.terminalID = terminalID;
    }

    public T getTrackID() {
        return trackID;
    }

    public void setTrackID(T trackID) {
        this.trackID = trackID;
    }

    public T getDesc() {
        return desc;
    }

    public void setDesc(T desc) {
        this.desc = desc;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public T getTime() {
        return time;
    }

    public void setTime(T time) {
        this.time = time;
    }

    /*public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }*/

    public T getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(T timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public T getServiceID() {
        return serviceID;
    }

    public void setServiceID(T serviceID) {
        this.serviceID = serviceID;
    }
}
