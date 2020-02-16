package com.graduationproject.invoforultimate.entity.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public class TrackHistoryInfo<E> {
    private E counts;
    private ArrayList<E> desc = new ArrayList<>();
    private ArrayList<E> date = new ArrayList<>();
    private ArrayList<E> time = new ArrayList<>();
    private ArrayList<E> distance = new ArrayList<>();
    private ArrayList<E> TrackID = new ArrayList<>();
    private ArrayList<E> bitmap = new ArrayList<>();

    public E getCounts() {
        return counts;
    }

    public void setCounts(E counts) {
        this.counts = counts;
    }

    public ArrayList<E> getDesc() {
        return desc;
    }

    public void setDesc(ArrayList<E> desc) {
        this.desc = desc;
    }

    public ArrayList<E> getDate() {
        return date;
    }

    public void setDate(ArrayList<E> date) {
        this.date = date;
    }

    public ArrayList<E> getTime() {
        return time;
    }

    public void setTime(ArrayList<E> time) {
        this.time = time;
    }

    public ArrayList<E> getDistance() {
        return distance;
    }

    public void setDistance(ArrayList<E> distance) {
        this.distance = distance;
    }

    public ArrayList<E> getTrackID() {
        return TrackID;
    }

    public void setTrackID(ArrayList<E> trackID) {
        TrackID = trackID;
    }

    @Override
    public String toString() {
        return "TrackHistoryInfo{" +
                "counts=" + counts +
                ", desc=" + desc +
                ", date=" + date +
                ", time=" + time +
                ", distance=" + distance +
                ", TrackID=" + TrackID +
                '}';
    }

    public ArrayList<E> getBitmap() {
        return bitmap;
    }

    public void setBitmap(ArrayList<E> bitmap) {
        this.bitmap = bitmap;
    }
}
