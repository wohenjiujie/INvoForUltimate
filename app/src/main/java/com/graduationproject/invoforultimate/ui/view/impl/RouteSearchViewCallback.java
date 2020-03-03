package com.graduationproject.invoforultimate.ui.view.impl;

import com.amap.api.services.help.Tip;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;

import java.util.List;

/**
 * Created by INvo
 * on 2020-03-03.
 */
public interface RouteSearchViewCallback extends ViewCallback {

    public void onGetInputCallback(List<Tip> list,int i);
}
