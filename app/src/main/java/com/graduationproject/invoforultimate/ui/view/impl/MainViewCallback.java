package com.graduationproject.invoforultimate.ui.view.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.poisearch.PoiResult;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;
import com.graduationproject.invoforultimate.utils.PoiOverlay;

import org.json.JSONObject;

/**
 * Created by INvo
 * on 2020-02-07.
 */
public interface MainViewCallback extends ViewCallback {

    void onPoiOverlayResult(PoiResult poiResult, int i);

    void onCheckTerminalResult(boolean x);

    void onCreateTerminalResult(String s);

    void onInitLocationResult(LatLng latLng);

    void onGetWeatherResult(@Nullable JSONObject object);
}
