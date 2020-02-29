package com.graduationproject.invoforultimate.presenter.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.entity.bean.TrackHistoryInfo;
import com.graduationproject.invoforultimate.model.impl.TrackHistoryImpl;
import com.graduationproject.invoforultimate.presenter.HistoryBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.ui.view.impl.HistoryViewCallback;

import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;
import static com.graduationproject.invoforultimate.entity.constants.TrackHistoryConstants.*;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public class HistoryBuilderImpl extends Presenter<HistoryViewCallback> implements HistoryBuilderPresenter{
    private ProgressDialog progressDialog;
    public void getTrackHistoryInfo(@Nullable Context context) {
        new TrackHistoryImpl(GET_TRACK_HISTORY_INFO,null, this).execute();
        if (null != context) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void getTrackHistoryIntent(Object var) {
        new TrackHistoryImpl(GET_TRACK_INTENT_INFO,  var, this).execute();
    }

    public void deleteTrack(Object var) {
        new TrackHistoryImpl(DELETE_TRACK_INFO, var, this).execute();
    }

    @Override
    public void onGetTrackHistoryCallback(TrackHistoryInfo trackHistoryInfo) {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
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
