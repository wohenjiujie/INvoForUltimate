package com.graduationproject.invoforultimate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.util.ToastUtil;

import java.util.List;

/**
 * Created by INvo
 * on 2019-10-16.
 */


public class TrackHistoryAdapter extends RecyclerView.Adapter<TrackHistoryAdapter.TrackHistoryViewHolder> {
    /**
     * 实际开发传入list列表参数
     */
    private Context context;


    public TrackHistoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TrackHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.track_history_recycler_view, parent,false));
    }

    /**
     * 设置绑定的viewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TrackHistoryViewHolder holder, int position) {
        holder.textView.setText("hahha");
        holder.itemView.setOnClickListener(v -> {
            /*if (1 ==position) {
                ToastUtil.showToast(context, "hhaha");
            }*/
            switch (position) {
                case 1:
                    ToastUtil.showToast(context, "hhaha" + (position + 10));
                    break;
                case 5:
                    ToastUtil.showToast(context, "hhaha" + position);
                    break;
            }
            /*if (1 != position || 5 != position) {
                ToastUtil.showToast(context, "s" + position);
            }*/
        });
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    /**
     * TrackHistory recyclerView控件的布局
     */
    class TrackHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        /**
         * @param itemView
         */

        public TrackHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.track_list);
        }
    }
}

