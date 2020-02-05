package com.graduationproject.invoforultimate;

import androidx.annotation.NonNull;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.graduationproject.invoforultimate.app.TrackApplication;
import com.graduationproject.invoforultimate.bean.TrackInfo;
import com.graduationproject.invoforultimate.initialize.GeographicDescription;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import com.graduationproject.invoforultimate.initialize.MapSetting;
import com.graduationproject.invoforultimate.service.OnTrackListenerService;
import com.graduationproject.invoforultimate.service.TrackUpload;
import com.graduationproject.invoforultimate.util.BottomNavigationUtil;
import com.graduationproject.invoforultimate.util.DatabaseUtil;
import com.graduationproject.invoforultimate.util.DialogUtil;
import com.graduationproject.invoforultimate.util.InputUtil;
import com.graduationproject.invoforultimate.service.OnTrackLifecycleListenerService;
import com.graduationproject.invoforultimate.util.UnixUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
    private TextView startTrack, startGather, startFor;//需要更名
    private AlertDialog.Builder builder;
    private EditText createTrace;
    private Button traceSetting;
    private Chronometer chronometer;
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
    private DialogUtil dialogUtil = new DialogUtil();
    private boolean isStart = false;
    private boolean isRunning = false;
    private boolean isGather = false;
    private TrackInfo trackInfo = new TrackInfo();
    private static int timeConsuming;
    private static boolean theFirstTransmit;
    private TrackUpload trackUpload;


    private boolean isLoadingInterface = false;//所有与此相关的代码无效（包括部分BottomNavigationUtil中的代码）
    private TextView trackSpeed, trackDistance;


    private Response response;

    private Handler handler;
    private TimerTask timerTask;
    private Timer timer;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String speed = null;
    private int counts = 0;
    private String distance;
    private String startUnix;
    private String endUnix;
    private ImageView trackSignal;


    private int count = 1;
    private float Speed = 0;
    private float avaSpeed = 0;
    private float sumSpeed = 0;

    private ImageView trackTest;

//    TrackThread trackThread = new TrackThread(getApplicationContext());

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* initUI(sa);
        initView(savedInstanceState);*/
//        initListener();
        // 不要使用Activity作为Context传入
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setInterval(GATHER_TIME, 30);
//        hansa();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && View.VISIBLE == startFor.getVisibility()) {
            //当进入轨迹追踪时
            startFor.setVisibility(View.INVISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
//            dialogUtil.exitDialog(MainActivity.this);
            /**
             * 解决空返回会回到桌面的问题
             */
            return false;

        } else if (KeyEvent.KEYCODE_BACK == keyCode && View.INVISIBLE == startFor.getVisibility()) {
            //在初始界面时
            dialogUtil.exitDialog(MainActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void hansa() {
        ImageView imageView = this.findViewById(R.id.history_map);
    }

    @Override
    protected void initControls(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mapSetting = new MapSetting(mapView, aMap, myLocationStyle);
        mapSetting.initialize();
        startTrack.setVisibility(View.INVISIBLE);
        startGather.setVisibility(View.INVISIBLE);
        traceSetting.setVisibility(View.INVISIBLE);
        createTrace.setVisibility(View.INVISIBLE);
        startFor.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.INVISIBLE);
        trackSpeed.setVisibility(View.INVISIBLE);
        trackSignal.setVisibility(View.INVISIBLE);
//        trackTest.setVisibility(View.VISIBLE);
        builder = new AlertDialog.Builder(MainActivity.this);
        bottomNavigationUtil = new BottomNavigationUtil(MainActivity.this, builder, bottomNavigationView, createTrace, traceSetting, startTrack, startGather, startFor);
        /*
         * 判断是否第一次接入终端
         * */
        if (!initializeTerminal.checkTerminal(getApplicationContext())) {
            dialogUtil.createTerminalID(MainActivity.this, MainActivity.this);
        }
        Long a = initializeTerminal.getTerminal(MainActivity.this);
        Log.d(TAG, "a:" + a);

        /*mapView = findViewById(R.id.basic_map);
        bottomNavigationView = findViewById(R.id.nav_view);
        startTrack = findViewById(R.id.start_track);
        startGather = findViewById(R.id.start_gather);
        traceSetting = findViewById(R.id.trace_setting);
        createTrace = findViewById(R.id.create_trace);
        startFor = findViewById(R.id.start_for);
        chronometer = findViewById(R.id.time_task);
        trackSpeed = this.findViewById(R.id.track_speed);
        trackDistance = this.findViewById(R.id.track_distance);
        trackSignal = this.findViewById(R.id.track_signal);
        //初始化client
        aMapLocationClient = new AMapLocationClient(this.getApplicationContext());
        aMapLocationClientOption = getDefaultOption();
        //设置定位参数
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        // 设置定位监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
        trackTest = this.findViewById(R.id.track_drawable_test);*/
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
    @Override
    protected void initView(@NonNull Bundle savedInstanceState) {
        /*mapView.onCreate(bundle);// 此方法必须重写
        mapSetting = new MapSetting(mapView, aMap, myLocationStyle);
        mapSetting.initialize();
        startTrack.setVisibility(View.INVISIBLE);
        startGather.setVisibility(View.INVISIBLE);
        traceSetting.setVisibility(View.INVISIBLE);
        createTrace.setVisibility(View.INVISIBLE);
        startFor.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.INVISIBLE);
        trackSpeed.setVisibility(View.INVISIBLE);
        trackSignal.setVisibility(View.INVISIBLE);
//        trackTest.setVisibility(View.VISIBLE);
        builder = new AlertDialog.Builder(MainActivity.this);
        bottomNavigationUtil = new BottomNavigationUtil(MainActivity.this, builder, bottomNavigationView, createTrace, traceSetting, startTrack, startGather, startFor);
        *//*
         * 判断是否第一次接入终端
         * *//*
        if (!initializeTerminal.checkTerminal(getApplicationContext())) {
            dialogUtil.createTerminalID(MainActivity.this, MainActivity.this);
        }
        Long a = initializeTerminal.getTerminal(MainActivity.this);
        Log.d(TAG, "a:" + a);
*/


        mapView = findViewById(R.id.basic_map);
        bottomNavigationView = findViewById(R.id.nav_view);
        startTrack = findViewById(R.id.start_track);
        startGather = findViewById(R.id.start_gather);
        traceSetting = findViewById(R.id.trace_setting);
        createTrace = findViewById(R.id.create_trace);
        startFor = findViewById(R.id.start_for);
        chronometer = findViewById(R.id.time_task);
        trackSpeed = this.findViewById(R.id.track_speed);
        trackDistance = this.findViewById(R.id.track_distance);
        trackSignal = this.findViewById(R.id.track_signal);
        //初始化client
        aMapLocationClient = new AMapLocationClient(this.getApplicationContext());
        aMapLocationClientOption = getDefaultOption();
        //设置定位参数
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        // 设置定位监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
        trackTest = this.findViewById(R.id.track_drawable_test);
    }

    private Bitmap getViewBitmap(MapView v) {
        v.clearFocus();
        v.setPressed(false);
        // 能画缓存就返回false
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = null;
        while (cacheBitmap == null) {
            cacheBitmap = v.getDrawingCache();
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }


    @Override
    protected void initListener(Bundle savedInstanceState) {
        /*
         * 底部导航栏监听
         * */
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                /**
                 * 暂定
                 */
                case R.id.tools1:
//                        bottomNavigationUtil.ItemSelected(1);
//                        databaseUtil = new DatabaseUtil();
//                        Log.d(TAG, databaseUtil.test());
//                        ToastText(databaseUtil.test());
//                    new Thread(() -> handler.sendEmptyMessage(0)).start();
//                    ToastUtil.showToast(this,"sendHttp");
                   /* new Thread() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient();
                            String content;
                            Response response;
                            RequestBody requestBody = null;
                            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                            Request request;
                            content = "http://xiaomu1079.club/searchCounts/2029115";
                            request = new Request.Builder().url(content).build();
                            try {
                                response = client.newCall(request).execute();
                                String accept = response.body().string();
//                                JSONObject jsonObject = new JSONObject(accept);

                                Log.d(TAG, accept);
                                ToastUtil.showToast(MainActivity.this, accept);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();*/
//                    initializeTerminal.testTerminal(MainActivity.this);
                    //

                    String a = initializeTerminal.getTerminalName(MainActivity.this);
//                    ToastText(a);
                    /**
                     * 测试speed
                     * 已成功post Speed数据
                     *
                     */

                   /* new Thread() {
                        @Override
                        public void run() {

                            String content = "https://tsapi.amap.com/v1/track/terminal/trsearch?key=b26487968ee70a1647954c49b55828f2&sid="
                                    + Constants.ServiceID + "&tid=211539155&trid=2660&pagesize=999";
                            Request request = new Request.Builder().url(content).get().build();
                            try {
                                response = okHttpClient.newCall(request).execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    String result = response.body().string();
                                    JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                                    JSONArray jsonArray = jsonObject.getJSONArray("tracks");
                                    int counts = Integer.parseInt(jsonArray.getJSONObject(0).getString("counts"));
                                    Log.d(TAG, "counts:" + counts);
                                    String x[] = new String[counts];
                                    for (int i = 0; i <= counts; i++) {
                                        x[i] = jsonArray.getJSONObject(0).getJSONArray("points").getJSONObject(i).getString("speed");
                                        Log.d(TAG, x[i]);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();*/

/**
 * 缩略图制作
 */
//                    View view = MainActivity.this.getWindow().getDecorView().findViewById(R.id.basic_map);
//
                    View view = mapView;
                    view.buildDrawingCache();
                    view.setDrawingCacheEnabled(true);
                    mapView.setDrawingCacheEnabled(true);

                    mapView.buildDrawingCache();
//                    Bitmap bitmap = MainActivity.this.mapView.getDrawingCache();

                    Bitmap bitmap = view.getDrawingCache();
                    trackTest.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] b = byteArrayOutputStream.toByteArray();

                    String aa =   Base64.encodeToString(b, Base64.DEFAULT);


                    Handler mHandler = new Handler();
                    Runnable mResetCache = new Runnable() {
                        @Override
                        public void run() {
                            trackTest.setImageBitmap(null);
                            Bundle bundle = new Bundle();
                            bundle.putString("bitmap", aa);
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            intent.setClass(MainActivity.this, TestActivity.class);
                            startActivity(intent);
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            bitmap.recycle();
//                            mapView.setDrawingCacheEnabled(false);
//                            mapView.destroyDrawingCache();
//                            view.setDrawingCacheEnabled(false);
//                            view.destroyDrawingCache();
                        }
                    };
                    mHandler.postDelayed(mResetCache, 500);
                    break;
                /*
                 * 查询轨迹历史记录
                 * */
                case R.id.tools2:
                    bottomNavigationUtil.ItemSelected(2);
                    break;
                /**
                 * 轨迹服务开启
                 */
                case R.id.tools3:
                    bottomNavigationUtil.ItemSelected(3);
                    /*if (startFor.getVisibility()==View.VISIBLE) {
                    }
                    bottomNavigationUtil.setListener(new OnLoadingStatus() {
                        @Override
                        public void isLoading(boolean x) {
                            Log.d(TAG, "x:" + x);
                            isLoadingInterface = x;
                        }
                    });*/
                    OnProcessCallBack(R.string.checkTid);
                    break;
            }
            return true;
        });


        /*
         * createTrace & traceSetting暂时不需要
         * */

        /*
         * 创建轨迹输入框监听
         * */

        createTrace.setOnKeyListener((v, keyCode, event) -> {
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
                    theFirstTransmit = true;
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


        /**
         * 开启所有
         * 1、开启服务--isServiceRunning default == false
         * 2、开启采集和计时
         * 3、结束操作停止服务和location
         */
        startFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart) {
                    timer = new Timer();

                    handler = new Handler() {

                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            sendEmptyMessageDelayed(1, 2222);
                        }
                    };
                    startTrack();
                    /*theFirstTransmit = true;
                    chronometer.setVisibility(View.VISIBLE);
                    chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
                    int hour = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
                    chronometer.setFormat("0" + hour + ":%s");
                    aMapTrackClient.startGather(onTrackLifecycleListener);
                    aMapLocationClient.startLocation();*/
                    chronometer.start();
                    UnixUtil unixUtil = new UnixUtil();
                    trackInfo.setDate(unixUtil.getDate());
                    Log.d(TAG, unixUtil.getDate());
                }
            }
        });

        startFor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isStart) {
                    aMapTrackClient.stopGather(onTrackLifecycleListener);
                    /*
                     * 考虑是否需要关闭服务
                     * */
                    aMapTrackClient.stopTrack(new TrackParam(Constants.ServiceID, Constants.TerminalID), onTrackLifecycleListener);
                    chronometer.stop();
                    timer.cancel();
                    trackSpeed.setText("0.0 km/h");
                    theFirstTransmit = true;//可能不需要在这里重置
                    chronometer.setVisibility(View.INVISIBLE);
                    trackSpeed.setVisibility(View.INVISIBLE);
                    aMapLocationClient.stopLocation();
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "trackInfo.getServiceID():" + trackInfo.getServiceID());
                    Log.d(TAG, "trackInfo.getTerminalID():" + trackInfo.getTerminalID());
                    Log.d(TAG, "trackInfo.getTrackID():" + trackInfo.getTrackID());
                    Log.d(TAG, (String) trackInfo.getDesc());
                    Log.d(TAG, (String) trackInfo.getDate());
                    Log.d(TAG, (String) trackInfo.getTime());
                    Log.d(TAG, "trackInfo.getDistance():" + trackInfo.getDistance());
                    Log.d(TAG, "trackInfo.getTimeConsuming():" + trackInfo.getTimeConsuming());
                    int a = (int) trackInfo.getTimeConsuming();

                    /**
                     * 2020.01.18
                     * get Distance
                     *
                     */

                    /*Thread thread = new Thread(() -> {
                        String result = null;
                        String speed = null;
                        long sumSpeed = 0;
                        long avaSpeed;
                        initializeTerminal = new InitializeTerminal();
                        String content = "https://tsapi.amap.com/v1/track/terminal/trsearch?key=b26487968ee70a1647954c49b55828f2&sid="
                                + Constants.ServiceID + "&tid=" + trackInfo.getTerminalID()
                                + "&trid=" + trackInfo.getTrackID() + "&pagesize=999";
                        Request request = new Request.Builder().url(content).get().build();
                        try {
                            response = okHttpClient.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                result = response.body().string();
                                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                                JSONArray jsonArray = jsonObject.getJSONArray("tracks");
                                String counts = jsonArray.getJSONObject(0).getString("counts");
                                int count = Integer.parseInt(counts);
                                for (int i = 0; i <= count; i++) {
                                    speed = jsonArray.getJSONObject(0).getJSONArray("points").getJSONObject(i).getString("speed");
                                    long temSpeed = new Double(Double.parseDouble(speed)).longValue();
                                    Log.d(TAG, "temSpeed:" + temSpeed);
                                    sumSpeed += temSpeed;
//                                    Log.d(TAG, "sumSpeed:" + sumSpeed);
                                }
                                avaSpeed = sumSpeed / count;
                                Log.d(TAG, "count:" + count);
                                Log.d(TAG, "avaSpeed:" + avaSpeed);
                                String resultSpeed = Long.toString(avaSpeed);
                                Log.d(TAG, resultSpeed);
                                trackInfo.setAvaPace(resultSpeed);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }*/


//                    Log.d(TAG, "a:" + a);
                    trackUpload = new TrackUpload(trackInfo);
//                    trackUpload.setCheck();
                    //
                    /**
                     * 后期取消upload相关代码对第一次验证的相关判断
                     *
                     */
                    if (trackUpload.isCheck()) {
                        /**
                         * 2020.01.18 modify
                         * 测试需不需要判断
                         *
                         */
                        /* if (initializeTerminal.checkFirstPosting(MainActivity.this)) {
                         *//**
                         * 如果已经上传
                         *//*
                            trackUpload.upload();
                            trackUpload.addTrackCounts();
                        } else {
                            *//**
                         * 第一次上传情况
                         *
                         *//*
//                            initializeTerminal.createTrackCounts(MainActivity.this, (Long) trackInfo.getTerminalID());//后期不需依赖此判断
                            trackUpload.upload();
                            trackUpload.addTrackCounts();

                    }*/
                        trackUpload.upload();
                        trackUpload.addTrackCounts();

                    } else {
                        /**
                         * 当记录时间小于1min时，不执行上传操作，直接进入回放界面
                         * alterDialog提示
                         */
                      /*  trackUpload.upload();
                        trackUpload.addTrackCounts();*/

//                        trackUpload.upload();
//                        trackUpload.addTrackCounts();
                    dialogUtil.checkFalse(MainActivity.this);


                    }
//                    boolean a = (boolean) trackUpload.isCheck();//test
                }
                return false;
            }
        });
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                int temp0 = Integer.parseInt(chronometer.getText().toString().split(":")[0]);
                int temp1 = Integer.parseInt(chronometer.getText().toString().split(":")[1]);
                int temp2 = Integer.parseInt(chronometer.getText().toString().split(":")[2]);
                timeConsuming = temp0 * 3600 + temp1 * 60 + temp2;
                String timeConsumingStr = chronometer.getText().toString();
                trackInfo.setTimeConsuming(timeConsuming);
                trackInfo.setTime(timeConsumingStr);
//                Log.d(TAG, timeConsuming);
//                Log.d(TAG, "temp:" + timeConsuming);
            }
        });


    }

    private GeographicDescription geographicDescription;

    /**
     * 发送地理位置描述
     * 只执行第一次发送的数据
     *
     * @param s
     */
    void transmitGeographicDescription(String s) {
        if (theFirstTransmit) {
            Log.d(TAG, "transmitGeographicDescription: yici");
            /*geographicDescription = new GeographicDescription();
            geographicDescription.setString(s);*/
            trackInfo.setDesc(s);
            theFirstTransmit = false;
        }
    }

    //    static int theFirstTransmit;
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
//                    Log.d(TAG, "latitude:" + latitude);
//                    Log.d(TAG, "longitude:" + longitude);
                    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(19));
                    coordinate.add(new LatLng(latitude, longitude));
//                    allCoordinate.addAll(coordinate);
//                    Log.d(TAG, "two:" + allCoordinate);
                    polyline = mapView.getMap().addPolyline(new PolylineOptions().
                            addAll(coordinate).width(10).color(Color.argb(255, 1, 1, 1)));

                    /*
                     * 获取地理位置描述
                     * */
                   /* Log.d(TAG, aMapLocation.getProvince());
                    Log.d(TAG, aMapLocation.getCity());
                    Log.d(TAG, aMapLocation.getDistrict());
                    Log.d(TAG, aMapLocation.getStreet());*/
                    String address = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
//                    theFirstTransmit = theFirstTransmit + 1;
                    if (address.length() > 0) {
                        transmitGeographicDescription(address);
                    }
                    /*Speed = aMapLocation.getSpeed();//速度显示在textView上
                    sumSpeed += Speed;
                    count += 1;*/

                    /**
                     * get gps signal rank
                     *
                     */
                    Log.d(TAG, "x:" + aMapLocation.getGpsAccuracyStatus());
                    trackSignal.setVisibility(View.VISIBLE);

                    switch (aMapLocation.getGpsAccuracyStatus()) {

                        case 1:

                            trackSignal.setImageResource(R.drawable.gps_signal_good);
                            break;
                        case 0:
                            trackSignal.setImageResource(R.drawable.gps_signal_bad);

                            break;
                        case -1:
                            trackSignal.setImageResource(R.drawable.gps_signal_unknown);

                            break;

                    }
//                    int x = aMapLocation.getGpsAccuracyStatus();

//                    Log.d(TAG, "speed:" + speed);
//                    Log.d(TAG, address + "\n" + address.length());

                    /*CoordinateConverter converter = new CoordinateConverter(MainActivity.this);
//                    converter.
                    DPoint dPoint1 = new DPoint(longitude, latitude);
                    DPoint dPoint2 = new DPoint(0, 0);
                    DPoint dPoint3 = new DPoint(0, 0);
                    if (dPoint2.equals(dPoint3)) {
                        float dis = converter.calculateLineDistance(dPoint1, dPoint2);
                        dPoint2 = dPoint1;
                        Log.d(TAG, "dis:" + dis);
                    }*/
                    /*float distance = AMapUtils.calculateLineDistance(new LatLng(longitude, latitude), new LatLng(108.316948, 22.824577));
                    int a = (int) distance;
                    Log.d(TAG, "distance:" + a);
                    Log.d(TAG, "distance:" + distance);*/

//                    Log.d(TAG, "theFirstTransmit:" + theFirstTransmit);
//                    Log.d(TAG, "aMapLocation.getSpeed():" + aMapLocation.getSpeed());


//                    ToastText(address + "\n" + address.length());



                    /*
                     * test
                     * */
//                    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(4));
                    /*latLngs.add(new LatLng(30.679879,104.064855));//成都
                    latLngs.add(new LatLng(39.90403,116.407525));//北京
                    polyline =mapView.getMap().addPolyline(new PolylineOptions().
                            addAll(latLngs).width(20).color(Color.argb(255, 1, 1, 1)));*/
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e(TAG, "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }

    };

    private OnTrackLifecycleListener onTrackLifecycleListener = new OnTrackLifecycleListenerService() {
        @Override
        public void onBindServiceCallback(int i, String s) {
//            Log.w(TAG, "onBindServiceCallback, status: " + i + ", msg: " + s);
        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.START_GATHER_SUCEE) {
//                Toast.makeText(MainActivity.this, "定位采集开启成功", Toast.LENGTH_SHORT).show();
                /*isGather = true;


                isGatherRunning = true;
                updateBtnStatus();*/
            } else if (i == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
//                Toast.makeText(MainActivity.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
                /*isGather = true;

                isGatherRunning = true;
                updateBtnStatus();*/
            } else {
                Log.w(TAG, "error onStartGatherCallback, status: " + i + ", msg: " + s);
                Toast.makeText(MainActivity.this,
                        "error onStartGatherCallback, status: " + i + ", msg: " + s,
                        Toast.LENGTH_LONG).show();
            }
        }

        private void showNetErrorHint(String errorMsg) {
            Toast.makeText(MainActivity.this, "网络请求失败，错误原因: " + errorMsg, Toast.LENGTH_SHORT).show();
        }

        /**
         * 开启服务回调成功
         * 计时器的开始
         * 循环计数器的开始
         *
         * @param i
         * @param s
         */

        @Override
        public void onStartTrackCallback(int i, String s) {
            if (i == ErrorCode.TrackListen.START_TRACK_SUCEE || i == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
//                Toast.makeText(MainActivity.this, "启动服务成功", Toast.LENGTH_SHORT).show();
                //

//                isServiceRunning = true;
                isStart = true;
                updateBtnStatus();//@Deprecated
                theFirstTransmit = true;
                chronometer.setVisibility(View.VISIBLE);
                trackSpeed.setVisibility(View.VISIBLE);
                /*timer = new Timer();

                handler = new Handler() {

                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        sendEmptyMessageDelayed(1, 2222);
                    }
                };*/


                /**
                 *
                 * 后期更新发送频率
                 */
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
//                        timer.schedule(timerTask, 5000,5000);
                        String content = "https://tsapi.amap.com/v1/track/terminal/trsearch?key=b26487968ee70a1647954c49b55828f2&sid="
                                + Constants.ServiceID + "&tid=" + initializeTerminal.getTerminal(TrackApplication.getContext()) + "&trid=" + TrackID + "&pagesize=999";
                        Request request = new Request.Builder().url(content).get().build();
                        try {
                            response = okHttpClient.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                String result = response.body().string();
                                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                                JSONArray jsonArray = jsonObject.getJSONArray("tracks");
                                String points = jsonArray.getJSONObject(0).getString("counts");

                                //是否需要判断counts是否为空

                                counts = Integer.parseInt(jsonArray.getJSONObject(0).getString("counts"));
                                Log.d(TAG, "counts:" + counts);
                                speed = jsonArray.getJSONObject(0)
                                        .getJSONArray("points")
                                        .getJSONObject(counts - 1)
                                        .getString("speed");

                                startUnix = jsonArray.getJSONObject(0)
                                        .getJSONObject("startPoint")
                                        .getString("locatetime");

                                endUnix = jsonArray.getJSONObject(0)
                                        .getJSONObject("endPoint")
                                        .getString("locatetime");

                                distance = jsonArray.getJSONObject(0)
                                        .getString("distance");
                                trackInfo.setDistance(distance);
                                Log.d(TAG, distance);
                                /*if (null==speed||null==points||counts==0) {
                                    trackSpeed.setText("0.0 km/h");
                                }*/

//                                aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.ServiceID,initializeTerminal.getTerminalName(TrackApplication.getContext())), new TrackHistoryListener() {
//
//                                    @Override
//                                    public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
//                                        if (queryTerminalResponse.isSuccess()) {
////                            final long a = 1573294314852L;
////                            final long b = 1573294764853L;
////                            int id = 1718;
//                                            if (queryTerminalResponse.isTerminalExist()) {
//                                                String test = initializeTerminal.getTerminalName(TrackApplication.getContext());
//                                                Log.d("TrackReplayActivity", test);
//                                                long tid = queryTerminalResponse.getTid();
//                                                // 搜索最近12小时以内上报的属于某个轨迹的轨迹点信息，散点上报不会包含在该查询结果中
//                                                QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
//                                                        Constants.ServiceID,
//                                                        initializeTerminal.getTerminal(getContext()),
//                                                        TrackID,     // 轨迹id，不指定，查询所有轨迹，注意分页仅在查询特定轨迹id时生效，查询所有轨迹时无法对轨迹点进行分页
//                                                        Long.valueOf(startUnix),
//                                                        Long.valueOf(endUnix),
//                                                        0,      // 不启用去噪
//                                                        0,   // 绑路
//                                                        0,      // 不进行精度过滤
//                                                        DriveMode.DRIVING,  // 当前仅支持驾车模式
//                                                        0,     // 距离补偿
//                                                        5000,   // 距离补偿，只有超过5km的点才启用距离补偿
//                                                        1,  // 结果应该包含轨迹点信息
//                                                        1,  // 返回第1页数据，但由于未指定轨迹，分页将失效
//                                                        100    // 一页不超过100条
//                                                );
//                                                aMapTrackClient.queryTerminalTrack(queryTrackRequest, new TrackHistoryListener() {
//                                                    @Override
//                                                    public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
//                                                        if (queryTrackResponse.isSuccess()) {
//                                                            List<Track> tracks = queryTrackResponse.getTracks();
//                                                            if (tracks != null && !tracks.isEmpty()) {
//                                                                for (Track track : tracks) {
//                                                                    distance = track.getDistance();
//                                                                    Log.d(TAG, "distance:" + distance);
//                                                                }
//
//                                                            }
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                });


                                /*String x[]=new String[counts];
                                for (int i=0; i <= counts; i++) {
                                    x[i] = jsonArray.getJSONObject(0).getJSONArray("points").getJSONObject(i).getString("speed");
                                    Log.d(TAG, x[i]);
                                }*/
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (null == speed) {
                                    trackSpeed.setText(0.0 + " km/h");
                                } else {
                                    trackSpeed.setText(speed + " km/h");
                                }
                                if (null == distance) {
                                    trackDistance.setText("0.0 m");
                                } else {
                                    trackDistance.setText(distance + " m");
                                }
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 5000, 7000);


                chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
                int hour = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
                chronometer.setFormat("0" + hour + ":%s");
                aMapTrackClient.startGather(onTrackLifecycleListener);
                aMapLocationClient.startLocation();


            } else if (i == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
//                Toast.makeText(MainActivity.this, "服务已经启动", Toast.LENGTH_SHORT).show();
                isStart = true;
//                isServiceRunning = true;
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
//                Toast.makeText(MainActivity.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
                /*isGatherRunning = false;
                isGather = false;*/

//                updateBtnStatus();
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
//                Toast.makeText(MainActivity.this, "停止服务成功", Toast.LENGTH_SHORT).show();
                isStart = false;
               /* isServiceRunning = false;
                isGatherRunning = false;
                isGather = false;*/
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
        startFor.setText(isStart ? R.string.end : R.string.start);


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
                    trackInfo.setTrackID(TrackID);
                    trackInfo.setServiceID(Constants.ServiceID);
                    trackInfo.setTerminalID(initializeTerminal.getTerminal(MainActivity.this));//tid得从sp获取
                    TrackParam trackParam = new TrackParam(Constants.ServiceID, Constants.TerminalID);
                    trackParam.setTrackId(TrackID);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        trackParam.setNotification(createNotification());
                    }
                    Log.d(TAG, "TrackID:" + TrackID);
//                    ToastUtil.showToast(MainActivity.this, "TrackID:" + TrackID);
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
