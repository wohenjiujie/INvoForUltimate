package com.graduationproject.invoforultimate.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graduationproject.invoforultimate.MainActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.TrackHistoryActivity;

/**
 * Created by INvo
 * on 2019-09-24.
 */
public class BottomNavigationUtil {
    /*
     *整合底部导航栏功能
     *
     * */

    private static AlertDialog.Builder builder;
    private static BottomNavigationView bottomNavigationView;
    //    private static int itemId;
    private static Context context;
    private static EditText editText;
    private static Button button;
    private static TextView textView1, textView2, textView3;
    private DatabaseUtil databaseUtil;
    private static boolean check;


    public BottomNavigationUtil(Context context, AlertDialog.Builder builder, BottomNavigationView bottomNavigationView, EditText editText, Button button, TextView textView1, TextView textView2, TextView textView3) {
        /*
         * 鹰眼服务构造方法
         * */
        this.bottomNavigationView = bottomNavigationView;
        this.builder = builder;
//        this.itemId = itemId;
        this.context = context;
        this.button = button;
        this.editText = editText;
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.textView3 = textView3;
    }

    public BottomNavigationUtil() {

    }

    public void ItemSelected(int itemId) {
//        check = true;
        if (itemId == 2) {
            Intent intent = new Intent();
            intent.setClass(context, TrackHistoryActivity.class);
            context.startActivity(intent);
        }
        if (itemId == 3) {
            builder.setTitle(R.string.hawkEyeService).setMessage(R.string.trackService).setIcon(R.drawable.ic_launcher_background)
                    .setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.showToast(context, "test");
                        }
                    }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*databaseUtil = new DatabaseUtil(context);
                    if (!databaseUtil.isRegistration()) {
                        check = false;
                        Log.d("my", "checkAfter:" + check);
                    } */

                    bottomNavigationView.setVisibility(View.INVISIBLE);//底部导航关闭显示}
//                        editText.setVisibility(View.VISIBLE);
//                        button.setVisibility(View.VISIBLE);
//                    textView1.setVisibility(View.VISIBLE);
//                    textView2.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.VISIBLE);
                }
            }).setCancelable(false).show();
        }
        Log.d("my", "check:" + check);
    }
}
