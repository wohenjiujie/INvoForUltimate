package com.graduationproject.invoforultimate.ui.activity;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graduationproject.invoforultimate.BaseActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.entity.constants.MainConstants;
import com.graduationproject.invoforultimate.listener.OnLocationSourceListenerImpl;
import com.graduationproject.invoforultimate.utils.TrackDialog;
import com.graduationproject.invoforultimate.presenter.impl.MainBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.impl.MainViewCallback;
import com.graduationproject.invoforultimate.utils.InputUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.*;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.graduationproject.invoforultimate.R2.id.basic_map;
import static com.graduationproject.invoforultimate.R2.id.main_bottom;
import static com.graduationproject.invoforultimate.R2.id.main_textView_1;
import static com.graduationproject.invoforultimate.R2.id.main_textView_2;
import static com.graduationproject.invoforultimate.R2.id.nav_view;
import static com.graduationproject.invoforultimate.R2.id.track_camera;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.*;
import static com.graduationproject.invoforultimate.entity.constants.TerminalModuleConstants.*;

public class MainActivity extends BaseActivity<MainViewCallback, MainBuilderImpl, TextureMapView> implements MainViewCallback {
    @BindView(basic_map)
    TextureMapView textureMapView;
    @BindView(nav_view)
    BottomNavigationView bottomNavigationView;
    @BindView(track_camera)
    CheckBox trackCamera;
    @BindView(main_textView_1)
    TextView trackWeatherInfo;
    @BindView(main_textView_2)
    TextView trackWeatherRefresh;
    @BindView(main_bottom)
    ConstraintLayout trackBottom;

    private static final String TAG = MainConstants.TAG;
    private static boolean isLocate = false;

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
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            new TrackDialog(this, DIALOG_EXIT_APP).setPositiveButton(DIALOG_POSITIVE_CHOICE, (dialog, which) -> {
                finish();
                System.exit(0);
            }).setNegativeButton(DIALOG_NEGATIVE_CHOICE, (dialog, which) -> dialog.dismiss()).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initControls(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 1);
        }
        getP().checkTerminal();
        getP().getWeather();
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.tools1:
                    ToastText("No Function");
                    break;

                case R.id.tools2:
                    startActivity(new Intent(MainActivity.this, LauncherActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, bottomNavigationView, "intent").toBundle());
                    break;

                case R.id.tools3:
                    ActivityCompat.startActivity(this, new Intent(getContext(), TrackHistoryActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                    break;
            }
            return true;
        });


    }

    @OnCheckedChanged(track_camera)
    public void setCameraModel(boolean isChecked) {
        trackCamera.setText(isChecked ? CHECK_BOX_CAMERA : CHECK_BOX_MARKER);
        getP().setCamera(isChecked);
    }

    @OnClick(main_textView_1)
    public void getWeatherInfo() {

    }

    @OnClick(main_textView_2)
    public void refreshWeatherInfo() {
        getP().getWeather();
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
                getP().mapSettings(getMap().getMap());
                bottomNavigationView.setVisibility(View.VISIBLE);
                trackBottom.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onInitLocationResult(LatLng latLng) {
        if (!isLocate) {
            getMap().getMap().moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            isLocate = true;
        }
        getMap().getMap().setLocationSource(new OnLocationSourceListenerImpl() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                getMap().getMap().animateCamera(CameraUpdateFactory.zoomTo(18), 500, null);
            }
        });
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
                trackBottom.setVisibility(View.INVISIBLE);
                terminalCreateAlterDialog();
            } else {
                getP().mapSettings(getMap().getMap());
            }
        });
    }

    @Override
    public void onGetWeatherResult(@Nullable JSONObject object) {
        runOnUiThread(() -> {
            if (null != object) {
                try {
                    trackWeatherInfo.setText(object.getString("city") + " " + object.getString("weather") + " " + object.getString("temperature"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                trackWeatherInfo.setText("获取信息失败");
            }
        });
    }
}
