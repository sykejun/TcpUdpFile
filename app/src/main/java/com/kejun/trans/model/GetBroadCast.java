package cn.silen_dev.lantransmission.model;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 局域网的网络管理类
 */

public class GetBroadCast {
     private  WifiManager wifiManager;
     private  WifiInfo wifiInfo;
     private  DhcpInfo dhcpInfo;

    public GetBroadCast(Context context) {
        wifiManager=((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
        wifiInfo = wifiManager.getConnectionInfo();
        dhcpInfo = wifiManager.getDhcpInfo();

    }

    /**
     * 获取广播的地址
     * @return
     */
    public String getBroadcastAddress(){
     if (getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED){
            String ip= getWifiApIpAddress();
           return ip.substring(0,ip.lastIndexOf(".")+1)+"255";
        }else{
            String ip=intToIp(dhcpInfo.ipAddress);
            String mask=intToIp(dhcpInfo.netmask);
            return getBroadcastAddress(ip,mask);
        }
    }

    /**
     * 定义WiFi状态的枚举类
     */

    public enum WIFI_AP_STATE {
        WIFI_AP_STATE_DISABLING, WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING,  WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED
    }

    /**
     * 使用反射的方法拿到网络的连接状态
     * @return
     */
    private WIFI_AP_STATE getWifiApState(){
        int tmp;
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            tmp = ((Integer) method.invoke(wifiManager));
            // Fix for Android 4
            if (tmp > 10) {
                tmp = tmp - 10;
            }
            return WIFI_AP_STATE.class.getEnumConstants()[tmp];
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
        }
    }

    /**
     * 将位运算转换成数字
     * @param paramInt
     * @return
     */
    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }


    /**
     * 从网络接口中取到自己需要的网络的iP地址
     * @return
     */
    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d("cdcd", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("", ex.toString());
        }
        return null;
    }

    /**
     * 完了地址的转换
     * @param ip
     * @param netmask
     * @return
     */
    public String getBroadcastAddress(String ip, String netmask) {
        String[] ips = ip.split("\\.");
        String[] masks = netmask.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ips.length; i++) {
            ips[i] = String.valueOf((~Integer.parseInt(masks[i])) | (Integer.parseInt(ips[i])));
            sb.append(turnToStr(Integer.parseInt(ips[i])));
            if (i != (ips.length - 1))
                sb.append(".");
        }
        return turnToIp(sb.toString());
    }


    /**
     * 把带符号整形转换为二进制
     *
     * @param num
     * @return
     */
    private String turnToStr(int num) {
        String str = "";
        str = Integer.toBinaryString(num);
        int len = 8 - str.length();
        // 如果二进制数据少于8位,在前面补零.
        for (int i = 0; i < len; i++) {
            str = "0" + str;
        }
        //如果num为负数，转为二进制的结果有32位，如1111 1111 1111 1111 1111 1111 1101 1110
        //则只取最后的8位.
        if (len < 0)
            str = str.substring(24, 32);
        return str;
    }

    /**
     * 把二进制形式的ip，转换为十进制形式的ip
     *
     * @param str
     * @return
     */
    private String turnToIp(String str) {
        String[] ips = str.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ips.length; i++) {
            sb.append(turnToInt(ips[i]));
            sb.append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 把二进制转换为十进制
     *
     * @param str
     * @return
     */
    private int turnToInt(String str) {
        int total = 0;
        int top = str.length();
        for (int i = 0; i < str.length(); i++) {
            String h = String.valueOf(str.charAt(i));
            top--;
            total += ((int) Math.pow(2, top)) * (Integer.parseInt(h));
        }
        return total;
    }
}

