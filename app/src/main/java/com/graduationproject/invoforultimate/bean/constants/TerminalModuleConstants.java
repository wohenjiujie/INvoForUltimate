package com.graduationproject.invoforultimate.bean.constants;

/**
 * Created by INvo
 * on 2020-02-07.
 */
public final class TerminalModuleConstants {
    public static String TAG = "terminalModuleLog";


    public static String INITIALIZE_TERMINAL = "Terminal";  //sp写入文件名
    public static int CREATE_TERMINAL = 0x01;  //create terminal code

    public static String CREATE_TERMINAL_MSG_EXISTING_ELEMENT = "EXISTING_ELEMENT";

    public static String CREATE_TERMINAL_MSG_OK = "OK";

    public static String CREATE_TERMINAL_MSG = "errmsg";
    public static String CREATE_TERMINAL_MSG_INVALID_PARAMS = "INVALID_PARAMS";

    public static int MSG_TERMINAL_EXIST_ERROR = 0x203;
    public static int MSG_TERMINAL_INVALID_ERROR = 0x204;
    public static int MSG_TERMINAL_SUCCESS = 0x205;

    public static String RESULT_TERMINAL_MSG_EXISTING_ELEMENT = "填入的终端名称已存在";
    public static String RESULT_TERMINAL_MSG_INVALID_PARAMS = "输入非法：仅支持中文、英文大小字母、英文下划线\"_\"、英文横线\"-\"和数字\n" +
            "\n" +
            "最长不得超过128字符, 不能以\"_\"开头";

    public static String RESULT_TERMINAL_MSG_SUCCESS = "创建终端成功";

    public static int CREATE_TRACK_COUNT = 0x02;  //create terminal code

}
