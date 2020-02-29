package com.graduationproject.invoforultimate.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.graduationproject.invoforultimate.BaseFragment;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.listener.TrackScreenShotImpl;
import com.graduationproject.invoforultimate.presenter.impl.RecordBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.impl.RecordViewCallback;
import com.graduationproject.invoforultimate.utils.TrackDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.graduationproject.invoforultimate.R2.id.record_controller;
import static com.graduationproject.invoforultimate.R2.id.record_time;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.ALTER_DIALOG_POSITIVE;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.DIALOG_NEGATIVE_CHOICE;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.DIALOG_POSITIVE_CHOICE;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.DIALOG_STOP_TRACK;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.DIALOG_TRACK_NOT_UPLOAD;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.TRACK_START;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.TRACK_STOP;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.TRACK_UPLOAD;
import static com.graduationproject.invoforultimate.entity.constants.TrackServiceConstants.TRACK_RESULT_FAILURE;
import static com.graduationproject.invoforultimate.entity.constants.TrackServiceConstants.TRACK_RESULT_START;
import static com.graduationproject.invoforultimate.entity.constants.TrackServiceConstants.TRACK_RESULT_STOP;

public class TrackRecordFragment extends BaseFragment<RecordViewCallback, RecordBuilderImpl, TextureMapView> implements RecordViewCallback {
    private static boolean isStart = false;
    private static List<LatLng> coordinate = new ArrayList<>();
    private static Polyline polyline;
    private static List<Polyline> polyLines = new LinkedList<>();

    @BindView(R.id.record_map)
    TextureMapView textureMapView;
    @BindView(R.id.record_controller)
    TextView recordController;
    @BindView(record_time)
    Chronometer chronometer;
    @BindView(R.id.record_signal)
    TextView recordSignal;
    @BindView(R.id.record_elevation)
    TextView recordElevation;
    @BindView(R.id.record_distance)
    TextView recordDistance;
    @BindView(R.id.record_speed)
    TextView recordSpeed;

    @BindView(R.id.record_tips)
    TextView recordTips;

    public static TrackRecordFragment newInstance(@Nullable Bundle args) {
        TrackRecordFragment fragment = new TrackRecordFragment();
        if (null != args) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    protected void initControls(@Nullable Bundle savedInstanceState) {
        getP().mapSettings(getMap().getMap());
    }


    @Override
    public void onKeyDownChild(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && isStart) {
            new TrackDialog(asContext(), DIALOG_STOP_TRACK)
                    .setPositiveButton(DIALOG_POSITIVE_CHOICE, (dialog, which) -> getMap().getMap().getMapScreenShot(new TrackScreenShotImpl() {
                        @Override
                        public void onMapScreenShot(Bitmap bitmap) {
                                getP().stopTrack(bitmap);
                        }
                    })).setNegativeButton(DIALOG_NEGATIVE_CHOICE, (dialog, which) -> dialog.dismiss()).show();
        }
    }

    @OnClick(record_controller)
    @NonNull
    public void recordStart() {
        if (!isStart) {
            getP().startTrack(chronometer);
        }
    }
    @MainThread
    @OnLongClick(record_controller)
    @NonNull
    public void trackStop() {
        // TODO: 2020-02-29 chronometer not clear

        if (isStart) {
            getMap().getMap().getMapScreenShot(new TrackScreenShotImpl() {
                @Override
                public void onMapScreenShot(Bitmap bitmap) {
                    getP().stopTrack(bitmap);
                }
            });
        }
    }

    @Override
    protected TextureMapView loadMap(Bundle savedInstanceState) {
        textureMapView.onCreate(savedInstanceState);
        return textureMapView;
    }

    @Override
    protected RecordBuilderImpl loadP() {
        return new RecordBuilderImpl();
    }

    @Override
    protected RecordViewCallback loadV() {
        return this;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_track_record, container, false);
    }

    @Override
    public void onTrackResult(int callback, String s) {
        getActivity().runOnUiThread(() -> {
            if (TRACK_RESULT_START == callback) {
                ToastText(s);
                isStart = true;
                updateBtnStatus();
                getMap().getMap().moveCamera(CameraUpdateFactory.zoomTo(19));
            }
            if (TRACK_RESULT_STOP == callback) {
                isStart = false;
                updateBtnStatus();
                recordSpeed.setText("0.0 km/h");
                recordDistance.setText("0.0 m");
                recordElevation.setText("0.0 m");
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
    public void onTrackChangedResult(String var1,String var2,String var3) {
        getActivity().runOnUiThread(() -> {
            if (null == var1) {
                recordSpeed.setText(0.0 + " km/h");
            } else {
                recordSpeed.setText(var1 + " km/h");
            }
            if (null == var2) {
                recordDistance.setText("0.0 m");
            } else {
                recordDistance.setText(var2 + " m");
            }
            if (null == var3||"0.0".equals(var3)) {
                recordElevation.setText("未知");
            } else {
                recordElevation.setText(var3 + " m");
            }
        });
    }

    @Override
    public void onTrackLocationResult(double longitude, double latitude, int rank) {
        getActivity().runOnUiThread(() -> {
            switch (rank) {
                case 1:
                    Drawable drawable1 = getResources().getDrawable((R.drawable.gps_signal_good),null);
                    drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                    recordSignal.setCompoundDrawables(null, null, drawable1, null);
                    recordTips.setText("强");
                    break;
                case 0:
                    Drawable drawable2 = getResources().getDrawable((R.drawable.gps_signal_bad),null);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                    recordSignal.setCompoundDrawables(null, null, drawable2, null);
                    recordTips.setText("中");
                    break;
                case -1:
                    Drawable drawable3 = getResources().getDrawable((R.drawable.gps_signal_unknown),null);
                    drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                    recordSignal.setCompoundDrawables(null, null, drawable3, null);
                    recordTips.setText("弱");
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
        getActivity().runOnUiThread(() -> {
            if (x) {
                ToastText(TRACK_UPLOAD);
            } else {
                new TrackDialog(asContext(), DIALOG_TRACK_NOT_UPLOAD)
                        .setPositiveButton(ALTER_DIALOG_POSITIVE, (dialog, which) -> dialog.dismiss()).show();
            }
        });
    }
    private void updateBtnStatus() {
        recordController.setText(isStart ? TRACK_STOP : TRACK_START);
    }
}
