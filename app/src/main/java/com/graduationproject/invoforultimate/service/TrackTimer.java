package com.graduationproject.invoforultimate.service;

import android.util.Log;

import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.listener.MyTrackTimer;
import com.graduationproject.invoforultimate.listener.TrackTimerResult;
import com.graduationproject.invoforultimate.utils.TerminalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.graduationproject.invoforultimate.bean.constants.HttpUrlConstants.SEARCH_TRACK_HISTORY;
import static com.graduationproject.invoforultimate.bean.constants.TrackServiceConstants.*;

/**
 * Created by INvo
 * on 2020-02-08.
 */
public class TrackTimer extends TimerTask implements MyTrackTimer {
    private int timerType;
    private Request request;
    private Response response;
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Object object;
    private TrackTimerResult trackTimerResult;

    public TrackTimer(int timerType,@Nullable Object data,@Nullable TrackTimerResult trackTimerResult ) {
        this.timerType = timerType;
        this.object = data;
        this.trackTimerResult = trackTimerResult;
    }

    @Override
    public void run() {
        if (TIMER_TYPE_UPDATE_DATA == timerType) {
            updateTrack();
        }
    }

    @Override
    public void updateTrack() {
        String content = SEARCH_TRACK_HISTORY +"&tid=" + TerminalUtil.getTerminal() + "&trid=" + object + "&pagesize=999";
        request = new Request.Builder().url(content).get().build();
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                String result = response.body().string();
                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                JSONArray jsonArray = jsonObject.getJSONArray("tracks");

                int counts = Integer.parseInt(jsonArray.getJSONObject(0).getString("counts"));
                Log.d(TAG, "counts:" + counts);
                String object1 = jsonArray.getJSONObject(0)
                        .getJSONArray("points")
                        .getJSONObject(counts - 1)
                        .getString("speed");
                String object2 = jsonArray.getJSONObject(0)
                        .getString("distance");
                Log.d(TAG, object1+"\n"+object2);
                trackTimerResult.onTimerCallback(object1,object2);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
