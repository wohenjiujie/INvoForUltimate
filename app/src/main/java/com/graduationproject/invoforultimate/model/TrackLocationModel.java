package com.graduationproject.invoforultimate.model;

import com.amap.api.maps.model.LatLng;

/**
 * Created by INvo
 * on 2020-02-09.
 */
public interface TrackLocationModel extends ModelResult {
    void onLatLngCallback(LatLng latLng);
}
