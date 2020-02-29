package com.graduationproject.invoforultimate.presenter;

/**
 * Created by INvo
 * on 2020-02-29.
 */
public interface RecordBuilderPresenter extends TrackPresenter {
    void onTrackCallback(int x,String s);

    void onTrackChangedCallback(String var1,String var2,String var3);

    void onTrackLocationCallback(double d1,double d2,int i);

    void onTrackUploadCallback(boolean x);
}
