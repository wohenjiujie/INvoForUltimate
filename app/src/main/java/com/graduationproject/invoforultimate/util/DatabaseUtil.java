package com.graduationproject.invoforultimate.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by INvo
 * on 2019-09-25.
 */
public class DatabaseUtil {
   private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static String tid;

    public DatabaseUtil(Context context) {
        this.context = context;
    }


    public boolean isRegistration() {
    sharedPreferences=context.getSharedPreferences("Terminal", MODE_PRIVATE);
        tid = sharedPreferences.getString("Tid", "");
        if (tid == "") {
            return false;
        } else {
            return true;
        }
    }

    public String getTerminalID() {
        return sharedPreferences.getString("tid", "");
    }
}
