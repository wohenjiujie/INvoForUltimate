package com.graduationproject.invoforultimate.model;

import androidx.annotation.Nullable;

/**
 * Created by INvo
 * on 2020-02-12.
 */
public interface TrackHistoryModel {
    void onTrackHistoryCallback(@Nullable String var1, @Nullable String var2);

    void onTrackHistoryIntentCallback(String result);
}
