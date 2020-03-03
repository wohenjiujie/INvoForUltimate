package com.graduationproject.invoforultimate.model.impl;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.entity.bean.TrackLatLng;
import com.graduationproject.invoforultimate.listener.OnLocationServiceListener;
import com.graduationproject.invoforultimate.listener.OnTrackLifecycleListenerImpl;
import com.graduationproject.invoforultimate.listener.OnTrackListenerImpl;
import com.graduationproject.invoforultimate.presenter.RecordBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.TrackPresenter;
import com.graduationproject.invoforultimate.utils.TerminalUtil;
import com.graduationproject.invoforultimate.entity.bean.TrackInfo;
import com.graduationproject.invoforultimate.service.TrackThread;
import com.graduationproject.invoforultimate.ui.activity.MainActivity;
import com.graduationproject.invoforultimate.utils.UnixUtil;

import java.io.ByteArrayOutputStream;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLocation;
import static com.graduationproject.invoforultimate.entity.constants.TrackServiceConstants.*;

/**
 * Created by INvo
 * on 2020-02-08.
 */
public class TrackServiceImpl {
    private AMapTrackClient aMapTrackClient;
    private boolean theFirstTransmit = false;
    private TrackInfo trackInfo = new TrackInfo();
    private TrackParam trackParam;
    private Chronometer chronometer;
    UnixUtil unixUtil = new UnixUtil();
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private RecordBuilderPresenter recordBuilderPresenter;
    private TrackLatLng trackLatLng;
    private CoordinateConverter coordinateConverter = new CoordinateConverter(getContext());
    private float sum = 0;

    private OnTrackLifecycleListenerImpl onTrackLifecycleListener = new OnTrackLifecycleListenerImpl() {
        @Override
        public void onStartTrackCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.START_TRACK_SUCEE || i == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                recordBuilderPresenter.onTrackCallback(TRACK_RESULT_START, TRACK_RESULT_START_RESULT);
                chronometer.start();
                trackLatLng = new TrackLatLng();
                trackInfo.setDate(unixUtil.getDate());
                chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
                int hour = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
                chronometer.setFormat("0" + hour + ":%s");
                aMapTrackClient.startGather(onTrackLifecycleListener);
            } else {
                recordBuilderPresenter.onTrackCallback(TRACK_RESULT_FAILURE, TRACK_RESULT_FAILURE_RESULT_);
            }
        }

        @Override
        public void onStopTrackCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                theFirstTransmit = false;
                sum = 0;
                recordBuilderPresenter.onTrackCallback(TRACK_RESULT_STOP, TRACK_RESULT_STOP_RESULT);
            }
        }
    };

    void init() {
        getLocation(aMapLocation -> {
            if (null != aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    Log.d(TAG, "aMapLocation.getSpeed():" + aMapLocation.getSpeed());
                    Log.d(TAG, "aMapLocation.getAltitude():" + aMapLocation.getAltitude());
                    double longitude = aMapLocation.getLongitude();
                    double latitude = aMapLocation.getLatitude();
                    DPoint dPoint = new DPoint();
                    dPoint.setLatitude(latitude);
                    dPoint.setLongitude(longitude);
                    if (!theFirstTransmit) {
                        trackLatLng.setStartDPoint(dPoint);
                    }
                    trackLatLng.setEndDPoint(dPoint);
                    float distance = coordinateConverter.calculateLineDistance(trackLatLng.getStartDPoint(), trackLatLng.getEndDPoint());
                    sum += distance;
                    trackLatLng.setStartDPoint(trackLatLng.getEndDPoint());
                    trackLatLng.setEndDPoint(null);
                    trackInfo.setDistance(String.format("%.1f", sum));
                    recordBuilderPresenter.onTrackChangedCallback(String.valueOf(aMapLocation.getSpeed()), String.format("%.1f", sum), String.valueOf(aMapLocation.getAltitude()));
                    recordBuilderPresenter.onTrackLocationCallback(longitude, latitude, aMapLocation.getGpsAccuracyStatus());
                    String address = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
                    if (address.length() > 0 && !theFirstTransmit) {
                        trackInfo.setDesc(address);
                        theFirstTransmit = true;
                    }
                    Log.d(TAG, address);
                }
            }
        });
    }


    public TrackServiceImpl(Chronometer chronometer, TrackPresenter trackPresenter) {
        this.recordBuilderPresenter = (RecordBuilderPresenter) trackPresenter;
        aMapTrackClient = new AMapTrackClient(getContext());
        aMapTrackClient.setInterval(GATHER_TIME, 30);
        init();
        this.chronometer = chronometer;
        chronometer.setOnChronometerTickListener(chronometer1 -> {
            int temp0 = Integer.parseInt(chronometer1.getText().toString().split(":")[0]);
            int temp1 = Integer.parseInt(chronometer1.getText().toString().split(":")[1]);
            int temp2 = Integer.parseInt(chronometer1.getText().toString().split(":")[2]);
            int timeConsuming = temp0 * 3600 + temp1 * 60 + temp2;
            String timeConsumingStr = chronometer1.getText().toString();
            trackInfo.setTimeConsuming(timeConsuming);
            trackInfo.setTime(timeConsumingStr);
        });
    }

    public void onUploadTrackCheck() {
        new TrackThread(UPLOAD_TRACK_INFO, trackInfo).start();
    }

    public void onStopTrack(Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.WEBP, 1, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        trackInfo.setBitmap(org.apache.commons.codec.binary.Base64.encodeBase64String(bytes));
        aMapTrackClient.stopGather(onTrackLifecycleListener);
        aMapTrackClient.stopTrack(new TrackParam(SERVICE_ID, TerminalUtil.getTerminal()), onTrackLifecycleListener);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
        recordBuilderPresenter.onTrackUploadCallback((int) trackInfo.getTimeConsuming() >= 60 ? true : false);
    }

    public void onStartTrack() {
        trackInfo.setTerminalID(TerminalUtil.getTerminal());
        trackInfo.setServiceID(SERVICE_ID);
        aMapTrackClient.addTrack(new AddTrackRequest((long) trackInfo.getServiceID(), (long) trackInfo.getTerminalID()), new OnTrackListenerImpl() {
            @Override
            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
                if (addTrackResponse.isSuccess()) {
                    trackInfo.setTrackID(addTrackResponse.getTrid());
                    trackParam = new TrackParam((long) trackInfo.getServiceID(), (long) trackInfo.getTerminalID());
                    trackParam.setTrackId((long) trackInfo.getTrackID());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        trackParam.setNotification(createNotification());
                    }
                    aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
                } else {
                    recordBuilderPresenter.onTrackCallback(TRACK_RESULT_FAILURE, TRACK_RESULT_FAILURE_RESPONSE + addTrackResponse.getErrorMsg());
                }
            }
        });
    }

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(getContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(getContext());
        }
        Intent nfIntent = new Intent(getContext(), MainActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(getContext(), 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("猎鹰sdk运行中")
                .setContentText("猎鹰sdk运行中");
        Notification notification = builder.build();
        return notification;
    }
}



