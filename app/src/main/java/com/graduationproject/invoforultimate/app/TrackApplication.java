package com.graduationproject.invoforultimate.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.graduationproject.invoforultimate.listener.OnLocationServiceListener;

/**
 * Created by INvo
 * on 2019-11-05.
 */
public class TrackApplication extends Application {
    private static Context context;
    private AMapLocationClientOption aMapLocationClientOption = null;
    private static AMapLocationClient aMapLocationClient = null;
    private static String city,address;
    private static LatLonPoint latLonPoint;
    private static double latitude, longitude;

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        aMapLocationClient = new AMapLocationClient(getContext());
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位模式
        aMapLocationClientOption.setInterval(2000);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.startLocation();
    }

    public static LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public static String getCity() {
        return city;
    }

    public static void getLocation(@Nullable OnLocationServiceListener onLocationServiceListener) {
        aMapLocationClient.setLocationListener(aMapLocation -> {
            if (null != aMapLocation) {
                onLocationServiceListener.getLocation(aMapLocation);
                city = aMapLocation.getCity();
                latLonPoint = new LatLonPoint(aMapLocation.getLongitude(), aMapLocation.getLatitude());
                address = aMapLocation.getStreet();
                latitude=aMapLocation.getLatitude();
                longitude=aMapLocation.getLongitude();
            }
        });
    }

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static Context getContext() {
        return context;
    }

    public static  String getAddress() {
        return address;
    }
}
