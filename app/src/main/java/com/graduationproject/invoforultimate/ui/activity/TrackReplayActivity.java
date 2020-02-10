package com.graduationproject.invoforultimate.ui.activity;

import android.os.Bundle;
import butterknife.BindView;
import androidx.annotation.CheckResult;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.graduationproject.invoforultimate.BaseActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.presenter.impl.ReplayBuilderImpl;
import com.graduationproject.invoforultimate.ui.view.ReplayViewCallback;


/**
 * 运动记录回放Activity
 */
public class TrackReplayActivity extends BaseActivity<ReplayViewCallback, ReplayBuilderImpl, TextureMapView> implements ReplayViewCallback {
//    private static final String TAG = TrackReplayConstants.TAG;
    @BindView(R.id.history_map)
    TextureMapView textureMapView;

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
    protected void initControls(Bundle savedInstanceState) {
        getP().trackReplay(this.getIntent().getExtras());
    }

    @Override
    public void onTrackReplayResult(String s) {
        ToastText(s);
    }

    @Override
    public void onTrackReplayCallback(MarkerOptions var1, MarkerOptions var2, PolylineOptions var3, LatLngBounds.Builder builder) {
        getMap().getMap().addMarker(var1);
        getMap().getMap().addMarker(var2);
        getMap().getMap().addPolyline(var3);
        getMap().getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 30));
    }
}
