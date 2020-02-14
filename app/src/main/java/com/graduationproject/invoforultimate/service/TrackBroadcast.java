package com.graduationproject.invoforultimate.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import com.graduationproject.invoforultimate.utils.ToastUtil;
import static com.graduationproject.invoforultimate.bean.constants.MainConstants.*;

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
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        ToastUtil.shortToast(context,getConnectionType(info.getType()) +NETWORK_BROADCAST_CONNECT);
                    }
                } else {
                    ToastUtil.shortToast(context,getConnectionType(info.getType()) +NETWORK_BROADCAST_NOT_CONNECT);
                }
            }
        }
    }
}
