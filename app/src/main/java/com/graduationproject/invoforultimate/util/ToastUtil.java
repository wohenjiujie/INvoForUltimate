package com.graduationproject.invoforultimate.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by INvo
 * on 2019-09-25.
 */
public final class ToastUtil{
    private static Toast toast;

    /**
     * 针对小米系列MIUI Toast会弹出应用名的不合理设计
     *
     * @param ctx
     * @param msg
     */
    @Deprecated
    public static void showToast(Context ctx, @Nullable String msg) {
        if (toast == null) {
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * Toast LENGTH_SHORT
     *
     * @param context
     * @param msg
     */
    public static void shortToast(Context context, @NonNull String msg) {
        toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    /**
     * Toast LENGTH_LONG
     *
     * @param context
     * @param msg
     */
    public static void LongToast(Context context, @NonNull String msg) {
        toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 针对小米系列MIUI Toast会弹出应用名的不合理设计
     *
     * @param ctx
     * @param msg
     */
    @Deprecated
    public static void showLongToast(Context ctx, @Nullable String msg) {
        if (toast == null) {
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
