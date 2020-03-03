package com.graduationproject.invoforultimate.presenter.impl;

import android.graphics.Bitmap;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.graduationproject.invoforultimate.listener.OnPoiSearchListenerImpl;
import com.graduationproject.invoforultimate.model.impl.TrackTerminalImpl;
import com.graduationproject.invoforultimate.model.impl.TrackLocationImpl;
import com.graduationproject.invoforultimate.model.impl.TrackServiceImpl;
import com.graduationproject.invoforultimate.model.impl.TrackWeatherImpl;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.presenter.MainBuilderPresenter;
import com.graduationproject.invoforultimate.ui.view.impl.MainViewCallback;
import com.graduationproject.invoforultimate.utils.PoiOverlay;

import org.json.JSONObject;

import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.CAMERA_FOLLOW_INIT;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.CAMERA_FOLLOW_START;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.CAMERA_FOLLOW_STOP;


/**
 * Created by INvo
 * on 2020-02-07.
 */
public class MainBuilderImpl extends Presenter<MainViewCallback> implements MainBuilderPresenter {
    private TrackTerminalImpl trackTerminalImpl;
    private TrackLocationImpl trackLocationImpl;
    private PoiSearch poiSearch;


    public MainBuilderImpl() {
        this.trackTerminalImpl = new TrackTerminalImpl(this);
        this.trackLocationImpl = new TrackLocationImpl(this);
    }
    public void initPoiSearch(double lat, double lon, String type) {
            poiSearch =null;
            PoiSearch.Query poiQuery = new PoiSearch.Query("", type);
            LatLonPoint centerPoint = new LatLonPoint(lat, lon);
            PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(centerPoint, 1000);
            poiSearch = new PoiSearch(getContext(), poiQuery);
            poiSearch.setBound(searchBound);
            poiSearch.setOnPoiSearchListener(new OnPoiSearchListenerImpl() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    getV().onPoiOverlayResult(poiResult,i);
                }
            });
            poiSearch.searchPOIAsyn();
    }

    public void setCamera(boolean type) {
        trackLocationImpl.setCameraModel(type);
    }

    public void mapSettings(AMap aMap) {
        trackLocationImpl.mapSettings(aMap,0x101);
        trackLocationImpl.cameraFollow(CAMERA_FOLLOW_INIT);
    }

    public void createTerminal(String s) {
        trackTerminalImpl.createTerminal(s);
    }

    public void checkTerminal() {
        trackTerminalImpl.checkTerminal();
    }

    public void getWeather() {
        new TrackWeatherImpl(this);
    }

    @Override
    public void createTerminalCallback(String s) {
        getV().onCreateTerminalResult(s);
    }

    @Override
    public void checkTerminalCallback(boolean x) {
        getV().onCheckTerminalResult(x);
    }

    @Override
    public void onLatLngCallback(LatLng latLng) {
        getV().onInitLocationResult(latLng);
    }

    @Override
    public void onGetWeatherCallback(@NonNull boolean callback, @Nullable JSONObject obj) {
        if (callback) {
            getV().onGetWeatherResult(obj);
        } else {
            getV().onGetWeatherResult(null);
        }
    }
}
