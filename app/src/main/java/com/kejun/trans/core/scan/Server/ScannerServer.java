package com.kejun.trans.core.scan.Server;

import com.kejun.trans.MyApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;




public class ScannerServer extends Thread {
    private static final int SCANNER_SERVER_PORT = 2333;
    private static final int PACKET_LENGTH = 1024;

    private DatagramSocket datagramSocket;

    private OnScannerFindListener onScannerFindListener;
    private MessageTool messageTool;
    private int uId;
    public ScannerServer(int uId) throws SocketException {
        super();
        this.uId=uId;
        datagramSocket=new DatagramSocket(SCANNER_SERVER_PORT);
        messageTool=new MessageTool(this,uId);
    }

    @Override
    public void run() {
        super.run();
        byte[] buffer=new byte[PACKET_LENGTH];
        while (true){
            DatagramPacket packet=new DatagramPacket(buffer,0,buffer.length);
            try {
                datagramSocket.receive(packet);
                messageTool.processMessage(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 重复扫描所在局域网里面的设备
     */
    public void scan(){
        //表示我还在继续扫描当前的局域网
       System.out.println("scan");
       //打印当前的WiFi的网络地址
       System.out.println(MyApplication.BRODCAST_ADDRESS);
        InetSocketAddress inetAddress=new InetSocketAddress(MyApplication.BRODCAST_ADDRESS,SCANNER_SERVER_PORT);
        send(inetAddress,ConstValue.HELLO+":"+uId);
      /*  System.out.println("scan");
        System.out.println(MyApplication.BRODCAST_ADDRESS);
        InetSocketAddress address=new InetSocketAddress(MyApplication.BRODCAST_ADDRESS,SCANNER_SERVER_PORT);
        send(address, ConstValue.HELLO+"；"+uId);*/
    }

    public void replyHello(InetSocketAddress inetSocketAddress){
        send(inetSocketAddress,ConstValue.HELLO_REPLY);
    }


    /**
     * 将局域网中地址和信息发到客户端
     * @param address
     * @param message
     */
    private void send(InetSocketAddress address,String message){
        byte[] buffer=message.getBytes();
        try {
            datagramSocket.send(new DatagramPacket(buffer,buffer.length,address));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 扫秒结果的回调监听
     * @param onScannerFindListener
     */
    public void setOnScannerFindListener(OnScannerFindListener onScannerFindListener) {
        this.onScannerFindListener = onScannerFindListener;
    }

    /**
     *
     * 找到新新地址的回调
     */
    public interface OnScannerFindListener{
        void onFind(String address);
    }

    public void find(String address){
        System.out.println(address);
        if (null!=onScannerFindListener){
            onScannerFindListener.onFind(address);
        }
    }
}
