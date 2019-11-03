package com.graduationproject.invoforultimate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.graduationproject.invoforultimate.constant.Constants;
import com.graduationproject.invoforultimate.util.DatabaseUtil;
import com.graduationproject.invoforultimate.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
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
        unixTime();
    }


    public void unixTime() {
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time);
        System.out.println(time);
        String formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(nowTimeStamp) ;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        System.out.println(date);
    }
}