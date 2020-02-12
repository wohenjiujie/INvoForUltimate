package com.graduationproject.invoforultimate.presenter.impl;

import android.widget.Chronometer;

import com.amap.api.maps.AMap;
import com.graduationproject.invoforultimate.model.TerminalModel;
import com.graduationproject.invoforultimate.model.TrackServiceModel;
import com.graduationproject.invoforultimate.model.impl.TerminalModelImpl;
import com.graduationproject.invoforultimate.model.impl.TrackLocationImpl;
import com.graduationproject.invoforultimate.model.impl.TrackServiceImpl;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.presenter.MainBuilderPresenter;
import com.graduationproject.invoforultimate.ui.view.impl.MainViewCallback;

import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CAMERA_FOLLOW_INIT;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CAMERA_FOLLOW_START;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CAMERA_FOLLOW_STOP;


/**
 * Created by INvo
 * on 2020-02-07.
 */
public class MainBuilderImpl extends Presenter<MainViewCallback> implements MainBuilderPresenter {


    private TerminalModelImpl terminalModelImpl;
    private TrackServiceImpl trackServiceImpl;
    private TrackLocationImpl trackLocationImpl;

    private TrackServiceModel trackServiceModel = new TrackServiceModel() {
        @Override
        public void onTrackCallback(int x, String s) {
            getV().onTrackResult(x, s);
        }

        @Override
        public void onTrackChangedCallback(String s1, String s2) {
            getV().onTrackChangedResult(s1,s2);
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
    };

    public MainBuilderImpl() {
        super();
        this.terminalModelImpl = new TerminalModelImpl(new TerminalModel() {
            @Override
            public void createTerminalCallback(String s) {
                getV().onCreateTerminalResult(s);
            }

            @Override
            public void checkTerminalCallback(boolean x) {
                getV().onCheckTerminalResult(x);
            }
        });
        this.trackLocationImpl = new TrackLocationImpl();
    }

    public void setCamera(boolean type) {
        trackLocationImpl.setCameraModel(type);
    }

    public void mapSettings(AMap aMap) {
        trackLocationImpl.mapSettings(aMap);
        trackLocationImpl.cameraFollow(CAMERA_FOLLOW_INIT, latLng -> {
            getV().onInitLocation(latLng,CAMERA_FOLLOW_INIT);
        });
    }

    public void stopTrack() {
        trackServiceImpl.onStopTrack();
        trackLocationImpl.cameraFollow(CAMERA_FOLLOW_STOP, latLng -> getV().onInitLocation(latLng,CAMERA_FOLLOW_STOP));

    }

    public void startTrack(Chronometer chronometer) {
        this.trackServiceImpl = new TrackServiceImpl(chronometer);
        trackServiceImpl.onStartTrack(trackServiceModel);
        trackLocationImpl.cameraFollow(CAMERA_FOLLOW_START, latLng -> getV().onInitLocation(latLng,CAMERA_FOLLOW_START));
    }

    @Override
    public void createTerminal(String s) {
        terminalModelImpl.createTerminal(s);
    }

    @Override
    public void checkTerminal() {
        terminalModelImpl.checkTerminal();
    }
}
