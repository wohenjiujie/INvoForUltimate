package com.graduationproject.invoforultimate.listener;

import androidx.annotation.Nullable;

import com.amap.api.services.core.LatLonPoint;

/**
 * Created by INvo
 * on 2020-02-02.
 */
public interface OnNavigationTipsAdapterListener extends TrackListener {
    /**
     * OnNavigationTipsAdapterListener onClick callback
     * @param var
     */
    void onSwitch(@Nullable LatLonPoint var);
}
