package com.graduationproject.invoforultimate.model.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.entity.bean.TrackHistoryInfo;
import com.graduationproject.invoforultimate.entity.bean.TrackIntentParcelable;
import com.graduationproject.invoforultimate.model.TrackHistory;
import com.graduationproject.invoforultimate.presenter.HistoryBuilderPresenter;
import com.graduationproject.invoforultimate.presenter.TrackPresenter;
import com.graduationproject.invoforultimate.service.TrackThread;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.graduationproject.invoforultimate.entity.constants.TrackHistoryConstants.*;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public class TrackHistoryImpl extends AsyncTask<String, Integer, String> implements TrackHistory {
    private int type;
    private static TrackHistoryInfo trackHistoryInfo = new TrackHistoryInfo();
    private HistoryBuilderPresenter historyBuilderPresenter;
    private ArrayList<String> desc = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> distance = new ArrayList<>();
    private ArrayList<String> TrackID = new ArrayList<>();
    private ArrayList<Bitmap> bitmap = new ArrayList<>();
    private Object var;

    public TrackHistoryImpl(int type, @Nullable Object var, TrackPresenter trackPresenter) {
        super();
        this.type = type;
        this.historyBuilderPresenter = (HistoryBuilderPresenter) trackPresenter;
        this.var = var;
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
        trackHistoryInfo.setBitmap(bitmap);
        try {
            JSONArray jsonArray = new JSONArray(var2);
            for (int i = 0; i < Integer.parseInt(var1); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                trackHistoryInfo.getDesc().add(jsonObject.getString("description"));
                trackHistoryInfo.getDate().add(jsonObject.getString("date"));
                trackHistoryInfo.getTime().add(jsonObject.getString("time"));
                trackHistoryInfo.getDistance().add(jsonObject.getString("mileage"));
                trackHistoryInfo.getTrackID().add(jsonObject.getString("track"));
                byte[] bytes = Base64.decodeBase64(jsonObject.getString("bitmap"));
                trackHistoryInfo.getBitmap().add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
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
