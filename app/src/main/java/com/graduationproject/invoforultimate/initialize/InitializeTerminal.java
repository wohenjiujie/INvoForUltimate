package com.graduationproject.invoforultimate.initialize;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.service.TrackHandler;
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
public class InitializeTerminal {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private HttpUtil httpUtil;
    private Bundle bundle;
    private String content;
    TrackHandler trackHandler;
    private Response response;
    private RequestBody requestBody = null;
    private MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();

//    private TrackThread trackThread;

    public boolean checkTerminal(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.initializeTerminal, Context.MODE_PRIVATE);
        Long aLong = sharedPreferences.getLong("tid", 0);
        if (aLong == 0) {
            return false;
        } else {
            return true;
        }
    }
    public void createTerminal(final Context context, final String terminalName,TrackHandler trackHandler) {
        Thread thread = new Thread(()->{
            content = "key="
                    + Constants.Key + "&sid=" + Constants.ServiceID + "&&name=" + terminalName;
            requestBody = RequestBody.create(mediaType, content);
            request = new Request.Builder().url(Constants.CreateTerminalUrl).post(requestBody).build();
            try {
                response = okHttpClient.newCall(request).execute();
                String accept = response.body().string();

                Log.d("myaccept", accept);

                JSONObject jsonObject = new JSONObject(accept);
                String errMsg = jsonObject.getString("errmsg");
                Message message = Message.obtain();
                if (!errMsg.equals("OK")) {
                    if (errMsg.equals("EXISTING_ELEMENT")) {
                        message.what = Constants.MsgTerminalExistError;
                    } else if (errMsg.equals("INVALID_PARAMS")) {
                        message.what = Constants.MsgTerminalInvalidError;
                    }
                } else {
                    message.what = Constants.MsgTerminalSuccess;
                    message.obj = accept;
                }
                trackHandler.sendMessageDelayed(message, 2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        thread.start();
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
    }


    public void setTerminal(Context context, String string) {
        sharedPreferences = context.getSharedPreferences(Constants.initializeTerminal, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Long tid = Long.valueOf(string);
        editor.putLong("tid", tid);
        editor.commit();
    }

    public Long getTerminal(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.initializeTerminal, Context.MODE_PRIVATE);
        Long tid = sharedPreferences.getLong("tid", 0);
        return tid;
    }
}
