package com.graduationproject.invoforultimate.service;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.MainActivity;
import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.constant.OnCreateTerminalListener;
import com.graduationproject.invoforultimate.initialize.InitializeTerminal;
import com.graduationproject.invoforultimate.util.HttpUtil;
import com.graduationproject.invoforultimate.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by INvo
 * on 2019-10-10.
 */
public class TrackThread extends Thread {
    private Context context;
    private int anInt;
    private InitializeTerminal initializeTerminal = new InitializeTerminal();
    private String string;
    private HttpUtil httpUtil;
//    private boolean result=false;
    private String content;
    private Response response;
    private RequestBody requestBody = null;
    private MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private OnCreateTerminalListener onCreateTerminalListener;
    private Bundle bundle;
//    private Handler handler;
//    private Message message;


    public void setOnCreateTerminalListener(OnCreateTerminalListener listener) {
        this.onCreateTerminalListener = listener;
    }
    public TrackThread(Context context, int CommandType, @Nullable String string,@Nullable Handler handler) {
        this.context = context;
        this.anInt = CommandType;
        this.string = string;
//        this.handler = handler;
    }

    /*public TrackThread(Context context, int CommandType, @Nullable String string) {

        this.anInt = CommandType;
        this.string = string;
    }*/

//    @SuppressLint("HandlerLeak")
   /* Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String temporary = null;
//            Message message=Message.obtain();
            if (msg.what == Constants.CreateTerminalCommand) {
                if (initializeTerminal.setTerminal(context, string)) {
                    ToastUtil.showToast(context, Constants.CreateTerminalSucceed);
                } else {
                    ToastUtil.showToast(context, Constants.CreateTerminalFailure);
                }
            } else if (msg.what == Constants.MsgTerminalExistError) {
                ToastUtil.showLongToast(context, Constants.TerminalExistError);
//                message.what = -1;
//                result = false;
            } else if (msg.what == Constants.MsgTerminalInvalidError) {
                ToastUtil.showLongToast(context, Constants.TerminalInvalidError);
//                message.what = -1;
//                result = false;
            } else if (msg.what == Constants.MsgTerminalSuccess) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                   temporary = jsonObject.getJSONObject("data").getString("tid");
//                    setResult(true);
                    Log.d("mytid", temporary);
//                    result = true;
//                    message.what = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == Constants.MsgTerminalSuccess || msg.what == Constants.MsgTerminalInvalidError || msg.what == Constants.MsgTerminalExistError) {
                boolean result;
                if (msg.what == Constants.MsgTerminalSuccess) {
                    result = true;
                } else {
                    result = false;
                }
                Log.d("myresult", "result:" + result);
                bundle = new Bundle();
                bundle.putBoolean("result", result);
                bundle.putString("tid", temporary);
//                setBundle(bundle);
                ToastUtil.showLongToast(context, "bundle test");
                onCreateTerminalListener.onBundle(bundle);
                Log.d("mybundle", "bundle:" + bundle);
            }

        }
    };*/

/*
    private void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }*/

    @Override
    public void run() {
        if (anInt == Constants.CreateTerminalCommand) {
//            handler.sendEmptyMessageDelayed(Constants.CreateTerminalCommand, 2000);
        } else if (anInt == Constants.CreateTerminalService) {

            new Thread() {
                @Override
                public void run() {
                    content = "key="
                            + Constants.Key + "&sid=" + Constants.ServiceID + "&&name=" + string;
                    requestBody = RequestBody.create(mediaType, content);
                    request = new Request.Builder().url(Constants.CreateTerminalUrl).post(requestBody).build();
                    try {
                        response = okHttpClient.newCall(request).execute();
                        String accept = response.body().string();
                        Log.d("myaccept", accept);
                        JSONObject jsonObject = new JSONObject(accept);
//                        JSONObject jsonObject = new JSONObject(accept);
                        String errMsg = jsonObject.getString("errmsg");
//                        Log.d("TrackThread", errMsg);
                        Message message = Message.obtain();
                        if (!errMsg.equals("OK")) {
                            if (errMsg.equals("EXISTING_ELEMENT")) {
                                message.what = Constants.MsgTerminalExistError;
                            } else if (errMsg.equals("INVALID_PARAMS")) {
                                message.what = Constants.MsgTerminalInvalidError;
                            }
//                            setResult(false);
                        } else {
                            message.what = Constants.MsgTerminalSuccess;
                            message.obj = accept;
//                            setResult(true);
                        }
//                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
