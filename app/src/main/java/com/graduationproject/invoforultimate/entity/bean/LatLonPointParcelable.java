package com.graduationproject.invoforultimate.entity.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.amap.api.services.core.LatLonPoint;


/**
 * Created by INvo
 * on 2020-03-02.
 * 序列化 LatLonPoint
 */
public class LatLonPointParcelable implements Parcelable {

    private LatLonPoint latLonPoint;

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latLonPoint, flags);
    }

    public LatLonPointParcelable() {
        super();
    }

    protected LatLonPointParcelable(Parcel in) {
        this.latLonPoint = in.readParcelable(LatLonPoint.class.getClassLoader());
    }

    public static final Parcelable.Creator<LatLonPointParcelable> CREATOR = new Parcelable.Creator<LatLonPointParcelable>() {
        @Override
        public LatLonPointParcelable createFromParcel(Parcel source) {
            return new LatLonPointParcelable(source);
        }

        @Override
        public LatLonPointParcelable[] newArray(int size) {
            return new LatLonPointParcelable[size];
        }
    };

    @Override
    public String toString() {
        return "LatLonPointParcelable{" +
                "latLonPoint=" + latLonPoint +
                '}';
    }
}
