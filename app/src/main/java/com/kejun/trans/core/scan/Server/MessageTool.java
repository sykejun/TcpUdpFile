package com.kejun.trans.core.scan.Server;

import com.kejun.trans.MyApplication;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;


public class MessageTool {
    private ScannerServer scannerServer;
    private int                                                         uId;
    public MessageTool(ScannerServer scannerServer, int uId) {
        this.scannerServer=scannerServer;
        this.uId=uId;

    }


    public void processMessage(DatagramPacket datagramPacket){
        byte[] data=datagramPacket.getData();
        String result=new String(data,0,datagramPacket.getLength());
        int index=0;
        if ((index=result.indexOf(":"))!=-1){
            if (result.substring(index+1).equals(uId+"")){
                MyApplication.myIpAddress=datagramPacket.getAddress().getHostAddress();
                return;
            }
            result=result.substring(0,index);
        }

        switch (result){
            //
            case ConstValue.HELLO:
                System.out.println("Hello");
                scannerServer.replyHello(new InetSocketAddress(datagramPacket.getAddress(),datagramPacket.getPort()));
                break;
            case ConstValue.HELLO_REPLY:
                System.out.println("hello_reply");
                scannerServer.find(datagramPacket.getAddress().getHostAddress());
                break;
        }
    }
}
