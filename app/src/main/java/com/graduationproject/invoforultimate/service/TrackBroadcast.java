package com.graduationproject.invoforultimate.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.graduationproject.invoforultimate.utils.ToastUtil;
import static com.graduationproject.invoforultimate.entity.constants.MainConstants.*;

/**
 * Created by INvo
 * on 2020-02-14.
 */
public class TrackBroadcast extends BroadcastReceiver {

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = NETWORK_BROADCAST_MOBILE;
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = NETWORK_BROADCAST_WIFI;
        }
        return connType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                if (!(NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable())) {
                    ToastUtil.shortToast(context,getConnectionType(info.getType()) +NETWORK_BROADCAST_NOT_CONNECT);
                }
            }
        }
    }
}
