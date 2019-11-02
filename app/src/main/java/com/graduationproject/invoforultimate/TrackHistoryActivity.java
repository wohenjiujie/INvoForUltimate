package com.graduationproject.invoforultimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.graduationproject.invoforultimate.adapter.TrackHistoryAdapter;

public class TrackHistoryActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    /*public TrackHistoryActivity(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_history);
        recyclerView=findViewById(R.id.track_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(TrackHistoryActivity.this));
        recyclerView.addItemDecoration(new Decoration());
        recyclerView.setAdapter(new TrackHistoryAdapter(TrackHistoryActivity.this));
    }

    class Decoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.divider));
        }
    }

}
