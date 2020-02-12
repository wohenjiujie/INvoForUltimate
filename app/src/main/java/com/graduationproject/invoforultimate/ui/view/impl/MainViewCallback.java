package com.graduationproject.invoforultimate.ui.view.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;

/**
 * Created by INvo
 * on 2020-02-07.
 */
public interface MainViewCallback extends ViewCallback {

    void onCheckTerminalResult(boolean x);

    void onCreateTerminalResult(String s);

    void onTrackResult(int callback, String s);

    void onTrackChangedResult(String s1, String s2);

    void onTrackLocationResult(double longitude, double latitude, int rank);

    void onTrackUploadResult(boolean x);

    void onInitLocation(LatLng latLng,@NonNull Integer type);
}
