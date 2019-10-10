package com.graduationproject.invoforultimate.util;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by INvo
 * on 2019-10-07.
 */
public class HttpUtil {
    //提供一个静态方法，当别的地方需要发起网络请求时，简单的调用这个方法即可
    //请求实例
    //OKHttp请求
    //callback是okhttp自带的回调接口，这里写的是使用GET方式获取服务器数据
    public static void sendOkHttpRequest(final String address, final Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        //enqueue方法内部已经帮助我们开启好了线程，最终的结果会回调到callback中
        client.newCall(request).enqueue(callback);
    }

}
