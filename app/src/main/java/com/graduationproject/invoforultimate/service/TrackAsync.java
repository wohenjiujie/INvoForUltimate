package com.graduationproject.invoforultimate.service;

import android.os.AsyncTask;
import android.util.Log;

import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.constant.OnTrackCountsPostListener;
import com.graduationproject.invoforultimate.app.TrackApplication;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;

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
 * on 2019-11-06.
 */
public class TrackAsync extends AsyncTask<String, Integer, String> {
    private InitializeTerminal initializeTerminal;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Response response;
    private String content;
    private Request request;
    private String result;
    private OnTrackCountsPostListener onTrackCountsPostListener;
    private int type;
    private int trackID;
    private RequestBody requestBody=null;

    /*
*
*
* */
    /*public void setOnTrackCountsPostListener(OnTrackCountsPostListener onTrackCountsPostListener) {
        this.onTrackCountsPostListener = onTrackCountsPostListener;
    }*/

    public TrackAsync(int type,OnTrackCountsPostListener onTrackCountsPostListener) {
        super();
        this.type = type;
        this.onTrackCountsPostListener = onTrackCountsPostListener;
    }
    public TrackAsync(int type,int trackID,OnTrackCountsPostListener onTrackCountsPostListener) {
        super();
        this.type = type;
        this.onTrackCountsPostListener = onTrackCountsPostListener;
        this.trackID = trackID;
    }



    @Override
    protected String doInBackground(String... strings) {
        if (type == 1) {
            initializeTerminal = new InitializeTerminal();
            content = "http://xiaomu1079.club/searchCounts/" + initializeTerminal.getTerminal(TrackApplication.getContext());
            request = new Request.Builder().url(content).get().build();
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == 2) {
            initializeTerminal = new InitializeTerminal();
            content = "http://xiaomu1079.club/getTerminalInfo/" + initializeTerminal.getTerminal(TrackApplication.getContext());
            request = new Request.Builder().url(content).get().build();
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == 3) {
            initializeTerminal = new InitializeTerminal();
            content = "https://tsapi.amap.com/v1/track/terminal/trsearch?key=b26487968ee70a1647954c49b55828f2&sid="
                    + Constants.ServiceID + "&tid=" + initializeTerminal.getTerminal(TrackApplication.getContext())
                    + "&trid=" + trackID + "&pagesize=999";
            request = new Request.Builder().url(content).get().build();
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    result = response.body().string();
//                    Log.d("TrackAsync", result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (4==type) {
            initializeTerminal = new InitializeTerminal();
            content = "http://xiaomu1079.club/decreaseCounts/"+initializeTerminal.getTerminal(TrackApplication.getContext());
            request = new Request.Builder().url(content).get().build();


            MediaType mediaType=MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("terminal", initializeTerminal.getTerminal(TrackApplication.getContext()));
                jsonObject.put("track", trackID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestBody = RequestBody.create(mediaType, jsonObject.toString());
            Request request2 = new Request.Builder().url(Constants.deleteTrackInfo).post(requestBody).build();

            try {
                response = okHttpClient.newCall(request).execute();
                Response response2 = okHttpClient.newCall(request2).execute();
                Log.d("TrackHistoryAdapter", "is done?>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (type == 1) {
            onTrackCountsPostListener.onGetCounts(s);
        } else if (type == 2) {
            onTrackCountsPostListener.onGetInfo(s);
        } else if (type == 3) {
            onTrackCountsPostListener.onGetID(s);
        } else if (4 == type) {
            onTrackCountsPostListener.onDeleteInfo();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
