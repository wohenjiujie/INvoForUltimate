package com.graduationproject.invoforultimate.service;


import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.entity.bean.TrackHistoryInfo;
import com.graduationproject.invoforultimate.entity.bean.TrackInfo;
import com.graduationproject.invoforultimate.listener.MyTrackThread;
import com.graduationproject.invoforultimate.listener.TrackReplayTem;
import com.graduationproject.invoforultimate.model.TrackReplay;
import com.graduationproject.invoforultimate.model.TrackTerminal;
import com.graduationproject.invoforultimate.model.TrackHistory;
import com.graduationproject.invoforultimate.model.TrackModel;
import com.graduationproject.invoforultimate.utils.TerminalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.graduationproject.invoforultimate.entity.constants.HttpUrlConstants.*;
import static com.graduationproject.invoforultimate.entity.constants.TerminalModuleConstants.*;
import static com.graduationproject.invoforultimate.entity.constants.TrackHistoryConstants.*;
import static com.graduationproject.invoforultimate.entity.constants.TrackReplayConstants.TRACK_LATLNG;
import static com.graduationproject.invoforultimate.entity.constants.TrackServiceConstants.*;

/**
 * Created by INvo
 * on 2019-10-10.
 */
public class TrackThread extends Thread implements MyTrackThread {


    private String content;
    private RequestBody requestBody;
    private MediaType mediaTypeCommon = MediaType.parse("application/x-www-form-urlencoded");
    private MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");

    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Response response;
    private TrackTerminal trackTerminal;
    private int threadType;
    private String post;
    private TrackInfo trackInfo;
    private TrackHistoryInfo trackHistoryInfo;
    private String content2;
    private Request request2;
    private Response response2;
    private TrackHistory trackHistory;
    private TrackReplay trackReplay;
    private int TrackID;

    public TrackThread(int threadType, String post, @Nullable TrackModel trackModel) {
        this.trackTerminal = (TrackTerminal) trackModel;
        this.threadType = threadType;
        this.post = post;
    }

    public TrackThread(int threadType, TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
        this.threadType = threadType;
    }

    /**
     * 本身就有Async的依赖，所以不需要执行run()方法
     * @param threadType
     * @param trackModel
     */
    public TrackThread(int threadType, TrackModel trackModel) {
        this.trackHistory = (TrackHistory)trackModel;
        if (GET_TRACK_HISTORY_INFO == threadType) {
            getTrackHistoryInfo();
        }
    }

    /**
     * 本身就有Async的依赖，所以不需要执行run()方法
     *
     * @param threadType
     * @param var
     * @param trackModel
     */
    public TrackThread(int threadType, @NonNull Object var, @NonNull TrackModel trackModel) {
        this.trackHistory = (TrackHistory) trackModel;
        if (GET_TRACK_INTENT_INFO == threadType) {
            getIntentInfo(var);
        }
        if (DELETE_TRACK_INFO == threadType) {
            deleteTrack(var);
        }
    }

    public TrackThread(int threadType, int TrackID, TrackModel trackModel) {
        this.threadType = threadType;
        this.trackReplay = (TrackReplay) trackModel;
        this.TrackID = TrackID;
    }

    private void deleteTrack(Object var) {
        content = DECREASE_TRACK_COUNT + TerminalUtil.getTerminal();
        request = new Request.Builder().url(content).get().build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("terminal", TerminalUtil.getTerminal());
            jsonObject.put("track", var);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = RequestBody.create(mediaTypeJson, jsonObject.toString());
        request2 = new Request.Builder().url(DELETE_TRACK_DATA).post(requestBody).build();
        try {
            response = okHttpClient.newCall(request).execute();
            response2 = okHttpClient.newCall(request2).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getIntentInfo(Object var) {
        content = SEARCH_TRACK_HISTORY + "&tid=" + TerminalUtil.getTerminal()
                + "&trid=" + var + "&pagesize=999";
        request = new Request.Builder().url(content).get().build();
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                trackHistory.onTrackHistoryIntentCallback(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getTrackHistoryInfo() {
        content = SEARCH_TRACK_COUNTS + TerminalUtil.getTerminal();
        content2 = GET_TERMINAL_INFO + TerminalUtil.getTerminal();
        request = new Request.Builder().url(content).get().build();
        request2 = new Request.Builder().url(content2).get().build();
        try {
            response = okHttpClient.newCall(request).execute();
            response2 = okHttpClient.newCall(request2).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                trackHistory.onTrackHistoryCallback(response.body().string(), response2.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTrackCount(String s) {
        String x = CREATE_TRACK_COUNT_URL + s;
        Request request = new Request.Builder().url(x).get().build();
        try {
            okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadTrackInfo() {
        content = ADD_TRACK_COUNT + trackInfo.getTerminalID();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("terminal", trackInfo.getTerminalID());
            jsonObject.put("track", trackInfo.getTrackID());
            jsonObject.put("date", trackInfo.getDate());
            jsonObject.put("time", trackInfo.getTime());
            jsonObject.put("description", trackInfo.getDesc());
            jsonObject.put("mileage", trackInfo.getDistance());
            jsonObject.put("bitmap", trackInfo.getBitmap());
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
        if (TRACK_LATLNG == threadType) {
            getTrackLatLngList();
        }
    }

    private void getTrackLatLngList() {
        content = SEARCH_TRACK_HISTORY + "&tid=" + TerminalUtil.getTerminal() + "&trid=" + TrackID + "&pagesize=999";
        request = new Request.Builder().url(content).get().build();
        try {
            response = okHttpClient.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                String result = response.body().string();
                trackReplay.onGetListTem(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
                    message.what = MSG_TERMINAL_INVALID_ERROR;
                }
            } else {
                message.what = MSG_TERMINAL_SUCCESS;
                message.obj = accept;
            }
            trackTerminal.createTrackCallback(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
