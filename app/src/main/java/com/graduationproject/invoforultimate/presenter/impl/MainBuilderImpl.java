package com.graduationproject.invoforultimate.presenter.impl;

import android.app.ProgressDialog;
import android.widget.Chronometer;

import com.graduationproject.invoforultimate.model.TerminalModule;
import com.graduationproject.invoforultimate.model.TrackServiceModel;
import com.graduationproject.invoforultimate.model.impl.TerminalModuleImpl;
import com.graduationproject.invoforultimate.model.impl.TrackServiceImpl;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.presenter.MainBuilderPresenter;
import com.graduationproject.invoforultimate.service.TrackService;
import com.graduationproject.invoforultimate.ui.view.MainViewCallback;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by INvo
 * on 2020-02-07.
 */
public class MainBuilderImpl extends Presenter<MainViewCallback> implements MainBuilderPresenter {


    private TerminalModuleImpl terminalModuleImpl;
    private TrackServiceImpl trackServiceImpl;

    private TrackServiceModel trackServiceModel = new TrackServiceModel() {
        @Override
        public void onStartTrackCallback(int x, String s) {
            getV().onStartTrackResult(x, s);
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
        this.terminalModuleImpl = new TerminalModuleImpl(new TerminalModule() {
            @Override
            public void createTerminalCallback(String s) {
                getV().onCreateTerminalResult(s);
            }

            @Override
            public void checkTerminalCallback(boolean x) {
                getV().onCheckTerminalResult(x);
            }
        });
    }

    public void stopTrack() {
        trackServiceImpl.onStopTrack();
    }

    public void startTrack(Chronometer chronometer) {
        this.trackServiceImpl = new TrackServiceImpl(chronometer);
        trackServiceImpl.onStartTrack(trackServiceModel);
    }

    @Override
    public void createTerminal(String s) {
        terminalModuleImpl.createTerminal(s);
    }

    @Override
    public void checkTerminal() {
        terminalModuleImpl.checkTerminal();
    }
}
