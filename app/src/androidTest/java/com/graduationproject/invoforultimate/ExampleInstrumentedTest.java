package com.graduationproject.invoforultimate;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
    }
@Test
    public void TestDataBase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        DatabaseUtil databaseUtil = new DatabaseUtil(context);
        Log.d("ExampleInstrumentedTest", "context:" + context);
        System.out.println("ahhahhahha");
        Log.d("ExampleInstrumentedTest", "databaseUtil.isRegistration():" + databaseUtil.isRegistration());
    }
}
