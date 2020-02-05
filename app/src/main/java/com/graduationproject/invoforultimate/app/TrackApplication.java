package com.graduationproject.invoforultimate.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by INvo
 * on 2019-11-05.
 */
public class TrackApplication extends Application {
    private static Context context;
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
