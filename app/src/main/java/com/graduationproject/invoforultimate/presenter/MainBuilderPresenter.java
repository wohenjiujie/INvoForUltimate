package com.graduationproject.invoforultimate.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;

import org.json.JSONObject;

/**
 * Created by INvo
 * on 2020-02-07.
 */
public interface MainBuilderPresenter extends TrackPresenter{

    void createTerminalCallback(String s);

    void checkTerminalCallback(boolean x);

    void onLatLngCallback(LatLng latLng,@NonNull Integer type);

  /*  void onTrackCallback(int x,String s);

    void onTrackChangedCallback(String s1,String s2);

    void onTrackLocationCallback(double d1,double d2,int i);

    void onTrackUploadCallback(boolean x);*/

    void onGetWeatherCallback(@NonNull boolean callback, @Nullable JSONObject obj);
}
