package com.graduationproject.invoforultimate.listener;

import com.amap.api.location.AMapLocation;

/**
 * Created by INvo
 * on 2020-03-03.
 */
public interface OnLocationServiceListener extends TrackListener {

    void getLocation(AMapLocation aMapLocation);
}
