package com.graduationproject.invoforultimate.ui.view.impl;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;

/**
 * Created by INvo
 * on 2020-02-10.
 */
public interface ReplayViewCallback extends ViewCallback {

    void onTrackReplayResult(String s);

    void onTrackReplayResult(MarkerOptions var1, MarkerOptions var2, PolylineOptions var3, LatLngBounds.Builder builder, MarkerOptions var4);

    void onMarkerPositionResult(LatLng latLng);
}
