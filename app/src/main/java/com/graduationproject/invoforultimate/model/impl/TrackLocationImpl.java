package com.graduationproject.invoforultimate.model.impl;

import android.graphics.Color;
import android.location.Location;

import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.presenter.MainBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.RecordBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.TrackPresenter;

import java.lang.reflect.Type;

/**
 * Created by INvo
 * on 2020-02-09.
 */
public class TrackLocationImpl {
    private LatLng latLng;
    private AMap aMap;
    private MainBuilderPresenter mainBuilderPresenter;
    private MyLocationStyle myLocationStyle;


    public TrackLocationImpl(@Nullable  TrackPresenter trackPresenter) {
        this.mainBuilderPresenter = (MainBuilderPresenter) trackPresenter;
    }

    public void mapSettings(AMap aMap, int type) {

        this.aMap = aMap;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.setMyLocationEnabled(true);
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000).showMyLocation(true);
        if (0x101 == type) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
                    .strokeColor(Color.argb(0, 0, 0, 0))
                    .radiusFillColor(Color.argb(0, 0, 0, 0));
            aMap.getUiSettings().setZoomControlsEnabled(true);
            aMap.getUiSettings().setZoomPosition(AMapOptions.LOGO_MARGIN_RIGHT);
            aMap.getUiSettings().setScaleControlsEnabled(true);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
        }
        if (0x102 == type) {
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.landian))
                    .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
                    .strokeColor(Color.argb(0, 0, 0, 0))
                    .radiusFillColor(Color.argb(0, 0, 0, 0));
        }
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setCompassEnabled(true);
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

    public void cameraFollow(int type) {
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
              mainBuilderPresenter.onLatLngCallback(latLng, type);
            }
        });
    }
}
