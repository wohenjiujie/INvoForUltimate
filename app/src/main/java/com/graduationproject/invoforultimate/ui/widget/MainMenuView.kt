package com.graduationproject.invoforultimate.ui.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.amap.api.services.core.PoiItem
import com.graduationproject.invoforultimate.R

/**
 *Created by INvo
 *on 2019-09-27.
 * 自定义PopupWindow
 */
class MainMenuView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) :
        PopupWindow(context, attrs, defStyleAttr) {
    private var popHeight = 0

    init {

        contentView = LayoutInflater.from(context).inflate(R.layout.main_menu, null)

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        popHeight = contentView.measuredHeight

        width = context.resources.displayMetrics.widthPixels / 3
        height = WindowManager.LayoutParams.WRAP_CONTENT

        animationStyle = R.style.MyPopWindowMainLocatio

        setBackgroundDrawable(ColorDrawable())

        isFocusable = true

        isOutsideTouchable = true

        isTouchable = true

        setTouchInterceptor { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                true
            }
            false
        }
    }

    private lateinit var listener: OnSearchResultClickListener

    fun setResultClickListener(listener: OnSearchResultClickListener) {
        this.listener = listener
    }

    interface OnSearchResultClickListener {
        fun onClickItem(position: Int, title: String, poiItem: PoiItem)
    }

    fun getPopHeight(): Int {
        return popHeight
    }
}