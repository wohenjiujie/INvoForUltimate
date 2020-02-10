package com.graduationproject.invoforultimate.model.impl;

import android.os.Bundle;

import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.DriveMode;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.Track;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.graduationproject.invoforultimate.adapter.TrackHistoryListener;
import com.graduationproject.invoforultimate.app.TrackApplication;
import com.graduationproject.invoforultimate.bean.TerminalInfo;
import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import com.graduationproject.invoforultimate.model.TrackReplayModel;

import java.util.List;

import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;
import static com.graduationproject.invoforultimate.bean.constants.TrackReplayConstants.ALL_EMPTY;
import static com.graduationproject.invoforultimate.bean.constants.TrackReplayConstants.NOT_NETWORK;
import static com.graduationproject.invoforultimate.bean.constants.TrackReplayConstants.TERMINAL_NOT_EXIST;
import static com.graduationproject.invoforultimate.bean.constants.TrackReplayConstants.TRACKS_EMPTY;
import static com.graduationproject.invoforultimate.bean.constants.TrackReplayConstants.TRACK_NOT_RESPONSE;

/**
 * Created by INvo
 * on 2020-02-10.
 */
public class TrackReplayImpl {

    private AMapTrackClient aMapTrackClient;
    private Bundle bundle;
    private TrackReplayModel trackReplayModel;

    public TrackReplayImpl(Bundle bundle, TrackReplayModel trackReplayModel) {
        this.bundle = bundle;
        this.trackReplayModel = trackReplayModel;
        trackReplay();

    }

    public void trackReplay() {
        int trackID = bundle.getInt("trackID");
        long startUnix = Long.valueOf(bundle.getString("startUnix"));
        long endUnix = Long.valueOf(bundle.getString("endUnix"));
        aMapTrackClient = new AMapTrackClient(getContext());

        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.ServiceID, TerminalInfo.getTerminalName()), new TrackHistoryListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
                                Constants.ServiceID,
                                TerminalInfo.getTerminal(),
                                trackID,     // 轨迹ID
                                startUnix,  //开始时间戳
                                endUnix,    //结束时间戳
                                0,      // 不启用去噪
                                0,   // 启用绑路
                                0,      // 不进行精度过滤
                                DriveMode.DRIVING,  // 当前仅支持驾车模式
                                0,     // 距离补偿
                                5000,   // 距离补偿，只有超过5km的点才启用距离补偿
                                1,  // 结果应该包含轨迹点信息
                                1,  // 返回1：1数据
                                100    // 一页不超过100条
                        );
                        aMapTrackClient.queryTerminalTrack(queryTrackRequest, new TrackHistoryListener() {
                            @Override
                            public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
                                if (queryTrackResponse.isSuccess()) {
                                    List<Track> tracks = queryTrackResponse.getTracks();
                                    if (tracks != null && !tracks.isEmpty()) {
                                        boolean allEmpty = true;
                                        for (Track track : tracks) {
                                            List<Point> points = track.getPoints();
                                            if (points != null && points.size() > 0) {
                                                allEmpty = false;
                                                /**
                                                 *将采取的坐标点绘制在MapView上
                                                 */
                                                trackReplayModel.onTrackPointsCallback(points);
                                            }
                                        }
                                        if (allEmpty) {
                                            trackReplayModel.onTrackPointsResultCallback(ALL_EMPTY);
                                        } else {
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("查询成功：本次运动的里程为");
                                            for (Track track : tracks) {
                                                stringBuilder.append(track.getDistance()).append("m");
                                            }
                                            trackReplayModel.onTrackPointsResultCallback(stringBuilder.toString());
                                        }
                                    } else {
                                        trackReplayModel.onTrackPointsResultCallback(TRACKS_EMPTY);
                                    }
                                } else {
                                    trackReplayModel.onTrackPointsResultCallback(TRACK_NOT_RESPONSE);
                                }
                            }
                        });
                    } else {
                        trackReplayModel.onTrackPointsResultCallback(TERMINAL_NOT_EXIST);
                    }
                } else {
                    trackReplayModel.onTrackPointsResultCallback(NOT_NETWORK + queryTerminalResponse.getErrorMsg());
                }
            }
        });
    }
}
