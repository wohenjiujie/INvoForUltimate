package com.graduationproject.invoforultimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;

import com.google.gson.JsonArray;
import com.graduationproject.invoforultimate.adapter.TrackHistoryAdapter;
import com.graduationproject.invoforultimate.constant.OnTrackCountsPostListener;
import com.graduationproject.invoforultimate.constant.TrackApplication;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import com.graduationproject.invoforultimate.service.TrackAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PipedReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrackHistoryActivity extends BaseActivity implements OnTrackCountsPostListener {

    private Request request;
    private String content;
    private Response response;
    private InitializeTerminal initializeTerminal;
    private RecyclerView recyclerView;
    private String result;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private TrackAsync trackAsync;
    private int counts;
    private String startUnix;
    private String endUnix;
    /*public TrackHistoryActivity(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }*/

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_track_history);

        recyclerView = findViewById(R.id.track_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(TrackHistoryActivity.this));
        recyclerView.addItemDecoration(new Decoration());
        new TrackAsync(1, this).execute();
//        sendResponse();
//        recyclerView.setAdapter(new TrackHistoryAdapter(getContext()));
//        recyclerView.getAdapter().getItemId();

    }

    /*private void sendResponse() {
     *//*new Thread(new Runnable() {
            @Override
            public void run() {
                initializeTerminal = new InitializeTerminal();
                content = "http://xiaomu1079.club/searchCounts/" +initializeTerminal.getTerminal(TrackApplication.getContext());
                request = new Request.Builder().url(content).get().build();
                try {
                    response = okHttpClient.newCall(request).execute();
                    result =response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    showResponse(result);
                }
            }
        }).start();*//*
        trackAsync = new TrackAsync(this);
        trackAsync.execute();

//        trackAsync.setOnTrackCountsPostListener(this);
    }*/

    private void showResponse(JSONArray jsonArray) {
        runOnUiThread(() -> {
            recyclerView.setAdapter(new TrackHistoryAdapter(getContext(), getCounts(), jsonArray, new TrackHistoryAdapter.onTrackItemClickListener() {
                @Override
                public void onSwitch(int pos, int id[]) {

                    start(id[pos]);
                    /*Intent intent = new Intent();
                    intent.setClass(TrackHistoryActivity.this, TrackReplayActivity.class);
                    Bundle bundle = new Bundle();
                   *//* bundle.putInt("trackID", id);
                    bundle.putString("startUnix", getStartUnix());
                    bundle.putString("endUnix", getEndUnix());*//*
                    Log.d("TrackHistoryActivity", "startUnix:"+getStartUnix());
                    Log.d("TrackHistoryActivity", "endUnix:"+getEndUnix());
                    intent.putExtras(bundle);
//        startActivity(intent);*/

                }
            }));
        });
    }

    private void start(int id) {
        new TrackAsync(3, id, this).execute();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_track_history;
    }

    @Override
    public void onGetInfo(String result) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showResponse(jsonArray);
    }

    @Override
    public void onGetID(String result) {
        String a = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
           JSONObject test1 = jsonObject.getJSONObject("tracks");
            Log.d("TrackHistoryActivity", "test1:" + test1);
//            Log.d("TrackHistoryActivity", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void ToastText(String text) {
        super.ToastText(text);
    }

    @Override
    protected void ToastMsg(int msg) {
        super.ToastMsg(msg);
    }

    @Override
    protected void OnProcessCallBack(int msg) {


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onGetCounts(String result) {
//        showResponse(result);
        setCounts(Integer.parseInt(result));
        new TrackAsync(2, this).execute();
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getStartUnix() {
        return startUnix;
    }

    public void setStartUnix(String startUnix) {
        this.startUnix = startUnix;
    }

    public String getEndUnix() {
        return endUnix;
    }

    public void setEndUnix(String endUnix) {
        this.endUnix = endUnix;
    }

    class Decoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.divider));
        }
    }

}
