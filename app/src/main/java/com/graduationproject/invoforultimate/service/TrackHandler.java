package com.graduationproject.invoforultimate.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.graduationproject.invoforultimate.connector.HandlerResult;

/**
 * Created by INvo
 * on 2019-10-11.
 */
public class TrackHandler extends Handler {
    private Message message;
    private HandlerResult handlerResult;
    public TrackHandler( Message message,HandlerResult handlerResult) {
        super();
        this.handlerResult = handlerResult;
        this.message = message;
        handleMessage(message);
    }

    public TrackHandler() {
        super();
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
    }
}
