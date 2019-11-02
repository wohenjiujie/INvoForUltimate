package com.graduationproject.invoforultimate.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.constant.OnCreateTerminalListener;
import com.graduationproject.invoforultimate.service.TrackThread;
/*import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;*/


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.*;

/**
 * Created by INvo
 * on 2019-10-07.
 */
public class HttpUtil {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Response response;
    private Request request;
    private String content;
    private String string;
    private MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    private TrackThread trackThread;
    private RequestBody requestBody = null;
    private Context context;
    Lock lock;
    ReentrantLock reentrantLock;


    public HttpUtil() {
        super();
    }

    public HttpUtil(Context context, String string) {
        this.context = context;
        this.string = string;
    }

    //提供一个静态方法，当别的地方需要发起网络请求时，简单的调用这个方法即可
    //请求实例
    //OKHttp请求
    //callback是OkHttp自带的回调接口，这里写的是使用GET方式获取服务器数据


    public void startService(int type,Handler handler)  {
//        lock.lock();
        if (type == Constants.CreateTerminalCommand) {
//            reentrantLock = new ReentrantLock();
//            reentrantLock.lock();

            trackThread = new TrackThread(context, Constants.CreateTerminalService, string, handler);

//                trackThread.join();
                trackThread.run();

//            setTrackThread(trackThread);
//            Bundle bundle = trackThread.getBundle();

//            reentrantLock.unlock();
//            trackThread.stop();
//            Log.d("mygetservicebundle", "bundle:" + bundle);
//            Log.d("mygetservicethread", "trackThread:" + trackThread);
//            boolean aaa = trackThread.getResult();
//            Log.d("myresult", "aaa:" + aaa);
//            return aaa;
        }
    }

    private void setTrackThread(TrackThread trackThread) {
        this.trackThread = trackThread;
    }

    public TrackThread getTrackThread() {
        return trackThread;
    }


}
