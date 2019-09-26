package com.graduationproject.invoforultimate.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by INvo
 * on 2019-09-25.
 */
public final class InputUtil {
    /*
     * 自动关闭系统输入法
     * */

    public static void hideAllInputMethod(Activity activity) {
        //从系统服务中获取输入法管理器
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
