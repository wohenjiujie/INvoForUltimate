package com.graduationproject.invoforultimate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.TextureMapView;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;
import com.graduationproject.invoforultimate.util.ToastUtil;

import butterknife.ButterKnife;

/**
 * Created by INvo
 * on 2019-09-24.
 */
public abstract class BaseActivity<V extends ViewCallback, P extends Presenter<V>, M extends TextureMapView> extends Activity {
    private static Context context;
    private P p;
    private V v;
    private M m;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        if (null == this.m) {
            this.m = loadM(savedInstanceState);
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
        initView(savedInstanceState);
        initControls(savedInstanceState);
        initListener(savedInstanceState);
    }

    protected static Context getContext() {
        return context;
    }

    protected abstract M loadM(Bundle savedInstanceState);

    protected abstract P loadP();

    protected abstract V loadV();

    protected P getP() {
        return this.p;
    }

    protected M getM() {
        return this.m;
    }

    protected abstract int getContentViewId();

    protected void ToastText(String string) {
        ToastUtil.shortToast(context, string);
    }

    protected void ToastTextLong(String string) {
        ToastUtil.LongToast(context, string);
    }

    protected void ToastMsg(int msg) {
        ToastUtil.shortToast(context, String.valueOf(msg));
    }

    protected void ToastMsgLong(int msg) {
        ToastUtil.LongToast(context, String.valueOf(msg));
    }


    /**
     * 初始View
     *
     * @param savedInstanceState
     */
    protected abstract void initView(@NonNull Bundle savedInstanceState);

    /**
     * Controls settings
     *
     * @param savedInstanceState
     */
    protected abstract void initControls(@Nullable Bundle savedInstanceState);

    /**
     * init Listener
     *
     * @param savedInstanceState
     */
    protected void initListener(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != this.m) {
            m.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != this.m) {
            m.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != this.m) {
            m.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != this.p && null != this.v) {
            this.p.detachV();
        }
        if (null != this.m) {
            m.onDestroy();
        }
    }
}
