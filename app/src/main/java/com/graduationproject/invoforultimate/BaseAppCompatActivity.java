package com.graduationproject.invoforultimate;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.TextureMapView;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.service.TrackBroadcast;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;
import com.graduationproject.invoforultimate.utils.ToastUtil;

import butterknife.ButterKnife;

/**
 * Created by INvo
 * on 2020-03-03.
 */
public abstract class BaseAppCompatActivity<V extends ViewCallback, P extends Presenter<V>, Map extends TextureMapView>  extends AppCompatActivity {
    private static Context context;
    private P p;
    private V v;
    private Map map;
    private boolean isRegistered = false;
    private TrackBroadcast trackBroadcast;
    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        if (null == this.map) {
            this.map = loadMap(savedInstanceState);
        }
        if (null == this.p) {
            this.p = loadP();
        }
        if (null == this.v) {
            this.v = loadV();
        }
        if (null != this.p && null != this.v) {
            this.p.attachV(this.v);
        }
        trackBroadcast = new TrackBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(trackBroadcast, filter);
        isRegistered = true;
        initControls(savedInstanceState);
    }

    protected static Context getContext() {
        return context;
    }

    protected abstract Map loadMap(Bundle savedInstanceState);

    protected abstract P loadP();

    protected abstract V loadV();

    protected P getP() {
        return this.p;
    }

    protected Map getMap() {
        return this.map;
    }

    protected abstract int getContentViewId();

    protected void ToastText(String string) {
        ToastUtil.shortToast(context, string);
    }

    protected void ToastTextLong(String string) {
        ToastUtil.LongToast(context, string);
    }

    protected abstract void initControls(@Nullable Bundle savedInstanceState);



    @Override
    protected void onResume() {
        super.onResume();
        if (null != this.map) {
            map.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != this.map) {
            map.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != this.map) {
            map.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != this.p && null != this.v) {
            this.p.detachV();
        }
        if (null != this.map) {
            map.onDestroy();
        }
        if (isRegistered) {
            unregisterReceiver(trackBroadcast);
            trackBroadcast = null;
        }
    }

}
