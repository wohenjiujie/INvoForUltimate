package com.graduationproject.invoforultimate.utils;

import android.content.Context;
import android.content.SharedPreferences;


import static com.graduationproject.invoforultimate.app.TrackApplication.*;
import static com.graduationproject.invoforultimate.entity.constants.TerminalModuleConstants.*;

/**
 * Created by INvo
 * on 2020-02-08.
 */
public final class TerminalUtil {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public static void setTerminal(String id, String terminal) {
        sharedPreferences = getContext().getSharedPreferences(INITIALIZE_TERMINAL, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Long tid = Long.valueOf(id);
        editor.putString("terminalName", terminal);
        editor.putLong("tid", tid);
        editor.commit();
    }

    public static Long getTerminal() {
        sharedPreferences = getContext().getSharedPreferences(INITIALIZE_TERMINAL, Context.MODE_PRIVATE);
        Long tid = sharedPreferences.getLong("tid", 0);
        return tid;
    }

    public static String getTerminalName() {
        sharedPreferences = getContext().getSharedPreferences(INITIALIZE_TERMINAL, Context.MODE_PRIVATE);
        String tidName = sharedPreferences.getString("terminalName", "");
        return tidName;
    }
}
