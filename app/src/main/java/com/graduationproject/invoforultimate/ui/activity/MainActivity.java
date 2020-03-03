package com.graduationproject.invoforultimate.ui.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.AmapRouteActivity;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graduationproject.invoforultimate.BaseActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.entity.constants.MainConstants;
import com.graduationproject.invoforultimate.listener.OnInfoWindowAdapterListenerImpl;
import com.graduationproject.invoforultimate.listener.OnInfoWindowClickListenerImpl;
import com.graduationproject.invoforultimate.listener.OnLocationServiceListener;
import com.graduationproject.invoforultimate.listener.OnLocationSourceListenerImpl;
import com.graduationproject.invoforultimate.listener.OnMarkerClickListenerImpl;
import com.graduationproject.invoforultimate.listener.OnPoiSearchListenerImpl;
import com.graduationproject.invoforultimate.ui.widget.MainMenuView;
import com.graduationproject.invoforultimate.utils.NavigationUtils;
import com.graduationproject.invoforultimate.utils.PoiOverlay;
import com.graduationproject.invoforultimate.utils.TrackDialog;
import com.graduationproject.invoforultimate.presenter.impl.MainBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.impl.MainViewCallback;
import com.graduationproject.invoforultimate.utils.InputUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.*;
import butterknife.internal.Utils;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.graduationproject.invoforultimate.R2.id.basic_map;
import static com.graduationproject.invoforultimate.R2.id.basic_top;
import static com.graduationproject.invoforultimate.R2.id.constraintLayout;
import static com.graduationproject.invoforultimate.R2.id.layout_menu;
import static com.graduationproject.invoforultimate.R2.id.main_bottom;
import static com.graduationproject.invoforultimate.R2.id.main_textView_1;
import static com.graduationproject.invoforultimate.R2.id.main_textView_2;
import static com.graduationproject.invoforultimate.R2.id.nav_view;
import static com.graduationproject.invoforultimate.R2.id.time_start;
import static com.graduationproject.invoforultimate.app.TrackApplication.getAddress;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLatLonPoint;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLatitude;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLocation;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLongitude;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.*;
import static com.graduationproject.invoforultimate.entity.constants.TerminalModuleConstants.*;
import static com.graduationproject.invoforultimate.utils.NavigationUtils.getFriendlyDistance;

public class MainActivity extends BaseActivity<MainViewCallback, MainBuilderImpl, TextureMapView> implements MainViewCallback {
    @BindView(basic_map)
    TextureMapView textureMapView;
    @BindView(nav_view)
    BottomNavigationView bottomNavigationView;
    @BindView(main_textView_1)
    TextView trackWeatherInfo;
    @BindView(main_textView_2)
    TextView trackWeatherRefresh;
    @BindView(main_bottom)
    ConstraintLayout trackBottom;
    @BindView(layout_menu)
    LinearLayout linearLayout;
    @BindView(basic_top)
    ConstraintLayout constraintLayout;

    private static final String TAG = MainConstants.TAG;
    private static boolean isLocate = false;
    private MainMenuView mainMenuView;
    private PoiOverlay poiOverlay;


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
        mainMenuView = new MainMenuView(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.tools1:
                    ActivityCompat.startActivity(this, new Intent(getContext(), RouteSearchActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
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
        mainMenuView.setOnDismissListener(() -> alphaAni(0.5f, 1.0f));
        CheckBox cameraCheckBox = mainMenuView.getContentView().findViewById(R.id.main_check_box);
        cameraCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonView.setText(isChecked ? CHECK_BOX_CAMERA : CHECK_BOX_MARKER);
            getP().setCamera(isChecked);
        });
        CheckBox lifeCheckBox = mainMenuView.getContentView().findViewById(R.id.lifestyle_check_box);
        CheckBox sportCheckBox = mainMenuView.getContentView().findViewById(R.id.sport_check_box);
        CheckBox entertainmentCheckBox = mainMenuView.getContentView().findViewById(R.id.entertainment_check_box);
        lifeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sportCheckBox.setChecked(false);
                entertainmentCheckBox.setChecked(false);
                getP().initPoiSearch(getLatitude(), getLongitude(), "生活服务");
            } else {
                poiOverlay.removeFromMap();
            }
        });
        sportCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lifeCheckBox.setChecked(false);
                entertainmentCheckBox.setChecked(false);
                getP().initPoiSearch(getLatitude(), getLongitude(), "体育休闲服务");
            } else {
                poiOverlay.removeFromMap();
            }
        });
        entertainmentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sportCheckBox.setChecked(false);
                lifeCheckBox.setChecked(false);
                getP().initPoiSearch(getLatitude(), getLongitude(), "餐饮服务");

            } else {
                poiOverlay.removeFromMap();
            }
        });
    }

    @Override
    public void onPoiOverlayResult(PoiResult poiResult, int i) {
        if (i != AMapException.CODE_AMAP_SUCCESS || poiResult == null) {
            return;
        }
        poiOverlay = new PoiOverlay(getMap().getMap(), poiResult.getPois());
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();

        getMap().getMap().setInfoWindowAdapter(new OnInfoWindowAdapterListenerImpl() {
            @SuppressLint("SetTextI18n")
            @Override
            public View getInfoWindow(Marker marker) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.info_window, null);
                TextView title = view.findViewById(R.id.info_title);
                TextView snippet = view.findViewById(R.id.info_snippet);
                RelativeLayout infoWindow = view.findViewById(R.id.info_window);
                title.setText(marker.getTitle());
                Log.d(TAG, marker.getTitle());
                float distance = poiOverlay.getDistance(poiOverlay.getPoiIndex(marker));
                snippet.setText("距当前位置" + getFriendlyDistance((int) distance));
                marker.showInfoWindow();
                infoWindow.setOnClickListener(v -> {
                    Poi start = new Poi(getAddress(), new LatLng(getLatitude(), getLongitude()), "");
                    Poi end = new Poi("", new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), "");
                    AmapNaviPage.getInstance()
                            .showRouteActivity(getContext(), new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), null, AmapRouteActivity.class);
                });
                return view;
            }
        });
    }

    private void alphaAni(float v, float v1) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(v, v1);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(animation -> {
            Window window = MainActivity.this.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.alpha = (float) animation.getAnimatedValue();
            window.setAttributes(params);
        });
        valueAnimator.start();
    }

    @OnClick(basic_top)
    public void showRoute() {
        AmapNaviPage.getInstance().showRouteActivity(getContext(), new AmapNaviParams(null), null, AmapRouteActivity.class);
    }

    @OnClick(layout_menu)
    public void showMainMenu() {
        int[] ints = new int[]{0, 0};
        linearLayout.getLocationInWindow(ints);
        alphaAni(1.0f, 0.5f);
        mainMenuView.showAsDropDown(linearLayout);
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
}
