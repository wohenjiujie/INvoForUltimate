package com.graduationproject.invoforultimate.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.graduationproject.invoforultimate.MainActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.TrackHistoryActivity;
import com.graduationproject.invoforultimate.constant.OnTrackCountsPostListener;
import com.graduationproject.invoforultimate.constant.TrackApplication;
import com.graduationproject.invoforultimate.service.TrackAsync;
import com.graduationproject.invoforultimate.service.TrackHistoryService;
import com.graduationproject.invoforultimate.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.SecureDirectoryStream;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by INvo
 * on 2019-10-16.
 */


public class TrackHistoryAdapter extends RecyclerView.Adapter<TrackHistoryAdapter.TrackHistoryViewHolder> {
    /**
     * 实际开发传入list列表参数
     */
    private Context context;
    private TrackHistoryService trackHistoryService = new TrackHistoryService();
    private TrackAsync trackAsync;
    private int counts;
    private String s;
    private JSONArray jsonArray;
    private int trackID[]=new int[1000];

    private onTrackItemClickListener onTrackItemClickListener;

    public TrackHistoryAdapter(Context context, int counts, JSONArray jsonArray,onTrackItemClickListener onTrackItemClickListener) {
        this.context = context;
        this.counts = counts;
//        this.s = string;
        this.jsonArray = jsonArray;
        this.onTrackItemClickListener = onTrackItemClickListener;
    }

    @NonNull
    @Override
    public TrackHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            return new TrackHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.track_history_recycler_view, parent, false));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置绑定的viewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TrackHistoryViewHolder holder, int position) {
        JSONObject jsonObject;
        String startUnix;
        String endUnix;
        for (int i = 0; i <= position; i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                String desc = jsonObject.getString("description");
                String date = jsonObject.getString("date");
                String time = jsonObject.getString("time");
                trackID[i] = Integer.parseInt(jsonObject.getString("track"));
                holder.desc.setText(desc);
                holder.time.setText(date);
                holder.timeConsuming.setText("用时："+time);
//                Log.d("TrackHistoryAdapter", "foreach:trackID:" + trackID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackItemClickListener.onSwitch(position,trackID);
//                Log.d("TrackHistoryAdapter", "trackID:" + trackID[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        /*trackAsync = new TrackAsync();
        trackAsync.execute();
        trackAsync.setOnTrackCountsPostListener(this);
        int a = getCounts();
        Log.d("TrackHistoryAdapter", "aaaaatest:" + a);
        return getCounts();*/
        Log.d("TrackHistoryAdapter", "counts:" + counts);
        return counts;
    }
    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }


    /**
     * TrackHistory recyclerView控件的布局
     */
    class TrackHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView desc,timeConsuming,time;

        /**
         * @param itemView
         */

        public TrackHistoryViewHolder(@NonNull View itemView) throws JSONException {
            super(itemView);
            desc = itemView.findViewById(R.id.track_title);
            time = itemView.findViewById(R.id.track_subtitle);
            timeConsuming = itemView.findViewById(R.id.track_extra_2);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String a = jsonObject.getString("time");
            Log.d("TrackHistoryViewHolder", a);
            /*JSONArray jsonArray = new JSONArray(s);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String a = jsonObject.getString("description");
            Log.d("TrackHistoryViewHolder", a);*/
        }
    }

    public interface onTrackItemClickListener{
        void onSwitch(int pos,int id[]);
    }
}

