package com.kejun.trans.core.transmission.Client;
import android.util.Log;
import com.google.gson.Gson;
import com.kejun.trans.core.transmission.ConstValue;
import com.kejun.trans.core.transmission.Server.LanServer;
import com.kejun.trans.core.transmission.TcpMessage.TcpMessage;
import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.model.Equipment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class LanClient extends Thread {
    private String              address;
    private Socket              socket;
    private InputStream         inputStream;
    private OutputStream        outputStream;
    private PrintWriter         printWriter;
    private BufferedReader      reader;
    private OnLanCilentListener onLanCilentListener;
    private TcpMessage          tcpMessage;
    public LanClient(String address) {
        this.address = address;

    }
    private void link(){
        try {

            socket=new Socket(address, LanServer.DEFAULT_PORT);//建立socket连接
            inputStream=socket.getInputStream();//获得输入流
            reader=new BufferedReader(new InputStreamReader(inputStream));//转换成打印流
            outputStream=socket.getOutputStream();//获得输出流
            printWriter=new PrintWriter(outputStream);//打印流
            printWriter.println(ConstValue.APP);//向写入流写标记
            printWriter.flush();//将多余的流刷出来
        } catch (IOException e) {
            if (onLanCilentListener!=null){
                onLanCilentListener.failToLink();//连接失败的时候的回调监听
            }
            e.printStackTrace();
        }
    }

    /**
     * 原计划用数据库一边读一边写来完成文件的进行进度和网速的显示的，但是一边读一边写的话，数据库不支持
     *暂时无法完成进度和网速的显示功能 每次的发送的一个任务的时候都是独立socket和进程完成的所以会导致无法
     * 拿到搜友的任务和进程的数据
     *
     */

    @Override
    public void run() {
        //进行文件传输前先将socket连接起来进行配置
        link();
        printWriter.println(new Gson().toJson(tcpMessage));//将要传输文件的信息写到输出流中
        printWriter.flush();//将多余的
        switch (tcpMessage.getTransmission().getType()){//更具不同的类型来判断使用什么样传输方式
            case ConstValue.TRANSMISSION_FILE:
            case ConstValue.TRANSMISSION_IMAGE:
            case ConstValue.TRANSMISSION_VIDEO:
                File file=new File(tcpMessage.getTransmission().getSendPath());//文件传输的路径
                Log.e("发送getLength====",tcpMessage.getTransmission().getLength()+"");//测试文件的大小
                try {
                    FileInputStream fileInputStream=new FileInputStream(file);//文件的读写操作
                    byte[] buffer = new byte[8092];
                    int len;
                    long total=0;
                    while ((len = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                        total+=len;
                    }
                    outputStream.flush();//将多余的字节刷出来
                    Log.e("发送total====",total+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ConstValue.TRANSMISSION_TEXT:
            case ConstValue.TRANSMISSION_CLIPBOARD:
                break;
        }
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage()+"====");
        }
    }

    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }
    public void sendTransmisstion(Transmission transmission, Equipment equipment){
        tcpMessage=new TcpMessage();
        tcpMessage.setToken(ConstValue.TRANSMISSION);
        tcpMessage.setTransmission(transmission);
        tcpMessage.setEquipment(equipment);
        start();
    }

    public void sendHello(){
        System.out.println("LanClient:sendHello");
        link();

        TcpMessage tcpMessage=new TcpMessage();
        tcpMessage.setToken(ConstValue.HELLO);
        printWriter.println(new Gson().toJson(tcpMessage));
        printWriter.flush();
        try {
            String result=reader.readLine();
            System.out.println("Lan:"+result);
            TcpMessage tcpMessage1=new Gson().fromJson(result,TcpMessage.class);
            Equipment equipment = tcpMessage1.getEquipment();
            equipment.setAddress(socket.getInetAddress().getHostAddress());
            if (null!=onLanCilentListener){
                onLanCilentListener.getHelloReply(equipment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                close();//第一次握手的时候调用关闭的方法
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setOnLanCilentListener(OnLanCilentListener onLanCilentListener) {
        this.onLanCilentListener = onLanCilentListener;
    }

    public interface OnLanCilentListener{
        void getHelloReply(Equipment equipment);
        void failToLink();
    }

}
