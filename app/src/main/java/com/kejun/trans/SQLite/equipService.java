package com.kejun.trans.SQLite;


import com.kejun.trans.model.Equipment;

import java.util.List;

public interface equipService {

    void insertEquipment(Equipment e);  //插入一条设备信息

    Equipment getEquipment(int id); //获取一个设备信息

    List<Equipment> getAllEquipment();//获取所有设备的信息

    void updateEquipmentName(int id,String name);//更新设备名称

    void updateEquipmentStatus(int id,int status);//更新设备状态


    void deleteEuipment(int id);//删除一条设备信息


}
