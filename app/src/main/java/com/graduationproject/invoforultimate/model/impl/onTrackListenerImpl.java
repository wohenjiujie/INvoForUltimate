package com.graduationproject.invoforultimate.model.impl;

import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;

/**
 * Created by INvo
 * on 2020-02-08.
 */
public  class onTrackListenerImpl implements OnTrackListener {

    public onTrackListenerImpl() {
        super();
    }

    @Override
    public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

    }

    @Override
    public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

    }

    @Override
    public void onDistanceCallback(DistanceResponse distanceResponse) {

    }

    @Override
    public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

    }

    @Override
    public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

    }

    @Override
    public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

    }

    @Override
    public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

    }

    @Override
    public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

    }
}
