package com.graduationproject.invoforultimate.presenter;

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

    public V getV() {
        return this.v;
    }

    public V attachV(V v) {
        return this.v = v;
    }

    public void detachV() {
        if (null != this.v) {
            this.v = null;
        }
    }
}
