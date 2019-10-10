package com.graduationproject.invoforultimate.initialize;

import android.content.Context;
import android.content.SharedPreferences;

import com.graduationproject.invoforultimate.constant.Constants;

/**
 * Created by INvo
 * on 2019-10-10.
 */
public class InitializeTerminal {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public boolean checkTerminal(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.initializeTerminal, Context.MODE_PRIVATE);
        String string = sharedPreferences.getString("tid", "");
        if (string == "") {
            return false;
        } else {
            return true;
        }
    }

    public boolean setTerminal(Context context) {

        return false;
    }

    public String getTerminal() {
        return null;
    }
}
