package com.graduationproject.invoforultimate;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnit4;

import com.graduationproject.invoforultimate.util.DatabaseUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.graduationproject.invoforultimate", appContext.getPackageName());
//        TestDataBase();
        testTimeTask(appContext);
    }

    @Test
    public void TestDataBase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        DatabaseUtil databaseUtil = new DatabaseUtil(context);
        Log.d("ExampleInstrumentedTest", "context:" + context);
        System.out.println("ahhahhahha");
        Log.d("ExampleInstrumentedTest", "databaseUtil.isRegistration():" + databaseUtil.isRegistration());
    }

    public void testTimeTask(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_task, null);
        Chronometer chronometer = view.findViewById(R.id.time_task);
        Button start = view.findViewById(R.id.time_start);
        Button pause = view.findViewById(R.id.time_pause);
        Button restart = view.findViewById(R.id.time_restart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置开始计时时间
                chronometer.setBase(SystemClock.elapsedRealtime());
                //启动计时器
                chronometer.start();
                pause.setEnabled(true);
                restart.setEnabled(false);
                start.setEnabled(false);
            }
        });
    }
}
