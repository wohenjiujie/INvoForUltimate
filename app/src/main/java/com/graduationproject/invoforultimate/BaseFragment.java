package com.graduationproject.invoforultimate;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.TextureMapView;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.service.TrackBroadcast;
import com.graduationproject.invoforultimate.ui.fragment.TrackRecordFragment;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;
import com.graduationproject.invoforultimate.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by INvo
 * on 2020-02-29.
 */
public abstract class BaseFragment<V extends ViewCallback,P extends Presenter<V>,Map extends TextureMapView> extends Fragment {
    private P p;
    private V v;
    private Map map;
    private Unbinder unbinder;
    private boolean isRegistered = false;
    private TrackBroadcast trackBroadcast;
    private static Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getContentView(inflater, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        context.registerReceiver(trackBroadcast, filter);
        isRegistered = true;
        initControls(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    protected abstract void initControls(@Nullable Bundle savedInstanceState);

    protected abstract Map loadMap(Bundle savedInstanceState);

    protected abstract P loadP();

    protected abstract V loadV();

    protected P getP() {
        return this.p;
    }

    protected Map getMap() {
        return this.map;
    }

    protected static Context asContext() {
        return context;
    }

    protected void ToastText(String string) {
        ToastUtil.shortToast(context, string);
    }

    protected void ToastTextLong(String string) {
        ToastUtil.LongToast(context, string);
    }

    public abstract void onKeyDownChild(int keyCode, KeyEvent event);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != this.map) {
            map.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != this.map) {
            map.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != this.map) {
            map.onSaveInstanceState(outState);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != this.p && null != this.v) {
            this.p.detachV();
        }
        if (null != this.map) {
            map.onDestroy();
        }
        if (isRegistered) {
            context.unregisterReceiver(trackBroadcast);
            trackBroadcast = null;
        }
    }
}
