package com.graduationproject.invoforultimate.presenter.impl;

import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.track.query.entity.Point;
import com.graduationproject.invoforultimate.model.TrackReplayModel;
import com.graduationproject.invoforultimate.model.impl.TrackReplayImpl;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.presenter.ReplayBuilderPresenter;
import com.graduationproject.invoforultimate.ui.view.impl.ReplayViewCallback;

import java.util.List;

/**
 * Created by INvo
 * on 2020-02-10.
 */
public class ReplayBuilderImpl extends Presenter<ReplayViewCallback> implements ReplayBuilderPresenter {

    public ReplayBuilderImpl() {
        super();
    }

    public void trackReplay(Bundle bundle) {
        new TrackReplayImpl(bundle, new TrackReplayModel() {
            @Override
            public void onTrackPointsCallback(List<Point> pointList) {
                drawPolyline(pointList);
            }

            @Override
            public void onTrackPointsResultCallback(String s) {
                getV().onTrackReplayResult(s);
            }
        });
    }

    private void drawPolyline(List<Point> points) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE).width(20);
        MarkerOptions markerOptions1 = null, markerOptions2 = null;
        if (points.size() > 0) {
            /**
             * 起点
             * available customize startPoint Icon
             */
            Point p = points.get(0);
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            markerOptions1 = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        if (points.size() > 1) {
            /**
             * 终点
             * available customize endPoints Icon
             */
            Point p = points.get(points.size() - 1);
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            markerOptions2 = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        getV().onTrackReplayCallback(markerOptions1, markerOptions2, polylineOptions, boundsBuilder);
    }
}
