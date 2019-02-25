package com.kejun.trans.SQLite;


import com.kejun.trans.core.transmission.Transmission;

import java.util.List;

public interface TransService {
    void insertTrans(Transmission e);  //插入一条传输记录信息

    Transmission getTrans(int id); //获取一个传输记录信息

    List<Transmission> getAllTrans(int type); //获取不同类型的传输记录信息列表

    List<Transmission> getAllTrans();//获取所有传输记录的信息

    //更新
    void updateTransName(int id,String name);//名称
    void updateText(int id,String message); //文本内容
    void updateSavepath(int id,String savePath); //路径
    void updateSendpath(int id,String sendPath); //传输状态
    void updateSource(int id,int sr); //资源

    void deleteTrans(int id);//删除一条传输记录信息


}
