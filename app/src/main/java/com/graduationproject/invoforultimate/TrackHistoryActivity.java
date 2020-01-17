package com.graduationproject.invoforultimate;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.graduationproject.invoforultimate.adapter.TrackHistoryAdapter;
import com.graduationproject.invoforultimate.constant.OnTrackCountsPostListener;
import com.graduationproject.invoforultimate.constant.TrackApplication;
import com.graduationproject.invoforultimate.constant.OnTrackAdapterListener;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import com.graduationproject.invoforultimate.service.TrackAsync;
import com.graduationproject.invoforultimate.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrackHistoryActivity extends BaseActivity implements OnTrackCountsPostListener, OnTrackAdapterListener {

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
    private int TrackID;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    private SwipeRefreshLayout swipeRefreshLayout;
    /*public TrackHistoryActivity(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }*/

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_track_history);
        swipeRefreshLayout = this.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorBlack, R.color.colorGreen, R.color.colorBlue, R.color.colorAccent);


        recyclerView = findViewById(R.id.track_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(TrackHistoryActivity.this));
        recyclerView.addItemDecoration(new Decoration());
        new TrackAsync(1, this).execute();
//        sendResponse();
//        recyclerView.setAdapter(new TrackHistoryAdapter(getContext()));
//        recyclerView.getAdapter().getItemId();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            handler.postDelayed(runnable, 500);
            new TrackAsync(1, this).execute();

        });
    }

    /**
     * 测试handler中的线程，后期不需要实现handleMessage方法
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                myTest();
                new Thread(new Thread(){
                    @Override
                    public void run() {
                        Log.d("mylog", "test handler thread222222");
                    }
                }).start();
            }
            /*if (msg.what == 1) {
                Log.d("mylog", "test handler thread2");
            }*/
        }
    };

    private void myTest() {
        Thread thread=new Thread(new Thread(){
            @Override
            public void run() {
                Log.d("mylog", "test handler thread2");
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showToast(TrackApplication.getContext(), "更新成功");
            handler.sendEmptyMessage(1);
        }
    };

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
            recyclerView.setAdapter(new TrackHistoryAdapter(getContext(), getCounts(), jsonArray, this));
        });
    }
    @Override
    public void onSwitch(int pos, int[] id) {
        start(id[pos]);
    }
    private void start(int id) {
        Log.d("myTrackID", "id[pos]:" + id);
        setTrackID(id);
        new TrackAsync(3, id, this).execute();
    }

    @Override
    public void onDelete(int pos, int[] id) {
        swipeRefreshLayout.setRefreshing(true);
        /*ToastUtil.showToast(TrackApplication.getContext(), "testDelete");
        Log.d("TrackHistoryAdapter", "testdelet");*/
        new TrackAsync(4, id[pos], this).execute();

    }



    @Override
    public void onDeleteInfo() {
        new TrackAsync(1, this).execute();
        ToastUtil.showToast(TrackApplication.getContext(), "删除成功");
        swipeRefreshLayout.setRefreshing(false);
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
        /**
         * 在此方法下获取时间戳和id
         */
        String points;
        int counts;
        Log.d("myResult_onGetID", result);
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
            JSONArray jsonArray = jsonObject.getJSONArray("tracks");

            points = jsonArray.getJSONObject(0).getString("counts");
            counts = Integer.parseInt(points);//时间戳数量
//            Log.d("myCounts", "counts:" + counts);
            String po = jsonArray.getJSONObject(0).getString("points");
            Log.d("myPoints", po);

            startUnix = jsonArray.getJSONObject(0).getJSONArray("points").getJSONObject(0).getString("locatetime");
            endUnix = jsonArray.getJSONObject(0).getJSONArray("points").getJSONObject(counts - 1).getString("locatetime");
            Log.d("myStartUnix", startUnix);
            Log.d("myEndUnix", endUnix);
            if (null != startUnix && null != endUnix) {
                setStartUnix(startUnix);
                setEndUnix(endUnix);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Intent intent = new Intent();
            intent.setClass(TrackHistoryActivity.this, TrackReplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("trackID", getTrackID());
            bundle.putString("startUnix", getStartUnix());
            bundle.putString("endUnix", getEndUnix());
//            Log.d("TrackHistoryActivity", "startUnix:"+getStartUnix());
//            Log.d("TrackHistoryActivity", "endUnix:"+getEndUnix());
            intent.putExtras(bundle);
            startActivity(intent);
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

    public int getTrackID() {
        return TrackID;
    }

    public void setTrackID(int trackID) {
        TrackID = trackID;
    }


    class Decoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.divider));
        }
    }

}
