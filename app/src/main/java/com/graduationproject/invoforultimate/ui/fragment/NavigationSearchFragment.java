package com.graduationproject.invoforultimate.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.AmapRouteActivity;
import com.graduationproject.invoforultimate.BaseFragment;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.entity.bean.LatLonPointParcelable;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.ui.view.ViewCallback;

import butterknife.BindView;

import static com.graduationproject.invoforultimate.R2.id.navigation_map;
import static com.graduationproject.invoforultimate.R2.id.navigation_ride;
import static com.graduationproject.invoforultimate.R2.id.navigation_walk;
import static com.graduationproject.invoforultimate.app.TrackApplication.getAddress;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLatLonPoint;

/**
 * Created by INvo
 * on 2020-01-11.
 */
public class NavigationSearchFragment extends BaseFragment {
    @BindView(navigation_map)
    TextureMapView textureMapView;
    @BindView(navigation_ride)
    CardView cardViewRide;
    @BindView(navigation_walk)
    CardView cardViewWalk;
    private CardView cardView;
    private LatLonPointParcelable parcelable;
    private Poi start, end;
    private Animation animation;
    private long duration = 1000L;
    private LatLng latLngEnd, latLngStart;
    private Marker marker;

    public static NavigationSearchFragment newInstance(@Nullable Bundle args) {
        NavigationSearchFragment fragment = new NavigationSearchFragment();
        if (null != args) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_navigation_model, container, false);
    }

    @Override
    protected void initControls(@Nullable Bundle savedInstanceState) {
        textureMapView.onCreate(savedInstanceState);
        parcelable = getArguments().getParcelable("navi");
        textureMapView.getMap().getUiSettings().setZoomControlsEnabled(false);
        latLngEnd = new LatLng(parcelable.getLatLonPoint().getLatitude(), parcelable.getLatLonPoint().getLongitude());
        latLngStart = new LatLng(getLatLonPoint().getLongitude(), getLatLonPoint().getLatitude());
        textureMapView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(latLngEnd));
        textureMapView.getMap().animateCamera(CameraUpdateFactory.zoomTo(18), 500, null);
        marker = textureMapView.getMap().addMarker(new MarkerOptions().position(latLngEnd).snippet("Marker For Invo"));
        animation = new RotateAnimation(marker.getRotateAngle(), marker.getRotateAngle() + 360, 0, 0, 0);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        marker.setAnimation(animation);
        marker.startAnimation();

        if (getArguments().getInt("style") == 0) {
            cardView = cardViewRide;
        } else if (getArguments().getInt("style") == 1) {
            cardView = cardViewWalk;
        }
        cardView.setVisibility(View.VISIBLE);
        cardView.setOnClickListener(v -> {
            start = new Poi(getAddress(), latLngStart, "");
            end = new Poi("", latLngEnd, "");
            AmapNaviPage.getInstance().showRouteActivity(getContext(), new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), null, AmapRouteActivity.class);
        });
    }

    @Override
    protected TextureMapView loadMap(Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected Presenter loadP() {
        return null;
    }

    @Override
    protected ViewCallback loadV() {
        return null;
    }

    @Override
    public void onKeyDownChild(int keyCode, KeyEvent event) {

    }
}
