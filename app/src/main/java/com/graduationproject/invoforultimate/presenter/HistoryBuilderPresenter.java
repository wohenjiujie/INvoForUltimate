package com.graduationproject.invoforultimate.presenter;

import android.os.Parcelable;

import com.graduationproject.invoforultimate.bean.TrackHistoryInfo;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public interface HistoryBuilderPresenter {
    void onGetTrackHistoryCallback(TrackHistoryInfo trackHistoryInfo);

    void onGetIntentCallback(Parcelable parcelable);

    void onDeleteCallback();
}
