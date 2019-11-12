package com.graduationproject.invoforultimate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

    }
    protected static  Context getContext(){
        return context;
    }
    protected abstract int getContentViewId();

    protected void ToastText(String text) {

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void ToastMsg(int msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    protected abstract void OnProcessCallBack(int msg);


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
