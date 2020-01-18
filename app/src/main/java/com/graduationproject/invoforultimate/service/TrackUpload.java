package com.graduationproject.invoforultimate.service;

import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.constant.TrackInfo;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by INvo
 * on 2019-11-03.
 */
public class TrackUpload<T> {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Response response;
    private RequestBody requestBody = null;
    private MediaType mediaType;
    private Request request;
    private TrackInfo trackInfo;
    private String content;
    private boolean check;
    public TrackUpload(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
        check = (int)trackInfo.getTimeConsuming() >= 60 ? true : false;
    }



    public void upload(){
        Thread thread = new Thread(() ->{
//            content = "http://xiaomu1079.club/increaseCounts/"+trackInfo.getTerminalID();
            mediaType = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("terminal", trackInfo.getTerminalID());
                jsonObject.put("track", trackInfo.getTrackID());
                jsonObject.put("date", trackInfo.getDate());
                jsonObject.put("time", trackInfo.getTime());
                jsonObject.put("description", trackInfo.getDesc());
                jsonObject.put("mileage", trackInfo.getDistance());
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
            requestBody = RequestBody.create(mediaType, jsonObject.toString());
            request = new Request.Builder().url(Constants.AddTrackInfo).post(requestBody).build();
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void addTrackCounts(){
        Thread thread = new Thread(() ->{
            content = "http://xiaomu1079.club/increaseCounts/"+trackInfo.getTerminalID();
            request = new Request.Builder().url(content).get().build();
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
    public boolean isCheck() {
        return check;
    }
}
