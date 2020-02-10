package com.graduationproject.invoforultimate.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graduationproject.invoforultimate.BaseActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.bean.constants.MainConstants;
import com.graduationproject.invoforultimate.connector.TrackDialog;
import com.graduationproject.invoforultimate.presenter.impl.MainBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.MainViewCallback;
import com.graduationproject.invoforultimate.util.InputUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import butterknife.OnLongClick;

import static com.graduationproject.invoforultimate.bean.constants.MainConstants.ALTER_DIALOG_POSITIVE;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CAMERA_FOLLOW_INIT;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CAMERA_FOLLOW_START;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CAMERA_FOLLOW_STOP;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CHECK_BOX_CAMERA;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.CHECK_BOX_MARKER;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.DIALOG_CREATE_TERMINAL;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.DIALOG_EXIT_APP;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.DIALOG_NEGATIVE_CHOICE;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.DIALOG_POSITIVE_CHOICE;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.DIALOG_START_TRACK;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.DIALOG_STOP_TRACK;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.DIALOG_TRACK_NOT_UPLOAD;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.INPUT_EMPTY;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.TRACK_START;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.TRACK_STOP;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.TRACK_UPLOAD;
import static com.graduationproject.invoforultimate.bean.constants.TerminalModuleConstants.RESULT_TERMINAL_MSG_EXISTING_ELEMENT;
import static com.graduationproject.invoforultimate.bean.constants.TerminalModuleConstants.RESULT_TERMINAL_MSG_INVALID_PARAMS;
import static com.graduationproject.invoforultimate.bean.constants.TerminalModuleConstants.RESULT_TERMINAL_MSG_SUCCESS;
import static com.graduationproject.invoforultimate.bean.constants.TrackServiceConstants.TRACK_RESULT_FAILURE;
import static com.graduationproject.invoforultimate.bean.constants.TrackServiceConstants.TRACK_RESULT_START;
import static com.graduationproject.invoforultimate.bean.constants.TrackServiceConstants.TRACK_RESULT_STOP;


public class MainActivity extends BaseActivity<MainViewCallback, MainBuilderImpl, TextureMapView> implements MainViewCallback {
    @BindView(R.id.basic_map)
    TextureMapView textureMapView;
    @BindView(R.id.nav_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.track_signal)
    ImageView trackSignal;
    @BindView(R.id.track_distance)
    TextView trackDistance;
    @BindView(R.id.time_task)
    Chronometer chronometer;
    @BindView(R.id.track_controller)
    TextView trackController;
    @BindView(R.id.track_speed)
    TextView trackSpeed;
    @BindView(R.id.track_camera)
    CheckBox trackCamera;
//    private static final String TAG = MainConstants.TAG;
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
                        .setPositiveButton(DIALOG_POSITIVE_CHOICE, (dialog, which) -> {
                            getP().stopTrack();
                        }).setNegativeButton(DIALOG_NEGATIVE_CHOICE, (dialog, which) -> dialog.dismiss()).show();
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

    @Override
    protected void initControls(Bundle savedInstanceState) {
        getP().mapSettings(getMap().getMap());
        getP().checkTerminal();
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                /*
                 * 暂定
                 */
                case R.id.tools1:

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

    @OnCheckedChanged(R.id.track_camera)
    public void setCameraModel(boolean isChecked) {
        trackCamera.setText(isChecked ? CHECK_BOX_CAMERA : CHECK_BOX_MARKER);
        getP().setCamera(isChecked);
    }

    @OnClick(R.id.track_controller)
    @NonNull
    public void trackStart() {
        if (!isStart) {
            isLocate = false;
            getP().startTrack(chronometer);
        }
    }

    @MainThread
    @OnLongClick(R.id.track_controller)
    @NonNull
    public void trackStop() {
        if (isStart) {
            isLocate = false;
            getP().stopTrack();
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
                    addAll(coordinate).width(10).color(Color.argb(255, 1, 1, 1)));
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
    public void onInitLocation(LatLng latLng, @NonNull Integer type) {
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
            }
        });
    }
}
