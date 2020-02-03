package com.graduationproject.invoforultimate;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.DriveMode;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.Track;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.graduationproject.invoforultimate.adapter.TrackHistoryListener;
import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.constant.TrackApplication;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import java.util.LinkedList;
import java.util.List;
import butterknife.BindView;

public class TrackReplayActivity extends BaseActivity {

    private AMapTrackClient aMapTrackClient;
    @BindView(R.id.history_map)
    TextureMapView textureMapView;
    private List<Polyline> polyLines = new LinkedList<>();
    private List<Marker> MapMarkers = new LinkedList<>();
    private InitializeTerminal initializeTerminal;

    /**
     * 清除原有的地图绘制缓存
     */
    private void clearTracksOnMap() {
        for (Polyline polyline : polyLines) {
            polyline.remove();
        }
        for (Marker marker : MapMarkers) {
            marker.remove();
        }
        MapMarkers.clear();
        polyLines.clear();
    }

    private void drawTrackOnMap(List<Point> points) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE).width(20);
        if (points.size() > 0) {
            /**
             * 起点
             * available customize startPoint Icon
             */
            Point p = points.get(0);
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            MapMarkers.add(textureMapView.getMap().addMarker(markerOptions));
        }
        if (points.size() > 1) {
            /**
             * 终点
             * available customize endPoints Icon
             */
            Point p = points.get(points.size() - 1);
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            MapMarkers.add(textureMapView.getMap().addMarker(markerOptions));
        }
        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            polylineOptions.add(latLng);
            boundsBuilder.include(latLng);
        }
        Polyline polyline = textureMapView.getMap().addPolyline(polylineOptions);
        polyLines.add(polyline);
        textureMapView.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_track_replay;
    }

    @Override
    protected void OnProcessCallBack(int msg) {

    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        textureMapView.onCreate(savedInstanceState);
    }


    @Override
    protected void initControls(Bundle savedInstanceState) {
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        initializeTerminal = new InitializeTerminal();
        /**
         * 从传入的Bundle中获取TrackID与Unix时间戳
         */
        Bundle bundle = this.getIntent().getExtras();
        int trackID = bundle.getInt("trackID");
        long startUnix = Long.valueOf(bundle.getString("startUnix"));
        long endUnix = Long.valueOf(bundle.getString("endUnix"));

        Log.d("TrackReplayActivity", "startUnix:" + startUnix);
        Log.d("TrackReplayActivity", "trackID:" + trackID);
        Log.d("TrackReplayActivity", "endUnix:" + endUnix);

        clearTracksOnMap();
        aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constants.ServiceID, initializeTerminal.getTerminalName(TrackApplication.getContext())), new TrackHistoryListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
                if (queryTerminalResponse.isSuccess()) {
                    if (queryTerminalResponse.isTerminalExist()) {
                        QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
                                Constants.ServiceID,
                                initializeTerminal.getTerminal(getContext()),
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
                                                drawTrackOnMap(points);
                                            }
                                        }
                                        if (allEmpty) {
                                            ToastText("检索失败，可能是由于行动路径过短");
                                        } else {
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("查询成功：本次运动的里程为");
                                            for (Track track : tracks) {
                                                stringBuilder.append(track.getDistance()).append("m");
                                            }
                                            ToastText(stringBuilder.toString());
                                        }
                                    } else {
                                        ToastText("未获取到运动记录");
                                    }
                                } else {
                                    ToastText("查询运动记录失败,请检查您的网络设置");
                                }
                            }
                        });
                    } else {
                        ToastTextLong("该终端不存在，有Bug请联系开发者：1143803369@qq.com");
                    }
                } else {
                    ToastText("网络请求失败，错误原因: " + queryTerminalResponse.getErrorMsg());
                }
            }
        });
    }

    @Override
    protected void initListener(Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textureMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        textureMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        textureMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textureMapView.onDestroy();
    }
}
