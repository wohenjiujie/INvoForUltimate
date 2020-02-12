package com.graduationproject.invoforultimate.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public class TrackIntentParcelable implements Parcelable {
    private int trackID;
    private long startUnix;
    private long endUnix;

    public int getTrackID() {
        return trackID;
    }

    public long getStartUnix() {
        return startUnix;
    }

    public long getEndUnix() {
        return endUnix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.trackID);
        dest.writeLong(this.startUnix);
        dest.writeLong(this.endUnix);
    }

    public TrackIntentParcelable(int var1, long var2, long var3) {
        this.trackID = var1;
        this.startUnix = var2;
        this.endUnix = var3;
    }

    protected TrackIntentParcelable(Parcel in) {
        this.trackID = in.readInt();
        this.startUnix = in.readLong();
        this.endUnix = in.readLong();
    }

    public static final Parcelable.Creator<TrackIntentParcelable> CREATOR = new Parcelable.Creator<TrackIntentParcelable>() {
        @Override
        public TrackIntentParcelable createFromParcel(Parcel source) {
            return new TrackIntentParcelable(source);
        }

        @Override
        public TrackIntentParcelable[] newArray(int size) {
            return new TrackIntentParcelable[size];
        }
    };

    @Override
    public String toString() {
        return "TrackIntentParcelable{" +
                "trackID=" + trackID +
                ", startUnix=" + startUnix +
                ", endUnix=" + endUnix +
                '}';
    }
}
