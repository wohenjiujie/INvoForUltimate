package com.graduationproject.invoforultimate.ui.activity;

import android.os.Bundle;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.maps.TextureMapView;
import com.amap.api.services.help.Tip;
import com.google.android.material.tabs.TabLayout;
import com.graduationproject.invoforultimate.BaseAppCompatActivity;
import com.graduationproject.invoforultimate.R;
import com.graduationproject.invoforultimate.adapter.NavigationFragmentPagerAdapter;
import com.graduationproject.invoforultimate.adapter.NavigationTipsAdapter;
import com.graduationproject.invoforultimate.entity.bean.LatLonPointParcelable;
import com.graduationproject.invoforultimate.listener.OnTextWatcherImpl;
import com.graduationproject.invoforultimate.presenter.impl.RouteSearchImpl;
import com.graduationproject.invoforultimate.ui.fragment.NavigationSearchFragment;
import com.graduationproject.invoforultimate.ui.view.impl.RouteSearchViewCallback;
import com.graduationproject.invoforultimate.ui.widget.DynamicHeightRecyclerView;
import com.graduationproject.invoforultimate.ui.widget.RouteSearchView;
import com.graduationproject.invoforultimate.utils.Decoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import com.graduationproject.invoforultimate.utils.InputUtil;

import static com.graduationproject.invoforultimate.R2.id.img_back;
import static com.graduationproject.invoforultimate.R2.id.navi_circleChangeBoundsView;
import static com.graduationproject.invoforultimate.R2.id.navi_linearLayout;
import static com.graduationproject.invoforultimate.R2.id.navi_tabLayout;
import static com.graduationproject.invoforultimate.R2.id.navi_viewPager;
import static com.graduationproject.invoforultimate.R2.id.navigation_tips;

public class RouteSearchActivity extends BaseAppCompatActivity<RouteSearchViewCallback, RouteSearchImpl, TextureMapView> implements RouteSearchViewCallback {
    @BindView(navi_tabLayout)
    TabLayout tabLayout;
    @BindView(navi_circleChangeBoundsView)
    RouteSearchView routeSearchView;
    @BindView(navigation_tips)
    DynamicHeightRecyclerView recyclerView;
    @BindView(navi_linearLayout)
    LinearLayout linearLayout;
    @BindView(navi_viewPager)
    ViewPager viewPager;
    @BindView(img_back)
    ImageView imageView;

    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private boolean addTab = false;
    private LatLonPointParcelable latLonPointParcelable = new LatLonPointParcelable();
    private ArrayList<NavigationSearchFragment> arrayList = new ArrayList<>();
    private ArrayList<Bundle> bundleArrayList = new ArrayList<>();
    private EditText editText;
    private NavigationFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void initControls(@Nullable Bundle savedInstanceState) {
        Explode explode = new Explode();
        explode.excludeTarget(android.R.id.statusBarBackground, true);
        explode.setDuration(700L);
        Window window = this.getWindow();
        window.setEnterTransition(explode);
        editText = getP().getEditView("输入位置", R.id.create_route_edit);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new Decoration());
        linearLayout.addView(editText);
        getP().startScene(linearLayout, routeSearchView);
        editText.addTextChangedListener(new OnTextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                getP().setInputTips(s);
            }
        });
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    InputUtil.hideAllInputMethod(RouteSearchActivity.this);
                }
            }
            return false;
        });
    }

    @Override
    public void onGetInputCallback(List<Tip> list, int i) {
        if (1000 == i) {
            recyclerView.setMaxHeight(500);
            recyclerView.setAdapter(new NavigationTipsAdapter(list, var -> {
                bundleArrayList.clear();
                arrayList.clear();
                latLonPointParcelable.setLatLonPoint(var);
                for (int j = 0; j < 2; j++) {
                    bundleArrayList.add(new Bundle());
                    bundleArrayList.get(j).putParcelable("navi", latLonPointParcelable);
                    bundleArrayList.get(j).putInt("style", j);
                }
                for (int i1 = 0; i1 < 2; i1++) {
                    arrayList.add(NavigationSearchFragment.newInstance(bundleArrayList.get(i1)));
                    if (!addTab) {
                        tabLayout.addTab(tabLayout.newTab());
                        addTab = true;
                    }
                }
                fragmentPagerAdapter = new NavigationFragmentPagerAdapter(getSupportFragmentManager(), arrayList);
                viewPager.setAdapter(fragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.getTabAt(0).setText("bike");
                tabLayout.getTabAt(1).setText("walk");
            }));
        } else {
            ToastText("请求失败,请检查网络设置");
        }
    }

    @OnClick(img_back)
    public void onBack() {
        RouteSearchActivity.this.onBackPressed();
    }

    @Override
    protected TextureMapView loadMap(Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected RouteSearchImpl loadP() {
        return new RouteSearchImpl();
    }

    @Override
    protected RouteSearchViewCallback loadV() {
        return this;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_route_search;
    }
}
