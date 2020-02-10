package com.graduationproject.invoforultimate.ui.view;

import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

/**
 * Created by INvo
 * on 2020-02-10.
 */
public interface ReplayViewCallback extends ViewCallback {

    void onTrackReplayResult(String s);

    void onTrackReplayCallback(MarkerOptions var1, MarkerOptions var2, PolylineOptions var3, LatLngBounds.Builder builder);
}
