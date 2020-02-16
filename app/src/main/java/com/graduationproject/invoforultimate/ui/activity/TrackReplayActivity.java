package com.graduationproject.invoforultimate.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.CheckResult;
import androidx.annotation.MainThread;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.graduationproject.invoforultimate.BaseActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.presenter.impl.ReplayBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.impl.ReplayViewCallback;
import butterknife.BindView;
import butterknife.OnClick;

import static com.graduationproject.invoforultimate.R2.id.history_map;
import static com.graduationproject.invoforultimate.R2.id.trace_replay;

public class TrackReplayActivity extends BaseActivity<ReplayViewCallback, ReplayBuilderImpl, TextureMapView> implements ReplayViewCallback {
//    private static final String TAG = TrackReplayConstants.TAG;
    @BindView(history_map)
    TextureMapView textureMapView;
    @BindView(trace_replay)
    Button traceReplay;
    private Marker maker;

    @Override
    protected void initControls(Bundle savedInstanceState) {
        getP().trackReplay(this.getIntent().getExtras());
    }

    @MainThread
    @OnClick(trace_replay)
    public void onReplay() {
        getP().MarkerReplay();
    }

    @CheckResult
    @Override
    protected TextureMapView loadMap(Bundle savedInstanceState) {
        textureMapView.onCreate(savedInstanceState);
        return textureMapView;
    }

    @Override
    protected ReplayBuilderImpl loadP() {
        return new ReplayBuilderImpl();
    }

    @Override
    protected ReplayViewCallback loadV() {
        return this;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_track_replay;
    }

    @Override
    public void onTrackReplayResult(String s) {
        runOnUiThread(() -> ToastText(s));
    }

    @Override
    public void onTrackReplayResult(MarkerOptions var1, MarkerOptions var2, PolylineOptions var3, LatLngBounds.Builder builder, MarkerOptions var4) {
        runOnUiThread(() -> {
            getMap().getMap().addMarker(var1);
            getMap().getMap().addMarker(var2);
            maker = getMap().getMap().addMarker(var4);
            getMap().getMap().addPolyline(var3);
            getMap().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 30));
        });
    }

    @MainThread
    @Override
    public void onMarkerPositionResult(LatLng latLng) {
        runOnUiThread(() -> maker.setPosition(latLng));
    }
}
