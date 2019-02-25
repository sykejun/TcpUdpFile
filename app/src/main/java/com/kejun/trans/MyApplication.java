package com.kejun.trans;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kejun.trans.SQLite.EquipOperators;
import com.kejun.trans.model.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MyApplication extends Application {
    private List<String>               scannerAddressList=new ArrayList<>();
    private List<Equipment>            connectEquipments;
    private Equipment                  myEquipmentInfo;
    private List<OnEquipmentLinstener> onEquipmentLinsteners=new ArrayList<>();
    public static String               myIpAddress;
    private SharedPreferences          sharedPreferences;

    public static String BRODCAST_ADDRESS=null;

    private EquipOperators equipOperators;

    @Override
    public void onCreate() {
        super.onCreate();
       connectEquipments=new ArrayList<>();
        myEquipmentInfo=new Equipment();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.contains("myId")){
            sharedPreferences.edit().putInt("myId",(int)(Math.random()*2000000000));
        }
        myEquipmentInfo.setId(sharedPreferences.getInt("myId",(int)(Math.random()*2000000000)));
        myEquipmentInfo.setName(sharedPreferences.getString("setting_user_name",getRandomString(6)));
        equipOperators=new EquipOperators(this.getApplicationContext());

     /* connectEquipments =new ArrayList<>();
      myEquipmentInfo=new Equipment();
      sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
      if (!sharedPreferences.contains("myId")){
          sharedPreferences.edit().putInt("myId",(int)(Math.random()*2000000000));
      }

      myEquipmentInfo.setId(sharedPreferences.getInt("myId", (int) (Math.random()*2000000000)));
      myEquipmentInfo.setName(sharedPreferences.);
*/
    }


    public  String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//含有字符和数字的字符串
        Random random = new Random();//随机类初始化
        StringBuffer sb = new StringBuffer();//StringBuffer类生成，为了拼接字符串

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);// [0,62)

            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public boolean addEquipment(Equipment equipment){
        for (Equipment equip:connectEquipments){
            if (equip.getId()==equipment.getId()){
                return false;
            }
        }
        connectEquipments.add(equipment);
        if (equipOperators.getEquipment(equipment.getId())==null){
            equipOperators.insertEquipment(equipment);
        }else {
            equipOperators.updateEquipmentName(equipment.getId(),equipment.getName());
        }
        for (OnEquipmentLinstener onEquipmentLinstener:onEquipmentLinsteners){
            if (null!=onEquipmentLinstener){
                onEquipmentLinstener.add(equipment);
            }
        }
        return true;
    }

    public Equipment findEquipment(int id){
        for (Equipment equipment:connectEquipments){
            if (equipment.getId()==id){
                return equipment;
            }
        }
        return equipOperators.getEquipment(id);
    }
    public Equipment findEquipment(String address){
        for (Equipment equipment:connectEquipments){
            if (equipment.getAddress().equals(address)){
                return equipment;
            }
        }
        return null;
    }


    public void removeEquipment(Equipment equipment){
        for (Equipment equip:connectEquipments){
            if (equip.getId()==equipment.getId()){
                connectEquipments.remove(equip);
                for (OnEquipmentLinstener onEquipmentLinstener:onEquipmentLinsteners){
                    if (null!=onEquipmentLinstener){
                        onEquipmentLinstener.remove(equip);
                    }
                }
                break;
            }
        }
    }

    public List<Equipment> getConnectEquipments() {
        return connectEquipments;
    }

    public Equipment getMyEquipmentInfo() {
        myEquipmentInfo.setAddress(myIpAddress);
        return myEquipmentInfo;
    }

    public void setMyEquipmentInfo(Equipment myEquipmentInfo) {
        this.myEquipmentInfo = myEquipmentInfo;
    }

    public interface OnEquipmentLinstener{
        void add(Equipment equipment);
        void remove(Equipment equipment);
    }

    public void addEquipmentLinstener(OnEquipmentLinstener onEquipmentLinstener){
        if (null!=onEquipmentLinstener){
            onEquipmentLinsteners.add(onEquipmentLinstener);
        }
    }
}
