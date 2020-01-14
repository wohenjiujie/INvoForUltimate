package com.graduationproject.invoforultimate.constant;

import android.os.Bundle;

import org.json.JSONException;

/**
 * Created by INvo
 * on 2019-10-11.
 */
public interface OnTrackCountsPostListener {
    void onGetCounts(String result);

    void onGetInfo(String result) ;

    void onGetID(String result);

    void onDeleteInfo();
}


