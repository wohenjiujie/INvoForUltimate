package com.graduationproject.invoforultimate.ui.view.impl;

import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.bean.TrackHistoryInfo;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public interface HistoryViewCallback extends ViewCallback {

    void onGetTrackHistoryResult(@Nullable TrackHistoryInfo trackHistoryInfo);

    void onGetIntentResult(@Nullable Parcelable parcelable);

    void onDeleteResult();
}
