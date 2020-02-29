package com.graduationproject.invoforultimate.presenter.impl;

import android.graphics.Bitmap;
import android.widget.Chronometer;

import com.amap.api.maps.AMap;
import com.graduationproject.invoforultimate.model.impl.TrackLocationImpl;
import com.graduationproject.invoforultimate.model.impl.TrackServiceImpl;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.presenter.RecordBuilderPresenter;
import com.graduationproject.invoforultimate.ui.view.impl.RecordViewCallback;


/**
 * Created by INvo
 * on 2020-02-29.
 */
public class RecordBuilderImpl extends Presenter<RecordViewCallback> implements RecordBuilderPresenter {
    private TrackLocationImpl trackLocationImpl;
    private TrackServiceImpl trackServiceImpl;
    public RecordBuilderImpl() {
        trackLocationImpl = new TrackLocationImpl(null);
    }

    public void startTrack(Chronometer chronometer) {
        this.trackServiceImpl = new TrackServiceImpl(chronometer,this);
        trackServiceImpl.onStartTrack();
    }

    public void stopTrack(Bitmap bitmap) {
        trackServiceImpl.onStopTrack(bitmap);
    }

    public void mapSettings(AMap aMap) {
        trackLocationImpl.mapSettings(aMap,0x102);
    }

    @Override
    public void onTrackCallback(int x, String s) {
        getV().onTrackResult(x, s);
    }

    @Override
    public void onTrackChangedCallback(String var1,String var2,String var3) {
        getV().onTrackChangedResult(var1, var2,var3);

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
