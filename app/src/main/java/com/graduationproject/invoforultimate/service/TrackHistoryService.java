package com.graduationproject.invoforultimate.service;

import com.graduationproject.invoforultimate.app.TrackApplication;
import com.graduationproject.invoforultimate.bean.TrackInfo;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by INvo
 * on 2019-11-05.
 */
 public  class TrackHistoryService {

    private OkHttpClient okHttpClient = new OkHttpClient();
    private Response response;
    private RequestBody requestBody = null;
    private MediaType mediaType;
    private Request request;
    private TrackInfo trackInfo;
    private String content;
    private InitializeTerminal initializeTerminal;
    private int counts;

    public int getTrackCounts(){

        return trackCounts();
//        return getCounts();
    }

    private int trackCounts(){
        initializeTerminal = new InitializeTerminal();
        final int[] result = new int[1];
        Thread thread = new Thread(() -> {
            content = "http://xiaomu1079.club/searchCounts/" +initializeTerminal.getTerminal(TrackApplication.getContext());
            request = new Request.Builder().url(content).get().build();
            try {
                response = okHttpClient.newCall(request).execute();
                result[0] =Integer.parseInt(response.body().string());
//                setCounts(result[0]);
//                Log.d("TrackHistoryService", a);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        /*try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }*/
//        long a = initializeTerminal.getTerminal(TrackApplication.getContext());
//        Log.d("TrackHistoryService", "a:" + a);
        return result[0];
    }

    private void setCounts(int result) {
        this.counts = result;
    }


    private int getCounts(){
        return this.counts;
    }
}
