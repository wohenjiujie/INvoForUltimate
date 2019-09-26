package com.graduationproject.invoforultimate.initialize;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.graduationproject.invoforultimate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INvo
 * on 2019-09-24.
 */
public class MapSetting {
    private static MapView mapView;
    private static AMap aMap;
    private static MyLocationStyle myLocationStyle;
    private static UiSettings uiSettings;//定义一个UiSettings对象
    List<LatLng> latLngs = new ArrayList<LatLng>();

    Polyline polyline;

    public MapSetting(MapView mapView, AMap aMap, MyLocationStyle myLocationStyle) {
        this.mapView = mapView;
        this.aMap = aMap;
        this.myLocationStyle = myLocationStyle;
    }

    public void initialize() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
            /*latLngs.add(new LatLng(30.679879,104.064855));//成都
            latLngs.add(new LatLng(39.90403,116.407525));//北京
            polyline =aMap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(20).color(Color.argb(255, 1, 1, 1)));*/
        }
        mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(14));
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMap().setMyLocationStyle(
                new MyLocationStyle()
                        .interval(2000)
                        .myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        );
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.interval(5000);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//设置定位蓝点精度圆圈的填充颜色的方法。
        uiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        uiSettings.setZoomControlsEnabled(false);

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
    }

}
