package com.graduationproject.invoforultimate.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graduationproject.invoforultimate.BaseActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.entity.constants.MainConstants;
import com.graduationproject.invoforultimate.listener.TrackScreenShotImpl;
import com.graduationproject.invoforultimate.utils.TrackDialog;
import com.graduationproject.invoforultimate.presenter.impl.MainBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.impl.MainViewCallback;
import com.graduationproject.invoforultimate.utils.InputUtil;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.*;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.graduationproject.invoforultimate.R2.id.basic_map;
import static com.graduationproject.invoforultimate.R2.id.nav_view;
import static com.graduationproject.invoforultimate.R2.id.time_task;
import static com.graduationproject.invoforultimate.R2.id.track_camera;
import static com.graduationproject.invoforultimate.R2.id.track_controller;
import static com.graduationproject.invoforultimate.R2.id.track_distance;
import static com.graduationproject.invoforultimate.R2.id.track_signal;
import static com.graduationproject.invoforultimate.R2.id.track_speed;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.*;
import static com.graduationproject.invoforultimate.entity.constants.TerminalModuleConstants.*;
import static com.graduationproject.invoforultimate.entity.constants.TrackServiceConstants.*;

public class MainActivity extends BaseActivity<MainViewCallback, MainBuilderImpl, TextureMapView> implements MainViewCallback {
    @BindView(basic_map)
    TextureMapView textureMapView;
    @BindView(nav_view)
    BottomNavigationView bottomNavigationView;
    @BindView(track_signal)
    ImageView trackSignal;
    @BindView(track_distance)
    TextView trackDistance;
    @BindView(time_task)
    Chronometer chronometer;
    @BindView(track_controller)
    TextView trackController;
    @BindView(track_speed)
    TextView trackSpeed;
    @BindView(track_camera)
    CheckBox trackCamera;
    private static final String TAG = MainConstants.TAG;
    private static List<LatLng> coordinate = new ArrayList<>();
    private static Polyline polyline;
    private static boolean isStart = false;
    private static boolean isLocate = false;
    private static List<Polyline> polyLines = new LinkedList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @CheckResult
    @Override
    protected TextureMapView loadMap(Bundle savedInstanceState) {
        textureMapView.onCreate(savedInstanceState);
        return textureMapView;
    }

    @CheckResult
    @Override
    protected MainBuilderImpl loadP() {
        return new MainBuilderImpl();
    }

    @CheckResult
    @Override
    protected MainViewCallback loadV() {
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && View.VISIBLE == trackController.getVisibility()) {
            if (isStart) {
                new TrackDialog(this, DIALOG_STOP_TRACK)
                        .setPositiveButton(DIALOG_POSITIVE_CHOICE, (dialog, which) -> getMap().getMap().getMapScreenShot(new TrackScreenShotImpl() {
                            @Override
                            public void onMapScreenShot(Bitmap bitmap) {
                                getP().stopTrack(bitmap);
                            }
                        })).setNegativeButton(DIALOG_NEGATIVE_CHOICE, (dialog, which) -> dialog.dismiss()).show();
            } else {
                trackController.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
            return false;
        } else if (KeyEvent.KEYCODE_BACK == keyCode && View.INVISIBLE == trackController.getVisibility()) {
            new TrackDialog(this, DIALOG_EXIT_APP).setPositiveButton(DIALOG_POSITIVE_CHOICE, (dialog, which) -> {
                finish();
                System.exit(0);
            }).setNegativeButton(DIALOG_NEGATIVE_CHOICE, (dialog, which) -> dialog.dismiss()).show();
        }
        return super.onKeyDown(keyCode, event);
    }
    public static String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void initControls(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 1);
        }
        getP().checkTerminal();
//        getP().mapSettings(getMap().getMap());
        String sha1 = sHA1(getContext());
        Log.d(TAG, sha1);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                /*
                 * 暂定
                 */
                case R.id.tools1:
                    ToastText("No Function");
                    break;

                case R.id.tools2:
                    this.startActivity(new Intent().setClass(getContext(), TrackHistoryActivity.class));
                    break;

                case R.id.tools3:
                    new TrackDialog(this, DIALOG_START_TRACK)
                            .setPositiveButton(DIALOG_POSITIVE_CHOICE, (dialog, which) -> {
                                bottomNavigationView.setVisibility(View.INVISIBLE);
                                trackController.setVisibility(View.VISIBLE);
                            })
                            .setNegativeButton(DIALOG_NEGATIVE_CHOICE, (dialog, which) -> dialog.dismiss()).show();
                    break;
            }
            return true;
        });
    }

    @MainThread
    @OnLongClick(track_controller)
    @NonNull
    public void trackStop() {
        if (isStart) {
            isLocate = false;
            getMap().getMap().getMapScreenShot(new TrackScreenShotImpl() {
                @Override
                public void onMapScreenShot(Bitmap bitmap) {
                    getP().stopTrack(bitmap);
                }
            });
        }
    }

    @OnCheckedChanged(track_camera)
    public void setCameraModel(boolean isChecked) {
        trackCamera.setText(isChecked ? CHECK_BOX_CAMERA : CHECK_BOX_MARKER);
        getP().setCamera(isChecked);
    }

    @OnClick(track_controller)
    @NonNull
    public void trackStart() {
        if (!isStart) {
            isLocate = false;
            getP().startTrack(chronometer);
        }
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
        trackController.setText(isStart ? TRACK_STOP : TRACK_START);
        chronometer.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
        trackDistance.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
        trackSpeed.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
        trackSignal.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
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
    public void onTrackResult(int callback, String s) {
        runOnUiThread(() -> {
            if (TRACK_RESULT_START == callback) {
                ToastText(s);
                isStart = true;
                updateBtnStatus();
                getMap().getMap().moveCamera(CameraUpdateFactory.zoomTo(21));
            }
            if (TRACK_RESULT_STOP == callback) {
                isStart = false;
                updateBtnStatus();
                trackSpeed.setText("0.0 km/h");
                trackDistance.setText("0 m");
                getMap().getMap().moveCamera(CameraUpdateFactory.zoomTo(16));
                for (Polyline p : polyLines) {
                    p.remove();
                }
                coordinate.clear();
                polyLines.clear();
            }
            if (TRACK_RESULT_FAILURE == callback) {
                ToastText(s);
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
            polyline = getMap().getMap().addPolyline(new PolylineOptions().
                    addAll(coordinate).width(10).color(Color.argb(255, 65, 105, 225)));
            polyLines.add(polyline);
        });
    }

    @Override
    public void onTrackUploadResult(boolean x) {
        runOnUiThread(() -> {
            if (x) {
                ToastText(TRACK_UPLOAD);
            } else {
                new TrackDialog(this, DIALOG_TRACK_NOT_UPLOAD)
                        .setPositiveButton(ALTER_DIALOG_POSITIVE, (dialog, which) -> dialog.dismiss()).show();
            }
        });
    }

    @Override
    public void onInitLocationResult(LatLng latLng, @NonNull Integer type) {
        if (CAMERA_FOLLOW_INIT == type) {
            if (!isLocate) {
                getMap().getMap().moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                isLocate = true;
            }
        }
        if (CAMERA_FOLLOW_START == type) {
            getMap().getMap().moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
        if (CAMERA_FOLLOW_STOP == type) {
            if (!isLocate) {
                getMap().getMap().moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                isLocate = true;
            }
        }
    }

    public void terminalCreateAlterDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_tid, null);
        Button button = view.findViewById(R.id.create_tid_btn);
        EditText editText = view.findViewById(R.id.create_tid_edit);
        AlertDialog alertDialog = new TrackDialog(this, DIALOG_CREATE_TERMINAL).setView(view).show();

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    InputUtil.hideAllInputMethod(this);
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
            } else {
                getP().mapSettings(getMap().getMap());
            }
        });
    }
}
