package com.graduationproject.invoforultimate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.help.Tip;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.listener.OnNavigationTipsAdapterListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.graduationproject.invoforultimate.R2.id.navigation_address;
import static com.graduationproject.invoforultimate.R2.id.navigation_linear;
import static com.graduationproject.invoforultimate.R2.id.navigation_name;
import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;

/**
 * Created by INvo
 * on 2020-03-02.
 */
public class NavigationTipsAdapter extends RecyclerView.Adapter<NavigationTipsAdapter.NavigationTipsViewHolder> {
    private List<Tip> tipList;
    private OnNavigationTipsAdapterListener listener;

    public NavigationTipsAdapter(@Nullable List<Tip> tipList, OnNavigationTipsAdapterListener listener) {
        super();
        this.tipList = tipList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NavigationTipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NavigationTipsViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.navigation_tips_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationTipsViewHolder holder, int position) {
        if (null != tipList) {
            for (int i = 0; i <= position; i++) {
                holder.name.setText(tipList.get(position).getName());
                holder.address.setText(tipList.get(position).getAddress());
            }
            holder.linearLayout.setOnClickListener(v -> listener.onSwitch(tipList.get(position).getPoint()));
        }
    }

    @Override
    public int getItemCount() {
        if (null == tipList) {
            return 0;
        }
        return tipList.size();
    }

    class NavigationTipsViewHolder extends RecyclerView.ViewHolder {
        @BindView(navigation_name)
        TextView name;
        @BindView(navigation_address)
        TextView address;
        @BindView(navigation_linear)
        LinearLayout linearLayout;

        public NavigationTipsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
