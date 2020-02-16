package com.graduationproject.invoforultimate.presenter.impl;

import android.graphics.Bitmap;
import android.widget.Chronometer;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.graduationproject.invoforultimate.model.impl.TrackTerminalImpl;
import com.graduationproject.invoforultimate.model.impl.TrackLocationImpl;
import com.graduationproject.invoforultimate.model.impl.TrackServiceImpl;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.presenter.MainBuilderPresenter;
import com.graduationproject.invoforultimate.ui.view.impl.MainViewCallback;

import static com.graduationproject.invoforultimate.entity.constants.MainConstants.CAMERA_FOLLOW_INIT;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.CAMERA_FOLLOW_START;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.CAMERA_FOLLOW_STOP;


/**
 * Created by INvo
 * on 2020-02-07.
 */
public class MainBuilderImpl extends Presenter<MainViewCallback> implements MainBuilderPresenter {


    private TrackTerminalImpl trackTerminalImpl;
    private TrackServiceImpl trackServiceImpl;
    private TrackLocationImpl trackLocationImpl;

    public MainBuilderImpl() {
        this.trackTerminalImpl = new TrackTerminalImpl(this);
        this.trackLocationImpl = new TrackLocationImpl(this);
    }

    /*private TrackServiceTrackModel trackServiceModel = new TrackServiceTrackModel() {
        @Override
        public void onTrackCallback(int x, String s) {
            getV().onTrackResult(x, s);
        }

        @Override
        public void onTrackChangedCallback(String s1, String s2) {
            getV().onTrackChangedResult(s1, s2);
        }

        @Override
        public void onTrackLocationCallback(double d1, double d2, int i) {
            getV().onTrackLocationResult(d1, d2, i);
        }

        @Override
        public void onTrackUploadCallback(boolean x) {
            getV().onTrackUploadResult(x);
            if (x) {
                trackServiceImpl.onUploadTrackCheck();
            }
        }
    };*/

    public void stopTrack(Bitmap bitmap) {
        trackServiceImpl.onStopTrack(bitmap);
        trackLocationImpl.cameraFollow(CAMERA_FOLLOW_STOP);
    }

    public void startTrack(Chronometer chronometer) {
        this.trackServiceImpl = new TrackServiceImpl(chronometer,this);
        trackServiceImpl.onStartTrack();
        trackLocationImpl.cameraFollow(CAMERA_FOLLOW_START);
    }

    public void setCamera(boolean type) {
        trackLocationImpl.setCameraModel(type);
    }

    public void mapSettings(AMap aMap) {
        trackLocationImpl.mapSettings(aMap);
        trackLocationImpl.cameraFollow(CAMERA_FOLLOW_INIT);
    }

    public void createTerminal(String s) {
        trackTerminalImpl.createTerminal(s);
    }

    public void checkTerminal() {
        trackTerminalImpl.checkTerminal();
    }

    @Override
    public void createTerminalCallback(String s) {
        getV().onCreateTerminalResult(s);
    }

    @Override
    public void checkTerminalCallback(boolean x) {
        getV().onCheckTerminalResult(x);
    }

    @Override
    public void onLatLngCallback(LatLng latLng, @NonNull Integer type) {
        if (CAMERA_FOLLOW_INIT == type) {
            getV().onInitLocationResult(latLng, type);
        } else if (CAMERA_FOLLOW_STOP == type) {
            getV().onInitLocationResult(latLng, type);
        } else if (CAMERA_FOLLOW_START == type) {
            getV().onInitLocationResult(latLng, type);
        }
    }

    @Override
    public void onTrackCallback(int x, String s) {
        getV().onTrackResult(x, s);
    }

    @Override
    public void onTrackChangedCallback(String s1, String s2) {
        getV().onTrackChangedResult(s1, s2);
    }

    @Override
    public void onTrackLocationCallback(double d1, double d2, int i) {
        getV().onTrackLocationResult(d1, d2, i);
    }

    @Override
    public void onTrackUploadCallback(boolean x) {
        getV().onTrackUploadResult(x);
        if (x) {
            trackServiceImpl.onUploadTrackCheck();
        }
    }
}
