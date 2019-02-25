package com.kejun.trans.core.transmission;

public class ConstValue {

    public static final int TCP_PORT = 2334;
    public static final String HELLO = "hello";
    public static final String REPLY_HELLO = "reply_hello";
    public static final String EXIT = "exit";
    public static final String TRANSMISSION = "LanTransmission";
    public static final String GET_FILE = "get";
    public static final String APP = "app";

    public static final int TRANSMISSION_FILE = 0;//文件
    public static final int TRANSMISSION_IMAGE = 1;//图片
    public static final int TRANSMISSION_VIDEO = 2;//视频
    public static final int TRANSMISSION_TEXT = 3;//文本
    public static final int TRANSMISSION_DEVICE = 4;//设备
    public static final int TRANSMISSION_CLIPBOARD = 5;//剪切板

    public static final int DEVICE_PHONE = 0;
    public static final int DEVICE_COMPUTER = 1;

    public static final int STATUS_DONE = 0;//以完成
    public static final int STATUS_ING = 1;//进行中
    public static final int STATUS_NONE = 2;//已取消

    public static final int SEND = 0;//发送
    public static final int RECEIVE = 1;//接受放

}