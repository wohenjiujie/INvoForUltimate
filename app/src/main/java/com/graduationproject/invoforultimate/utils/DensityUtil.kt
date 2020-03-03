package com.graduationproject.invoforultimate.utils

import android.content.Context

/**
 *Created by INvo
 *on 2020-01-11.
 */
class DensityUtil {
    companion object {
        /**
         * dp转换成px
         */
        fun  dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * px转换成dp
         */
        fun px2dp(context: Context, pxValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
    }
}