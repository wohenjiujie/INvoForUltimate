package com.graduationproject.invoforultimate.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graduationproject.invoforultimate.BaseActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.bean.constants.MainConstants;
import com.graduationproject.invoforultimate.initialize.MapSetting;
import com.graduationproject.invoforultimate.presenter.impl.MainBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.MainViewCallback;
import com.graduationproject.invoforultimate.util.BottomNavigationUtil;
import com.graduationproject.invoforultimate.util.DialogUtil;
import com.graduationproject.invoforultimate.util.InputUtil;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.*;
import static com.graduationproject.invoforultimate.bean.constants.TerminalModuleConstants.*;
import static com.graduationproject.invoforultimate.bean.constants.TrackServiceConstants.*;


public class MainActivity extends BaseActivity<MainViewCallback, MainBuilderImpl, TextureMapView> implements MainViewCallback {
    private static final String TAG = MainConstants.TAG;
    @BindView(R.id.basic_map)
    TextureMapView textureMapView;
    private AMap aMap = null;
    private MapSetting mapSetting;
    private MyLocationStyle myLocationStyle = new MyLocationStyle();
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationUtil bottomNavigationUtil;
    private TextView startFor;//需要更名
    private Chronometer chronometer;
    private static List<LatLng> coordinate = new ArrayList<LatLng>();
    private Polyline polyline;
    private DialogUtil dialogUtil = new DialogUtil();
    private boolean isStart = false;
    private TextView trackSpeed, trackDistance;
    private ImageView trackSignal;


    @Override
    protected TextureMapView loadM(Bundle savedInstanceState) {
        textureMapView.onCreate(savedInstanceState);
        return textureMapView;
    }

    @Override
    protected MainBuilderImpl loadP() {
        return new MainBuilderImpl();
    }

    @Override
    protected MainViewCallback loadV() {
        return this;
    }


    /**
     * 解决空返回会回到桌面的问题
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //when start track cannot return
        if (KeyEvent.KEYCODE_BACK == keyCode && View.VISIBLE == startFor.getVisibility()) {
            //当进入轨迹追踪时
            startFor.setVisibility(View.INVISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            return false;
        } else if (KeyEvent.KEYCODE_BACK == keyCode && View.INVISIBLE == startFor.getVisibility()) {
            //在初始界面时
            dialogUtil.exitDialog(MainActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initControls(Bundle savedInstanceState) {
        mapSetting = new MapSetting(getM(), aMap, myLocationStyle);
        mapSetting.initialize();
        startFor.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.INVISIBLE);
        trackSpeed.setVisibility(View.INVISIBLE);
        trackSignal.setVisibility(View.INVISIBLE);
        trackDistance.setVisibility(View.INVISIBLE);
        getP().checkTerminal();
    }

    @Override
    protected void initView(@NonNull Bundle savedInstanceState) {
        bottomNavigationView = findViewById(R.id.nav_view);
        startFor = findViewById(R.id.start_for);
        chronometer = findViewById(R.id.time_task);
        trackSpeed = this.findViewById(R.id.track_speed);
        trackDistance = this.findViewById(R.id.track_distance);
        trackSignal = this.findViewById(R.id.track_signal);
    }

    @Override
    protected void initListener(Bundle savedInstanceState) {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                /**
                 * 暂定
                 */
                case R.id.tools1:

                    break;

                case R.id.tools2:
                    this.startActivity(new Intent().setClass(getContext(), TrackHistoryActivity.class));
                    break;

                case R.id.tools3:
                    new AlertDialog.Builder(this)
                            .setTitle(BOTTOM_NAVIGATION_TITLE)
                            .setMessage(BOTTOM_NAVIGATION_MESSAGE)
                            .setIcon(R.drawable.ic_launcher_background)
                            .setNegativeButton(BOTTOM_NAVIGATION_NEGATIVE_CHOICE, (dialog, which) -> {
                                startFor.setVisibility(View.INVISIBLE);//start button invisible
                            }).setPositiveButton(BOTTOM_NAVIGATION_POSITIVE_CHOICE, (dialog, which) -> {
                        bottomNavigationView.setVisibility(View.INVISIBLE);//底部导航关闭显示
                        startFor.setVisibility(View.VISIBLE);//start button visible
                    }).setCancelable(false).show();
                    break;
            }
            return true;
        });

        startFor.setOnClickListener(v -> {
            if (!isStart) {
                getP().startTrack(chronometer);
            }
        });

        startFor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isStart) {
                    getP().stopTrack();
                }
                return false;
            }
        });

    }

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
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateTerminalResult(String s) {
        runOnUiThread(() -> {
            if (RESULT_TERMINAL_MSG_EXISTING_ELEMENT.equals(s)) {
                ToastText(s);
                terminalCreateAlterDialog();
            }
            if (RESULT_TERMINAL_MSG_INVALID_PARAMS.equals(s)) {
                ToastText(s);
                terminalCreateAlterDialog();
            }
            if (RESULT_TERMINAL_MSG_SUCCESS.equals(s)) {
                ToastText(s);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateBtnStatus() {
        startFor.setText(isStart ? TRACK_STOP : TRACK_START);
    }

    @Override
    public void onStartTrackResult(int callback, String s) {
        runOnUiThread(() -> {
            if (TRACK_RESULT_START == callback) {
                ToastText(s);
                isStart = true;
                updateBtnStatus();
                chronometer.setVisibility(View.VISIBLE);
                trackSpeed.setVisibility(View.VISIBLE);
                getM().getMap().moveCamera(CameraUpdateFactory.zoomTo(19));
                trackSignal.setVisibility(View.VISIBLE);
                trackDistance.setVisibility(View.VISIBLE);
            }
            if (TRACK_RESULT_STOP == callback) {
                ToastText(s);
                isStart = false;
                updateBtnStatus();
                trackSpeed.setText("0.0 km/h");
                trackDistance.setText("0 m");
                trackDistance.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);
                trackSpeed.setVisibility(View.INVISIBLE);
                trackSignal.setVisibility(View.INVISIBLE);
            }
            if (TRACK_RESULT_FAILURE == callback) {
                ToastText(s);
            }
        });
    }

    @Override
    public void onTrackChangedResult(String s1, String s2) {
        runOnUiThread(() -> {
            if (null == s1) {
                trackSpeed.setText(0.0 + " km/h");
            } else {
                trackSpeed.setText(s1 + " km/h");
            }
            if (null == s2) {
                trackDistance.setText("0.0 m");
            } else {
                trackDistance.setText(s2 + " m");
            }
        });
    }

    @Override
    public void onTrackLocationResult(double longitude, double latitude, int rank) {
        runOnUiThread(() -> {
            trackSignal.setVisibility(View.VISIBLE);
            switch (rank) {
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
            coordinate.add(new LatLng(latitude, longitude));
            polyline = getM().getMap().addPolyline(new PolylineOptions().
                    addAll(coordinate).width(10).color(Color.argb(255, 1, 1, 1)));
        });
    }

    @Override
    public void onTrackUploadResult(boolean x) {
        runOnUiThread(() -> {
            if (x) {
                ToastText(TRACK_UPLOAD);
            } else {
                dialogUtil.checkFalse(MainActivity.this);
            }
        });
    }

    public void terminalCreateAlterDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_tid, null);
        Button button = view.findViewById(R.id.create_tid_btn);
        EditText editText = view.findViewById(R.id.create_tid_edit);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(ALTER_DIALOG_TITLE)
                .setMessage(ALTER_DIALOG_MESSAGE)
                .setView(view)
                .setCancelable(false)
                .show();

        editText.setOnKeyListener((v, keyCode, event) -> {
            //监听回车按下事件
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    InputUtil.hideAllInputMethod(MainActivity.this);//?
                    if (0 == editText.getText().toString().length()) {
                        ToastText(INPUT_EMPTY);
                    }
                }
            }
            return false;
        });

        button.setOnClickListener(v -> {
            if (0 != editText.getText().toString().length()) {
                alertDialog.dismiss();
                getP().createTerminal(editText.getText().toString());
            }
        });
    }

    @Override
    public void onCheckTerminalResult(boolean x) {
        runOnUiThread(() -> {
            if (!x) {
                bottomNavigationView.setVisibility(View.INVISIBLE);
                terminalCreateAlterDialog();
            }
        });
    }
}
