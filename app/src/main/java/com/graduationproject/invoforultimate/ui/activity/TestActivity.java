package com.graduationproject.invoforultimate.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.graduationproject.invoforultimate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.invoker_test)
    ImageView imageView;
//    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        String text = bundle.getString("bitmap");
        byte[] bytes = Base64.decode(text, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        imageView = this.findViewById(R.id.invoker_test);
        imageView.setImageBitmap(bitmap);
    }


}
