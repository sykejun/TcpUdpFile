package com.kejun.trans.dialog;

import android.content.Context;

import com.kejun.trans.core.transmission.ConstValue;
import com.kejun.trans.core.transmission.Transmission;


public class NotificationCome {
    private Transmission transmission;

    public NotificationCome(Transmission transmission) {
        this.transmission = transmission;
    }

    public void sendSimplestNotificationWithAction(Context context) {
        NotificationUtils notificationUtils=new NotificationUtils(context);

        switch (transmission.getType()) {
            case ConstValue.TRANSMISSION_CLIPBOARD:
            case ConstValue.TRANSMISSION_TEXT:
                notificationUtils.sendNotification("收到文本",transmission.getMessage());
                break;
            case ConstValue.TRANSMISSION_FILE:
            case ConstValue.TRANSMISSION_IMAGE:
            case ConstValue.TRANSMISSION_VIDEO:
                notificationUtils.sendNotification("文件传输完成",transmission.getMessage());
                break;
        }

    }
}
