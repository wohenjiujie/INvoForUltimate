package com.graduationproject.invoforultimate.ui.view;

/**
 * Created by INvo
 * on 2020-02-07.
 */
public interface MainViewCallback extends ViewCallback {

    void onCheckTerminalResult(boolean x);

    void onCreateTerminalResult(String s);

    void onStartTrackResult(int callback, String s);

    void onTrackChangedResult(String s1, String s2);

    void onTrackLocationResult(double longitude, double latitude, int rank);

    void onTrackUploadResult(boolean x);
}
