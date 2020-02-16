package com.graduationproject.invoforultimate.model;

import android.os.Message;

/**
 * Created by INvo
 * on 2020-02-07.
 */
@FunctionalInterface
public interface TrackTerminal extends TrackModel{
    void createTrackCallback(Message message);
}
