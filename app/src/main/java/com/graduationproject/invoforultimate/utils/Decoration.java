package com.graduationproject.invoforultimate.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduationproject.invoforultimate.R;

import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public class Decoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, getContext().getResources().getDimensionPixelOffset(R.dimen.divider));
    }
}
