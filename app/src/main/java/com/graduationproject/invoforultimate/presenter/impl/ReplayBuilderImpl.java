package com.graduationproject.invoforultimate.presenter.impl;

import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.track.query.entity.Point;
import com.graduationproject.invoforultimate.R;
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
    private TrackReplayImpl trackReplayImpl;


    public void trackReplay(Bundle bundle) {
        trackReplayImpl= new TrackReplayImpl(bundle, this);
    }

    public void MarkerReplay() {
        trackReplayImpl.markerReplay();
    }

    private void drawPolyline(List<Point> points) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.argb(180,139 ,58, 98)).width(20);
        MarkerOptions markerOptions1 = null, markerOptions2 = null,markerOptions3=null;
        if (points.size() > 0) {
            /**
             * 起点
             * available customize startPoint Icon
             */
            Point p = points.get(0);
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            markerOptions1 = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
            markerOptions3 = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.walk));
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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
        }
        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        getV().onTrackReplayResult(markerOptions1, markerOptions2, polylineOptions, boundsBuilder,markerOptions3);
    }

    @Override
    public void onTrackPointsCallback(List<Point> pointList) {
        drawPolyline(pointList);
    }

    @Override
    public void onTrackPointsResultCallback(String s) {
        getV().onTrackReplayResult(s);
    }

    @Override
    public void onMarkerReplayCallback(LatLng latLng) {
        getV().onMarkerPositionResult(latLng);
    }
}
