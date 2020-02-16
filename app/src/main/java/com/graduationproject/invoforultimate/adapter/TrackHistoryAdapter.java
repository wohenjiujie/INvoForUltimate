package com.graduationproject.invoforultimate.adapter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.entity.bean.TrackHistoryInfo;
import com.graduationproject.invoforultimate.listener.OnTrackAdapterListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.graduationproject.invoforultimate.R2.id.delete_track_info;
import static com.graduationproject.invoforultimate.R2.id.track_extra_1;
import static com.graduationproject.invoforultimate.R2.id.track_extra_2;
import static com.graduationproject.invoforultimate.R2.id.track_subtitle;
import static com.graduationproject.invoforultimate.R2.id.track_title;
import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;
import static com.graduationproject.invoforultimate.ui.activity.TrackHistoryActivity.TAG;

/**
 * Created by INvo
 * on 2019-10-16.
 */
public class TrackHistoryAdapter extends RecyclerView.Adapter<TrackHistoryAdapter.TrackHistoryViewHolder> {
    private TrackHistoryInfo trackHistoryInfo;
    private OnTrackAdapterListener onTrackAdapter;

    public TrackHistoryAdapter(TrackHistoryInfo trackHistoryInfo, OnTrackAdapterListener onTrackAdapter) {
        super();
        this.trackHistoryInfo = trackHistoryInfo;
        this.onTrackAdapter = onTrackAdapter;
    }

    @NonNull
    @Override
    public TrackHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackHistoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.track_history_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackHistoryViewHolder holder, int position) {
        for (int i = 0; i <= position; i++) {
            holder.desc.setText((String) trackHistoryInfo.getDesc().get(i));
            holder.date.setText((String) trackHistoryInfo.getDate().get(i));
            holder.time.setText("用时:" + trackHistoryInfo.getTime().get(i));
            holder.distance.setText("里程:" + trackHistoryInfo.getDistance().get(i) + "m");
            holder.thumbnail.setImageBitmap((Bitmap) trackHistoryInfo.getBitmap().get(i));
        }
        holder.desc.setOnClickListener(v -> onTrackAdapter.onSwitch(trackHistoryInfo.getTrackID().get(position)));
        holder.delete.setOnClickListener(v -> onTrackAdapter.onDelete(trackHistoryInfo.getTrackID().get(position)));
    }

    @Override
    public int getItemCount() {
        return Integer.parseInt((String) trackHistoryInfo.getCounts());
    }

    class TrackHistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(track_title)
        TextView desc;
        @BindView(track_subtitle)
        TextView date;
        @BindView(track_extra_2)
        TextView time;
        @BindView(track_extra_1)
        TextView distance;
        @BindView(delete_track_info)
        TextView delete;
        @BindView(R.id.track_extra_3)
        ImageView thumbnail;

        public TrackHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

