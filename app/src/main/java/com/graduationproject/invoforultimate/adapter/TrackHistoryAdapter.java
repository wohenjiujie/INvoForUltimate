package com.graduationproject.invoforultimate.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.constant.OnTrackAdapterListener;
import com.graduationproject.invoforultimate.service.TrackAsync;
import com.graduationproject.invoforultimate.service.TrackHistoryService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

//    private onTrackItemClickListener onTrackItemClickListener;
    private OnTrackAdapterListener onTrackAdapter;

    public TrackHistoryAdapter(Context context, int counts, JSONArray jsonArray, OnTrackAdapterListener onTrackAdapter) {
        this.context = context;
        this.counts = counts;
//        this.s = string;
        this.jsonArray = jsonArray;
//        this.onTrackItemClickListener = onTrackItemClickListener;
        this.onTrackAdapter = onTrackAdapter;
    }
//    , onTrackItemClickListener onTrackItemClickListener

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
                String distance = jsonObject.getString("mileage");
                trackID[i] = Integer.parseInt(jsonObject.getString("track"));
                holder.desc.setText(desc);
                holder.time.setText(date);
                holder.timeConsuming.setText("用时:"+time);
                holder.distance.setText("里程:"+distance+"m");
//                Log.d("my", "foreach:trackID:" + trackID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackAdapter.onSwitch(position,trackID);
//                Log.d("TrackHistoryAdapter", "trackID:" + trackID[position]);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackAdapter.onDelete(position,trackID);
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

        private TextView desc,timeConsuming,time,distance;
        private final TextView delete;

        /**
         * @param itemView
         */

        public TrackHistoryViewHolder(@NonNull View itemView) throws JSONException {
            super(itemView);
            desc = itemView.findViewById(R.id.track_title);
            time = itemView.findViewById(R.id.track_subtitle);
            timeConsuming = itemView.findViewById(R.id.track_extra_2);
            delete = itemView.findViewById(R.id.delete_track_info);
            distance = itemView.findViewById(R.id.track_extra_1);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String a = jsonObject.getString("time");
            Log.d("TrackHistoryViewHolder", a);
            /*JSONArray jsonArray = new JSONArray(s);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String a = jsonObject.getString("description");
            Log.d("TrackHistoryViewHolder", a);*/
        }
    }

   /* public interface onTrackItemClickListener{
        void onSwitch(int pos,int id[]);
    }
*/
  /*  public interface onTrackDeleteListener {
        void onDelete(int pos,int id[]);
    }*/

}

