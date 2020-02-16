package com.graduationproject.invoforultimate.presenter.impl;

import android.os.Parcelable;

import com.graduationproject.invoforultimate.entity.bean.TrackHistoryInfo;
import com.graduationproject.invoforultimate.model.impl.TrackHistoryServiceImpl;
import com.graduationproject.invoforultimate.presenter.HistoryBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.ui.view.impl.HistoryViewCallback;

import static com.graduationproject.invoforultimate.entity.constants.TrackHistoryConstants.*;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public class HistoryBuilderImpl extends Presenter<HistoryViewCallback> implements HistoryBuilderPresenter{
    public HistoryBuilderImpl() {
        super();
    }

    public void getTrackHistoryInfo() {
        new TrackHistoryServiceImpl(GET_TRACK_HISTORY_INFO,null, this).execute();
    }

    public void getTrackHistoryIntent(Object var) {
        new TrackHistoryServiceImpl(GET_TRACK_INTENT_INFO,  var, this).execute();
    }

    public void deleteTrack(Object var) {
        new TrackHistoryServiceImpl(DELETE_TRACK_INFO, var, this).execute();
    }

    @Override
    public void onGetTrackHistoryCallback(TrackHistoryInfo trackHistoryInfo) {
        getV().onGetTrackHistoryResult(trackHistoryInfo);
    }

    @Override
    public void onGetIntentCallback(Parcelable parcelable) {
        getV().onGetIntentResult(parcelable);
    }

    @Override
    public void onDeleteCallback() {
        getV().onDeleteResult();
    }
}
