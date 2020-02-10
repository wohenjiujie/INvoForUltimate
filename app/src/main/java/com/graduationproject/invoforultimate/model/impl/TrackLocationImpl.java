package com.graduationproject.invoforultimate.model.impl;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.graduationproject.invoforultimate.model.TrackLocationModel;

/**
 * Created by INvo
 * on 2020-02-09.
 */
public class TrackLocationImpl {
    private LatLng latLng;
    private AMap aMap;
    public TrackLocationImpl() {
        super();
    }

    public void mapSettings(AMap aMap){
        this.aMap = aMap;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(
                new MyLocationStyle()
                        .interval(2000)
                        .showMyLocation(true)
                        .myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER)
                        .strokeColor(Color.argb(0, 0, 0, 0))
                        .radiusFillColor(Color.argb(0, 0, 0, 0))
        );
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setZoomPosition(AMapOptions.LOGO_MARGIN_RIGHT);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
    }

    public void setCameraModel(boolean type) {
        aMap.setMyLocationStyle(
                new MyLocationStyle()
                        .interval(2000)
                        .showMyLocation(true)
                        .myLocationType(type ? MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER : MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
                        .strokeColor(Color.argb(0, 0, 0, 0))
                        .radiusFillColor(Color.argb(0, 0, 0, 0))
        );
    }

    public void cameraFollow(int type, TrackLocationModel trackLocationModel) {
        aMap.setOnMyLocationChangeListener(location -> {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            trackLocationModel.onLatLngCallback(latLng);
        });
    }
}
