package com.graduationproject.invoforultimate.model.impl;

import android.util.Log;

import com.graduationproject.invoforultimate.bean.TerminalInfo;
import com.graduationproject.invoforultimate.model.TerminalModel;
import com.graduationproject.invoforultimate.service.TrackThread;

import org.json.JSONException;
import org.json.JSONObject;

import static com.graduationproject.invoforultimate.bean.constants.TerminalModuleConstants.*;

/**
 * Created by INvo
 * on 2020-02-07.
 */
public class TerminalModelImpl {

    private TrackThread trackThread;
    private TerminalModel terminalModel;

    public TerminalModelImpl(TerminalModel terminalModel) {
        this.terminalModel = terminalModel;
    }

    public void createTerminal(String s) {
        trackThread = new TrackThread(CREATE_TERMINAL, s, message -> {
            String tid = null;
            if (MSG_TERMINAL_SUCCESS == message.what) {
                try {
                    JSONObject jsonObject = new JSONObject(message.obj.toString());
                    tid = jsonObject.getJSONObject("data").getString("tid");
                    Log.d("my", tid);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    TerminalInfo.setTerminal(tid, s);
                    new TrackThread(CREATE_TRACK_COUNT, tid, null).start();
                    terminalModel.createTerminalCallback(RESULT_TERMINAL_MSG_SUCCESS);
                }
            }
            if (MSG_TERMINAL_INVALID_ERROR == message.what) {
                terminalModel.createTerminalCallback(RESULT_TERMINAL_MSG_INVALID_PARAMS);
            }
            if (MSG_TERMINAL_EXIST_ERROR == message.what) {
                terminalModel.createTerminalCallback(RESULT_TERMINAL_MSG_EXISTING_ELEMENT);
            }
        });
        trackThread.start();
        try {
            trackThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void checkTerminal() {
        Long l = TerminalInfo.getTerminal();
        if (l == 0) {
            terminalModel.checkTerminalCallback(false);
        } else {
            terminalModel.checkTerminalCallback(true);
        }
    }
}
