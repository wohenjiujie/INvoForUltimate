package com.graduationproject.invoforultimate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.util.ToastUtil;

import butterknife.ButterKnife;

/**
 * Created by INvo
 * on 2019-09-24.
 */
public abstract class BaseActivity extends Activity {
    private static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initView(savedInstanceState);
        initControls(savedInstanceState);
        initListener(savedInstanceState);
    }

    protected static Context getContext() {
        return context;
    }

    protected abstract int getContentViewId();

    protected void ToastText(String string) {
        ToastUtil.shortToast(context, string);
    }

    protected void ToastTextLong(String string) {
        ToastUtil.LongToast(context,string);
    }

    protected void ToastMsg(int msg) {
        ToastUtil.shortToast(context, String.valueOf(msg));
    }

    protected void ToastMsgLong(int msg) {
        ToastUtil.LongToast(context, String.valueOf(msg));
    }

    /**
     * 回调方法
     *
     * @param msg
     */
    protected abstract void OnProcessCallBack(int msg);

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
    protected abstract void initListener(@Nullable Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
