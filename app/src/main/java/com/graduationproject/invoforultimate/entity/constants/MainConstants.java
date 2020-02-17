package com.graduationproject.invoforultimate.entity.constants;

/**
 * Created by INvo
 * on 2020-02-06.
 */
public final class MainConstants {




    public static String TAG = "mainLog";
    public static String TRACK_UPLOAD = "正在上传数据";
    public static String TRACK_START = "开始";
    public static String TRACK_STOP = "结束";

    public static String NETWORK_BROADCAST_WIFI = "Wifi";
    public static String NETWORK_BROADCAST_MOBILE = "蜂窝数据";
//    public static String NETWORK_BROADCAST_CONNECT = "已连接";
    public static String NETWORK_BROADCAST_NOT_CONNECT = "已断开";

    public static String BOTTOM_NAVIGATION_TITLE = "开始运动";
    public static String BOTTOM_NAVIGATION_MESSAGE = "是否开始运动记录";

//    public static String BOTTOM_NAVIGATION_POSITIVE_CHOICE = "是";
//    public static String BOTTOM_NAVIGATION_NEGATIVE_CHOICE = "否";

    public static String DIALOG_CHECK_TITLE = "本次服务将不会上传";
    public static String DIALOG_CHECK_MESSAGE = "因为距离或时间太短等其他因素，本次记录的轨迹将不会上传到服务器上";

    public static int DIALOG_START_TRACK = 0x601;
    public static int DIALOG_TRACK_NOT_UPLOAD = 0x602;
    public static int DIALOG_CREATE_TERMINAL = 0x603;
    public static int DIALOG_EXIT_APP = 0x604;
    public static int DIALOG_STOP_TRACK = 0x605;
    public static String DIALOG_EXIT_TRACK = "是否要终止记录进程";


    public static String DIALOG_EXIT_TITLE = "是否退出软件";
    public static String DIALOG_POSITIVE_CHOICE = "是";
    public static String DIALOG_NEGATIVE_CHOICE = "否";
    public static String ALTER_DIALOG_TITLE = "该设备没有注册终端";
    public static String ALTER_DIALOG_MESSAGE = "请创建你的终端ID以作为标识";
    public static String ALTER_DIALOG_POSITIVE = "好";
    public static String INPUT_EMPTY = "输入为空!";

    public static String CHECK_BOX_CAMERA = "镜头跟随";
    public static String CHECK_BOX_MARKER= "定位跟随";



    public static int CAMERA_FOLLOW_INIT = 0x701;
    public static int CAMERA_FOLLOW_START = 0x702;
    public static int CAMERA_FOLLOW_STOP = 0x703;


}