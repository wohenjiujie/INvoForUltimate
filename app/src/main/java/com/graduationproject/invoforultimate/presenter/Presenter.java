package com.graduationproject.invoforultimate.presenter;

import androidx.annotation.CallSuper;

import com.graduationproject.invoforultimate.ui.view.ViewCallback;

/**
 * Created by INvo
 * on 2020-02-06.
 */
public abstract class Presenter<V extends ViewCallback> {

    private V v;
    public Presenter() {
        super();
    }

    @CallSuper
    public V getV() {
        return this.v;
    }

    @CallSuper
    public V attachV(V v) {
        return this.v = v;
    }

    @CallSuper
    public void detachV() {
        if (null != this.v) {
            this.v = null;
        }
    }
}
