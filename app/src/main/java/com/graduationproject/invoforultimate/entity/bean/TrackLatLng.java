package com.graduationproject.invoforultimate.entity.bean;

import com.amap.api.location.DPoint;

/**
 * Created by INvo
 * on 2020-02-17.
 */
public class TrackLatLng {
    private DPoint startDPoint;
    private DPoint endDPoint;

    public DPoint getStartDPoint() {
        return startDPoint;
    }

    public void setStartDPoint(DPoint startDPoint) {
        this.startDPoint = startDPoint;
    }

    public DPoint getEndDPoint() {
        return endDPoint;
    }

    public void setEndDPoint(DPoint endDPoint) {
        this.endDPoint = endDPoint;
    }
}
