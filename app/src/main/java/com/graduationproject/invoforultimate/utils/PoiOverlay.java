package com.graduationproject.invoforultimate.utils;

import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by INvo
 * on 2019-11-03.
 * PoiOverlay 基本构造方法
 */
public class PoiOverlay {

    private List<PoiItem> poiItems;
    private AMap aMap;
    private ArrayList<Marker> markerArrayList = new ArrayList<>();

    public PoiOverlay(AMap aMap, List<PoiItem> poiItems) {
        this.aMap = aMap;
        this.poiItems = poiItems;
    }

    public void addToMap() {
        try {
            for (int i = 0; i < poiItems.size(); i++) {
                Marker marker = aMap.addMarker(getMarkerOptions(i));
                marker.setObject(i);
                markerArrayList.add(marker);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void removeFromMap() {
        for (Marker mark : markerArrayList) {
            mark.remove();
        }
        poiItems.clear();
        markerArrayList.clear();
        aMap = null;
    }

    public void zoomToSpan() {
        try {
            if (poiItems != null && poiItems.size() > 0) {
                if (aMap == null)
                    return;
                if (poiItems.size() == 1) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(poiItems.get(0).getLatLonPoint().getLatitude(),
                            poiItems.get(0).getLatLonPoint().getLongitude()), 18f));
                } else {
                    LatLngBounds bounds = getLatLngBounds();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < poiItems.size(); i++) {
            b.include(new LatLng(poiItems.get(i).getLatLonPoint().getLatitude(),
                    poiItems.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int index) {
        return new MarkerOptions()
                .position(
                        new LatLng(poiItems.get(index).getLatLonPoint()
                                .getLatitude(), poiItems.get(index)
                                .getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .icon(getBitmapDescriptor(index));
    }

    protected BitmapDescriptor getBitmapDescriptor(int index) {
        return null;
    }

    protected String getTitle(int index) {
        return poiItems.get(index).getTitle();
    }

    protected String getSnippet(int index) {
        return poiItems.get(index).getSnippet();
    }

    public float getDistance(int index) {
        return poiItems.get(index).getDistance();
    }

    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < markerArrayList.size(); i++) {
            if (markerArrayList.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    public PoiItem getPoiItem(int index) {
        if (index < 0 || index >= poiItems.size()) {
            return null;
        }
        return poiItems.get(index);
    }
}
