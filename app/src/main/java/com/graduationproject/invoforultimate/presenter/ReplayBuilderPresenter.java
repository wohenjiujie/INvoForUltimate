package com.graduationproject.invoforultimate.presenter;

import com.amap.api.maps.model.LatLng;
import com.amap.api.track.query.entity.Point;

import java.util.List;

/**
 * Created by INvo
 * on 2020-02-10.
 */
public interface ReplayBuilderPresenter extends TrackPresenter{
    void onTrackPointsCallback(List<Point> pointList);

    void onTrackPointsResultCallback(String s);

    void onMarkerReplayCallback(LatLng latLng);

}
