package com.graduationproject.invoforultimate.model;

/**
 * Created by INvo
 * on 2020-02-08.
 */
public interface TrackServiceModel extends ModelResult {


    void onTrackCallback(int x,String s);

    void onTrackChangedCallback(String s1,String s2);

    void onTrackLocationCallback(double d1,double d2,int i);

    void onTrackUploadCallback(boolean x);

}
