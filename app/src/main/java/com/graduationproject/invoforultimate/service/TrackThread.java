package com.graduationproject.invoforultimate.service;


import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.bean.TrackInfo;
import com.graduationproject.invoforultimate.connector.MyTrackThread;
import com.graduationproject.invoforultimate.connector.TrackResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.graduationproject.invoforultimate.bean.constants.HttpUrlConstants.*;
import static com.graduationproject.invoforultimate.bean.constants.TerminalModuleConstants.*;
import static com.graduationproject.invoforultimate.bean.constants.TrackServiceConstants.*;

/**
 * Created by INvo
 * on 2019-10-10.
 */
public class TrackThread extends Thread implements MyTrackThread {


    private String content;
    private RequestBody requestBody;
    private MediaType mediaTypeCommon=MediaType.parse("application/x-www-form-urlencoded");
    private  MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Response response;
    private TrackResult trackResult;
    private int threadType;
    private String post;
    private TrackInfo trackInfo;
    public TrackThread(int threadType, String post, @Nullable TrackResult trackResult) {
        super();
        this.trackResult = trackResult;
        this.threadType = threadType;
        this.post = post;
    }

    public TrackThread(int threadType,TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
        this.threadType = threadType;
    }
    public TrackThread() {
        super();
    }

    @Override
    public void createTrackCount(String s) {
        String x = CREATE_TRACK_COUNT_URL + s;
        Request request = new Request.Builder().url(x).get().build();
        try {
            okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadTrackInfo() {
        content = ADD_TRACK_COUNT+trackInfo.getTerminalID();
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
        requestBody = RequestBody.create(mediaTypeJson, jsonObject.toString());
        request = new Request.Builder().url(ADD_TRACK_INFO).post(requestBody).build();

        try {
            response = okHttpClient.newCall(request).execute();
            request = new Request.Builder().url(content).get().build();
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        if (CREATE_TERMINAL == threadType) {
            createTerminal(post);
        }
        if (CREATE_TRACK_COUNT == threadType) {
            createTrackCount(post);
        }
        if (UPLOAD_TRACK_INFO == threadType) {
            uploadTrackInfo();
        }
    }

    @Override
    public void createTerminal(String s) {
        content = TERMINAL_PREFIX + "&&name=" + s;
        requestBody = RequestBody.create(mediaTypeCommon, content);
        request = new Request.Builder().url(CREATE_TERMINAL_URL).post(requestBody).build();
        try {
            response = okHttpClient.newCall(request).execute();
            String accept = response.body().string();
            Log.d("myaccept", accept);
            JSONObject jsonObject = new JSONObject(accept);
            String errMsg = jsonObject.getString(CREATE_TERMINAL_MSG);
            Message message = Message.obtain();
            if (!errMsg.equals(CREATE_TERMINAL_MSG_OK)) {
                if (errMsg.equals(CREATE_TERMINAL_MSG_EXISTING_ELEMENT)) {
                    message.what = MSG_TERMINAL_EXIST_ERROR;
                } else if (errMsg.equals(CREATE_TERMINAL_MSG_INVALID_PARAMS)) {
                    message.what =MSG_TERMINAL_INVALID_ERROR;
                }
            } else {
                message.what = MSG_TERMINAL_SUCCESS;
                message.obj = accept;
            }


            trackResult.trackResult(message);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
