package com.graduationproject.invoforultimate.presenter.impl;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.graduationproject.invoforultimate.presenter.Presenter;
import com.graduationproject.invoforultimate.presenter.RouteSearchPresenter;
import com.graduationproject.invoforultimate.ui.activity.RouteSearchActivity;
import com.graduationproject.invoforultimate.ui.view.impl.RouteSearchViewCallback;
import com.graduationproject.invoforultimate.ui.widget.RouteSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.graduationproject.invoforultimate.app.TrackApplication.getCity;
import static com.graduationproject.invoforultimate.app.TrackApplication.getContext;
import static com.graduationproject.invoforultimate.app.TrackApplication.getLatLonPoint;

/**
 * Created by INvo
 * on 2020-03-03.
 */
public class RouteSearchImpl extends Presenter<RouteSearchViewCallback> implements RouteSearchPresenter {
    private Inputtips inputTips;
    private InputtipsQuery inputtipsQuery;


    public void setInputTips(CharSequence s){
        inputtipsQuery = new InputtipsQuery(s.toString(), getCity());
        inputtipsQuery.setCityLimit(true);
        inputtipsQuery.setLocation(getLatLonPoint());
        inputTips = new Inputtips(getContext(), inputtipsQuery);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                getV().onGetInputCallback(list, i);
            }
        });
        inputTips.requestInputtipsAsyn();
    }

    public EditText getEditView(String hint, @IdRes int id) {
        EditText editText = new EditText(getContext());
        editText.setId(id);
        editText.setHint(hint);
        editText.setTextSize(16.0f);
        editText.setTextColor(Color.BLACK);
        editText.setMaxLines(1);
        editText.setSingleLine();
        editText.setHintTextColor(Color.parseColor("#BEBEBE"));
        editText.setPadding(0, 8, 8, 8);
        editText.setBackground(null);
        return editText;
    }

    public void startScene(LinearLayout linearLayout, RouteSearchView routeSearchView) {
        View view = linearLayout.getChildAt(0);
        view.setTranslationY(-100.0f);
        view.setAlpha(0.0f);
        Observable.timer(300L, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            routeSearchView.startDraw();
            Observable.timer(300L, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong1 -> {
                        ObjectAnimator animationTranslation = ObjectAnimator.ofFloat(view, "translationY", new float[]{-100.0F, 0.0F});
                        ObjectAnimator animationAlpha = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0F, 1.0F});
                        AnimatorSet set = new AnimatorSet();
                        set.setDuration(1000L);
                        set.playTogether(new Animator[]{ animationTranslation,  animationAlpha});
                        set.start();
                    });
        });
    }
}
