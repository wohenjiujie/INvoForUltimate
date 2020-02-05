package com.graduationproject.invoforultimate;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Response response;
    private RequestBody requestBody = null;
    private MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String content;
    private String result;
    private Runnable runnable;
    private Handler handler;
    public static final String url = "https://127.0.0.1/deleteTrackInfo";
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("terminal", 292);
            jsonObject.put("track", 290);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        /*content = "terminal=292&track=290";
        requestBody = RequestBody.create( mediaType, content);
        request = new Request.Builder().url(url).post(requestBody).build();

        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
//        unixTime();
//        getJson();
//        getLength();
//        longToString();

        myTest();


    }

    @Test
    public void myTest() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                postDelayed(runnable, 2000);
            }
        };

        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("hahahah");
                Log.d("ExampleUnitTest", "hahah");
            }
        };
    }

    private void longToString() {
        String a = "1569469623619";
        long c = Long.valueOf(a);
        System.out.println(c);
        final long b = 1569469623619L;
        System.out.println(b);
    }

    /*private void getLength() {
        String a = "";
        if (a.equals("")) {
            System.out.println(a.length());
        }
    }*/


    public void unixTime() {
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time);
        System.out.println(time);
        String formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(nowTimeStamp) ;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        System.out.println(date);
    }

    /*@Test
    public void getJson(){
        content="https://tsapi.amap.com/v1/track/terminal/trsearch?key=b26487968ee70a1647954c49b55828f2&sid="
                +Constants.ServiceID+"&tid=211539155&trid="+"1718"+"&pagesize=999";
        request = new Request.Builder().url(content).get().build();
        try {
            response = okHttpClient.newCall(request).execute();
            result = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                *//*JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String a = jsonObject.getString("counts");
                System.out.println(a);*//*
                JSONObject jsonObject = new JSONObject(result);
                String a = jsonObject.getString("counts");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}