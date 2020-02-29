package com.graduationproject.invoforultimate.ui.view.impl;

import com.graduationproject.invoforultimate.ui.view.ViewCallback;

/**
 * Created by INvo
 * on 2020-02-29.
 */
public interface RecordViewCallback extends ViewCallback {
    void onTrackResult(int callback, String s);

    void onTrackChangedResult(String var1,String var2,String var3);

    void onTrackLocationResult(double longitude, double latitude, int rank);

    void onTrackUploadResult(boolean x);
}
