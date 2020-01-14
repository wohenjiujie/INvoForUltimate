package com.graduationproject.invoforultimate.constant;

/**
 * Created by INvo
 * on 2019-09-25.
 */
public class Constants {
    public static final String Key = "b26487968ee70a1647954c49b55828f2";
    public static final long ServiceID = 72958;
    public static final String initializeTerminal = "Terminal";
    public static final String CreateTerminalUrl = "https://tsapi.amap.com/v1/track/terminal/add";
    public static final String AddTrackInfo = "http://xiaomu1079.club/addTrackInfo";
    public static final String deleteTrackInfo = "http://xiaomu1079.club/deleteTrackInfo";
    public static final long TerminalID = 211539155;
    public static final long TID_TEST = 202911601;
    public static final int CreateTerminalCommand = 0x201;
    public static final int CreateTerminalService = 0x202;
    public static final int MsgTerminalExistError = 0x203;
    public static final int MsgTerminalInvalidError = 0x204;
    public static final int MsgTerminalSuccess = 0x205;
    public static final int UploadTrackInfo = 0x206;//上传
    public static final String CreateTerminalSucceed = "创建成功";
    public static final String CreateTerminalFailure = "创建失败";
    public static final String InputEmpty = "请不要输入空值";
    public static final String TerminalExistError = "填入的终端名称已存在";
    public static final String TerminalInvalidError = "输入非法：仅支持中文、英文大小字母、英文下划线\"_\"、英文横线\"-\"和数字\n" +
            "\n" +
            "最长不得超过128字符, 不能以\"_\"开头";

}
