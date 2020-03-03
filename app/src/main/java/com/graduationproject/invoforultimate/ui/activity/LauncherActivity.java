package com.graduationproject.invoforultimate.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.ui.fragment.TrackRecordFragment;


public class LauncherActivity extends AppCompatActivity {
    private TrackRecordFragment trackRecordFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (savedInstanceState == null) {
            trackRecordFragment = TrackRecordFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.track_record, trackRecordFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        trackRecordFragment.onKeyDownChild(keyCode,event);
        return super.onKeyDown(keyCode, event);
    }
}
