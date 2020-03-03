package com.graduationproject.invoforultimate.model.impl;

import android.graphics.Color;
import android.location.Location;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.listener.OnLocationServiceListener;
import com.graduationproject.invoforultimate.listener.OnLocationServiceListenerImpl;
import com.graduationproject.invoforultimate.presenter.MainBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.RecordBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.TrackPresenter;

import java.lang.reflect.Type;

import static com.graduationproject.invoforultimate.app.TrackApplication.getAddress;
import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLocation;

/**
 * Created by INvo
 * on 2020-02-09.
 */
public class TrackLocationImpl {
    private LatLng latLng;
    private AMap aMap;
    private MainBuilderPresenter mainBuilderPresenter;
    private MyLocationStyle myLocationStyle;
    private PoiSearch poiSearch;

    public TrackLocationImpl(@Nullable TrackPresenter trackPresenter) {
        this.mainBuilderPresenter = (MainBuilderPresenter) trackPresenter;
    }

    public void mapSettings(AMap aMap, int type) {
        this.aMap = aMap;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.setMyLocationEnabled(true);
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000).showMyLocation(true);
        if (0x101 == type) {
            //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
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
            //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.landian))
                    .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
                    .strokeColor(Color.argb(0, 0, 0, 0))
                    .radiusFillColor(Color.argb(0, 0, 0, 0));
        }
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setCompassEnabled(true);
    }

    public void setCameraModel(boolean type) {
        //连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
        aMap.setMyLocationStyle(
                new MyLocationStyle()
                        .interval(2000)
                        .showMyLocation(true)
                        .myLocationType(type ? MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER : MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
                        .strokeColor(Color.argb(0, 0, 0, 0))
                        .radiusFillColor(Color.argb(0, 0, 0, 0))
        );
    }

    public void cameraFollow(@Nullable int type) {
        aMap.setOnMyLocationChangeListener(location -> {
            getLocation(new OnLocationServiceListenerImpl());
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mainBuilderPresenter.onLatLngCallback(latLng);
        });
    }
}
