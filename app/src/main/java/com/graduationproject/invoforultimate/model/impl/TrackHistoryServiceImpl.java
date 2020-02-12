package com.graduationproject.invoforultimate.model.impl;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.bean.TrackHistoryInfo;
import com.graduationproject.invoforultimate.bean.TrackIntentParcelable;
import com.graduationproject.invoforultimate.model.TrackHistoryModel;
import com.graduationproject.invoforultimate.presenter.HistoryBuilderPresenter;
import com.graduationproject.invoforultimate.utils.TrackThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.graduationproject.invoforultimate.bean.constants.TrackHistoryConstants.*;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public class TrackHistoryServiceImpl extends AsyncTask<String, Integer, String> implements TrackHistoryModel {
    private int type;
    private TrackHistoryInfo trackHistoryInfo = new TrackHistoryInfo();
    private HistoryBuilderPresenter historyBuilderPresenter;
    private ArrayList<String> desc = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> distance = new ArrayList<>();
    private ArrayList<String> TrackID = new ArrayList<>();
    private Object var;

    public TrackHistoryServiceImpl(int type, @Nullable Object var, HistoryBuilderPresenter historyBuilderPresenter) {
        super();
        this.type = type;
        this.historyBuilderPresenter = historyBuilderPresenter;
        this.var=var;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (GET_TRACK_HISTORY_INFO == type) {
            new TrackThread(GET_TRACK_HISTORY_INFO, this);
        }
        if (GET_TRACK_INTENT_INFO == type) {
            new TrackThread(GET_TRACK_INTENT_INFO, var, this);
        }
        if (DELETE_TRACK_INFO == type) {
            new TrackThread(DELETE_TRACK_INFO, var, this);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (DELETE_TRACK_INFO == type) {
            historyBuilderPresenter.onDeleteCallback();
        }
    }

    @Override
    public void onTrackHistoryCallback(@Nullable String var1, @Nullable String var2) {
        trackHistoryInfo.setCounts(var1);
        trackHistoryInfo.setDesc(desc);
        trackHistoryInfo.setDate(date);
        trackHistoryInfo.setTime(time);
        trackHistoryInfo.setDistance(distance);
        trackHistoryInfo.setTrackID(TrackID);
        try {
            JSONArray jsonArray = new JSONArray(var2);
            for (int i = 0; i <= Integer.parseInt(var1); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                trackHistoryInfo.getDesc().add(jsonObject.getString("description"));
                trackHistoryInfo.getDate().add(jsonObject.getString("date"));
                trackHistoryInfo.getTime().add(jsonObject.getString("time"));
                trackHistoryInfo.getDistance().add(jsonObject.getString("mileage"));
                trackHistoryInfo.getTrackID().add(jsonObject.getString("track"));
            }
            historyBuilderPresenter.onGetTrackHistoryCallback(trackHistoryInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTrackHistoryIntentCallback(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            JSONArray jsonArray = jsonObject.getJSONArray("tracks");
            String startUnix = jsonArray.getJSONObject(0).getJSONObject("startPoint").getString("locatetime");
            String endUnix = jsonArray.getJSONObject(0).getJSONObject("endPoint").getString("locatetime");
            TrackIntentParcelable trackIntentParcelable = new TrackIntentParcelable(Integer.parseInt((String) var), Long.valueOf(startUnix), Long.valueOf(endUnix));
            historyBuilderPresenter.onGetIntentCallback(trackIntentParcelable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
