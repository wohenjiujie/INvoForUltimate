package com.graduationproject.invoforultimate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import com.graduationproject.invoforultimate.initialize.MapSetting;
import com.graduationproject.invoforultimate.service.OnTrackListenerService;
import com.graduationproject.invoforultimate.util.BottomNavigationUtil;
import com.graduationproject.invoforultimate.util.DatabaseUtil;
import com.graduationproject.invoforultimate.util.HttpUtil;
import com.graduationproject.invoforultimate.util.InputUtil;
import com.graduationproject.invoforultimate.service.OnTrackLifecycleListenerService;
import com.graduationproject.invoforultimate.util.ToastUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";

    private static final String TAG = "myLog";

    private MapView mapView = null;
    private AMap aMap = null;
    private AMapLocationClient aMapLocationClient = null;
    private AMapLocationClientOption aMapLocationClientOption = null;
    private MapSetting mapSetting;
    private MyLocationStyle myLocationStyle = new MyLocationStyle();
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationUtil bottomNavigationUtil;
    private TextView startTrack, startGather;
    private AlertDialog.Builder builder;
    private EditText createTrace;
    private Button traceSetting;
    private boolean isServiceRunning = false;
    private boolean isGatherRunning = false;
    private boolean isLocationRunning = false;
    private AMapTrackClient aMapTrackClient;
    private static long TrackID;
    private static final int GATHER_TIME = 5;
    private static double longitude;//经度
    private static double latitude;//纬度
    private static List<LatLng> coordinate = new ArrayList<LatLng>();
    private List<LatLng> allCoordinate = new ArrayList<LatLng>();
    private List<LatLng> latLngs = new ArrayList<LatLng>();
    private Polyline polyline;
    private DatabaseUtil databaseUtil;
    private InitializeTerminal initializeTerminal = new InitializeTerminal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initView(savedInstanceState);
        initListener();
        // 不要使用Activity作为Context传入
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setInterval(GATHER_TIME, 30);


    }

    protected void initUI() {
        mapView = findViewById(R.id.basic_map);
        bottomNavigationView = findViewById(R.id.nav_view);
        startTrack = findViewById(R.id.start_track);
        startGather = findViewById(R.id.start_gather);
        traceSetting = findViewById(R.id.trace_setting);
        createTrace = findViewById(R.id.create_trace);
        //初始化client
        aMapLocationClient = new AMapLocationClient(this.getApplicationContext());
        aMapLocationClientOption = getDefaultOption();
        //设置定位参数
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        // 设置定位监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
    }

    /**
     * 设置定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位模式
        mOption.setInterval(5000);
        mOption.setOnceLocation(false);
        mOption.setOnceLocationLatest(false);//开启连续定位
        return mOption;
    }

    protected void initView(@Nullable Bundle bundle) {
        mapView.onCreate(bundle);// 此方法必须重写
        mapSetting = new MapSetting(mapView, aMap, myLocationStyle);
        mapSetting.initialize();
        startTrack.setVisibility(View.INVISIBLE);
        startGather.setVisibility(View.INVISIBLE);
        traceSetting.setVisibility(View.INVISIBLE);
        createTrace.setVisibility(View.INVISIBLE);
        builder = new AlertDialog.Builder(MainActivity.this);
        bottomNavigationUtil = new BottomNavigationUtil(MainActivity.this, builder, bottomNavigationView, createTrace, traceSetting, startTrack, startGather);
        /*
        * 判断是否第一次接入终端
        * */
        if (!initializeTerminal.checkTerminal(getApplicationContext())) {
            ToastUtil.showToast(this, "no terminal!!");
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("diy");
            progressDialog.show();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
//                http://xiaomu1079.club/infos/query
                HttpUtil.sendOkHttpRequest("http://xiaomu1079.club/infos/query", new com.squareup.okhttp.Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String string = response.body().string();
                        Looper.prepare();
                        ToastUtil.showToast(getApplication(), string);
                        Looper.loop();
                        Log.d(TAG, string);
                        try {
                            JSONArray jsonArray = new JSONArray(string);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String result = jsonObject.getString("count");
//                                ToastUtil.showToast(getApplication(),result);
                                Log.d(TAG, result);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }
    };

    protected void initListener() {
        /*
         * 底部导航栏监听
         * */
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.tools1:
//                        bottomNavigationUtil.ItemSelected(1);
//                        databaseUtil = new DatabaseUtil();
//                        Log.d(TAG, databaseUtil.test());
//                        ToastText(databaseUtil.test());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(0);
                            }
                        }).start();
                        break;
                    case R.id.tools2:
                        bottomNavigationUtil.ItemSelected(2);
                        break;
                    case R.id.tools3:
                        bottomNavigationUtil.ItemSelected(3);
                        OnProcessCallBack(R.string.checkTid);
                        break;
                }
                return true;
            }
        });

        /*
         * 创建轨迹输入框监听
         * */

        createTrace.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //监听回车按下事件
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        InputUtil.hideAllInputMethod(MainActivity.this);
                        if (createTrace.getText().toString().length() == 0) {
//                            ToastText("empty");
                        } else {
                            ToastText(createTrace.getText().toString());
                        }
                        /*
                         * 开始创建轨迹
                         * */
                    }
                }

                return false;
            }
        });

        /*
         * 轨迹设置监听
         * */

        traceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*
         * 开始采集监听
         * */
        startGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isGatherRunning) {
                    aMapTrackClient.stopGather(onTrackLifecycleListener);
                } else {
//                    aMapTrackClient.setTrackId(trackId);
                    aMapTrackClient.startGather(onTrackLifecycleListener);
                }
                if (isLocationRunning) {
                    aMapLocationClient.stopLocation();
                    isLocationRunning = false;
                } else {
                    aMapLocationClient.startLocation();
                    isLocationRunning = true;
                }
            }
        });

        /*
         * 开启服务监听
         * */

        startTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceRunning) {
                    aMapTrackClient.stopTrack(new TrackParam(Constants.ServiceID, Constants.TerminalID), onTrackLifecycleListener);

                } else {
                    startTrack();
                }
            }
        });


    }


    /**
     * 定位监听
     */
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    longitude = aMapLocation.getLongitude();
                    latitude = aMapLocation.getLatitude();
//                    ToastUtil.showToast(MainActivity.this, "info:" + longitude + "\n" + latitude);
                    Log.d(TAG, "latitude:" + latitude);
                    Log.d(TAG, "longitude:" + longitude);
                    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(19));
                    coordinate.add(new LatLng(latitude, longitude));
//                    allCoordinate.addAll(coordinate);
                    Log.d(TAG, "two:" + allCoordinate);
                    polyline = mapView.getMap().addPolyline(new PolylineOptions().
                            addAll(coordinate).width(10).color(Color.argb(255, 1, 1, 1)));


                    /*
                     * test
                     * */
//                    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(4));
                    /*latLngs.add(new LatLng(30.679879,104.064855));//成都
                    latLngs.add(new LatLng(39.90403,116.407525));//北京
                    polyline =mapView.getMap().addPolyline(new PolylineOptions().
                            addAll(latLngs).width(20).color(Color.argb(255, 1, 1, 1)));*/
                }
            }
        }
    };

    private OnTrackLifecycleListener onTrackLifecycleListener = new OnTrackLifecycleListenerService() {
        @Override
        public void onBindServiceCallback(int i, String s) {
            Log.w(TAG, "onBindServiceCallback, status: " + i + ", msg: " + s);
        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.START_GATHER_SUCEE) {
                Toast.makeText(MainActivity.this, "定位采集开启成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
                updateBtnStatus();
            } else if (i == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                Toast.makeText(MainActivity.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
                isGatherRunning = true;
                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStartGatherCallback, status: " + i + ", msg: " + s);
                Toast.makeText(MainActivity.this,
                        "error onStartGatherCallback, status: " + i + ", msg: " + s,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStartTrackCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.START_TRACK_SUCEE || i == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
//                Toast.makeText(MainActivity.this, "启动服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
                updateBtnStatus();
            } else if (i == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
//                Toast.makeText(MainActivity.this, "服务已经启动", Toast.LENGTH_SHORT).show();
                isServiceRunning = true;
                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStartTrackCallback, status: " + i + ", msg: " + s);
                Toast.makeText(MainActivity.this,
                        "error onStartTrackCallback, status: " + i + ", msg: " + s,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
                Toast.makeText(MainActivity.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
                isGatherRunning = false;
                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStopGatherCallback, status: " + i + ", msg: " + s);
                Toast.makeText(MainActivity.this,
                        "error onStopGatherCallback, status: " + i + ", msg: " + s,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStopTrackCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                // 成功停止
                Toast.makeText(MainActivity.this, "停止服务成功", Toast.LENGTH_SHORT).show();
                isServiceRunning = false;
                isGatherRunning = false;
                updateBtnStatus();
            } else {
                Log.w(TAG, "error onStopTrackCallback, status: " + i + ", msg: " + s);
                Toast.makeText(MainActivity.this,
                        "error onStopTrackCallback, status: " + i + ", msg: " + s,
                        Toast.LENGTH_LONG).show();

            }
        }
    };

    private void updateBtnStatus() {
        startTrack.setText(isServiceRunning ? "停止服务" : "启动服务");
        startTrack.setTextColor(isServiceRunning ? 0xFFFFFFFF : 0xFF000000);
        startTrack.setBackgroundResource(isServiceRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
        startGather.setText(isGatherRunning ? "停止采集" : "开始采集");
        startGather.setTextColor(isGatherRunning ? 0xFFFFFFFF : 0xFF000000);
        startGather.setBackgroundResource(isGatherRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
    }

    private void startTrack() {

        aMapTrackClient.addTrack(new AddTrackRequest(Constants.ServiceID, Constants.TerminalID), new OnTrackListenerService() {
            @Override
            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
                if (addTrackResponse.isSuccess()) {
                    TrackID = addTrackResponse.getTrid();
                    TrackParam trackParam = new TrackParam(Constants.ServiceID, Constants.TerminalID);
                    trackParam.setTrackId(TrackID);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        trackParam.setNotification(createNotification());
                    }
                    Log.d(TAG, "TrackID:" + TrackID);
                    ToastUtil.showToast(MainActivity.this, "TrackID:" + TrackID);

                    aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);

                } else {
                    ToastText("网络请求失败，" + addTrackResponse.getErrorMsg());
                }


            }
        });

    }


    @Override
    protected void OnProcessCallBack(int msg) {
        switch (msg) {
            case R.string.checkTid:
//                ToastText("tid is empty");
                /*
                 * 没有创建终端，需要创建终端
                 *
                 * */
                break;
        }
    }

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        Intent nfIntent = new Intent(MainActivity.this, MainActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(MainActivity.this, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("猎鹰sdk运行中")
                .setContentText("猎鹰sdk运行中");
        Notification notification = builder.build();
        return notification;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


}
