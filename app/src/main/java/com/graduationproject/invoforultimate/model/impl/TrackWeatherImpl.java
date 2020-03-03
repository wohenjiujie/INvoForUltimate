package com.graduationproject.invoforultimate.model.impl;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.graduationproject.invoforultimate.listener.OnTrackWeatherSearchListenerImpl;
import com.graduationproject.invoforultimate.presenter.MainBuilderPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;

/**
 * Created by INvo
 * on 2020-02-28.
 */
public class TrackWeatherImpl {
    private MainBuilderPresenter mainBuilderPresenter;
    private AMapLocationClient aMapLocationClient = null;
    public TrackWeatherImpl(@Nullable MainBuilderPresenter mainBuilderPresenter) {
        this.mainBuilderPresenter = mainBuilderPresenter;
        getWeatherInfo();
    }

    private void getWeatherInfo() {
        aMapLocationClient = new AMapLocationClient(getContext());
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                WeatherSearchQuery weatherSearchQuery = new WeatherSearchQuery(aMapLocation.getCity()
                        , WeatherSearchQuery.WEATHER_TYPE_LIVE);
                WeatherSearch weatherSearch = new WeatherSearch(getContext());
                weatherSearch.setOnWeatherSearchListener(new OnTrackWeatherSearchListenerImpl() {
                    @Override
                    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                        if (1000 == i) {
                            if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                                LocalWeatherLive liveResult = localWeatherLiveResult.getLiveResult();
                                try {
                                    mainBuilderPresenter.onGetWeatherCallback(true,new JSONObject()
                                            .put("city", liveResult.getCity())
                                            .put("weather", liveResult.getWeather())
                                            .put("temperature", liveResult.getTemperature()+"°"));
                                    aMapLocationClient.stopLocation();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            mainBuilderPresenter.onGetWeatherCallback(false, null);
                            aMapLocationClient.stopLocation();
                        }
                    }
                });
                weatherSearch.setQuery(weatherSearchQuery);
                weatherSearch.searchWeatherAsyn();
            }
        });

        aMapLocationClient.startLocation();
    }
}
