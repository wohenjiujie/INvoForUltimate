package com.graduationproject.invoforultimate.bean.constants;

/**
 * Created by INvo
 * on 2020-02-08.
 */
public final class TrackServiceConstants {

    public static  String TAG = "trackServiceLog";

    public static  int TIMER_TYPE_UPDATE_DATA = 0x2065;
    public static final int TRACK_RESULT_FAILURE = 0x200;
    public static final int TRACK_RESULT_START = 0x202;
    public static final int TRACK_RESULT_STOP = 0x201;

    public static  int GATHER_TIME = 5;

    public static  long SERVICE_ID = 72958;


    public static  String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";
    public static int UPLOAD_TRACK_INFO = 0x03;  //create terminal code


    public static  String TRACK_RESULT_START_RESULT ="开始记录";
    public static  String TRACK_RESULT_STOP_RESULT ="记录完成";
    public static  String TRACK_RESULT_FAILURE_RESULT_ ="记录失败，请检查网络是否正常";

}
