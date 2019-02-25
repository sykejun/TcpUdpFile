package com.kejun.trans.core.transmission.Server;

import android.util.Log;

import com.kejun.trans.MyApplication;
import com.kejun.trans.core.scan.Server.ScannerServer;
import com.kejun.trans.core.transmission.Client.LanClient;
import com.kejun.trans.core.transmission.ConstValue;
import com.kejun.trans.model.Equipment;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class LanServer extends Thread {
    private             List<ClientLinkThread> clientLinkThreads;
    private             ServerSocket           serverSocket;
    private             int                    port;
    private             boolean                flag = true;
    public static final int                    DEFAULT_PORT = ConstValue.TCP_PORT;
    private             MyApplication          myApplication;
    private             ScannerServer                                                                scannerServer;

    public LanServer(MyApplication myApplication) {
        this(DEFAULT_PORT, myApplication);
    }

    public LanServer(int port, MyApplication myApplication) {
        super();
        this.port = port;
        clientLinkThreads = new ArrayList<>();
        this.myApplication = myApplication;
    }


    @Override
    public void run() {
        super.run();
        try {
            serverSocket = new ServerSocket(port);
            while (flag) {
                Socket socket = serverSocket.accept();
                ClientLinkThread clientLinkThread = new ClientLinkThread(socket, myApplication);
                clientLinkThread.start();
                clientLinkThreads.add(clientLinkThread);
                Log.e("clientLinkThreads","="+clientLinkThreads.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startLan() {
        this.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //如果重复跑这个代码会出现端口一绑定过的异常
                    scannerServer = new ScannerServer(myApplication.getMyEquipmentInfo().getId());
                    scannerServer.setOnScannerFindListener(new ScannerServer.OnScannerFindListener() {
                        @Override
                        public void onFind(String address) {
                            if (myApplication.findEquipment(address) == null) {
                                LanClient lanClient = new LanClient(address);
                                lanClient.setOnLanCilentListener(new LanClient.OnLanCilentListener() {
                                    @Override
                                    public void getHelloReply(Equipment equipment) {
                                        myApplication.addEquipment(equipment);
                                    }

                                    @Override
                                    public void failToLink() {

                                    }
                                });
                                //进行设备的双向绑定
                                lanClient.sendHello();
                            }
                        }
                    });
                    scannerServer.start();
                    while (flag) {
                        scannerServer.scan();
                        Thread.sleep(4000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
     // 暂停当前线程的连接
    public void stopLan() {
        for (ClientLinkThread clientLinkThread : clientLinkThreads) {
            clientLinkThread.interrupt();

        }
        this.interrupt();
    }


}
