package com.graduationproject.invoforultimate.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Created by INvo
 * on 2019-09-25.
 */
public final class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context ctx, @Nullable String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showLongToast(Context ctx, @Nullable String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
