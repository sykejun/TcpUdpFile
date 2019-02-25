package com.kejun.trans.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kejun.trans.model.Equipment;

import java.util.ArrayList;
import java.util.List;


/**
 * 用来操作数据库的管理类
 */
public class EquipOperators implements equipService {
    //建立数据库
   private Context         context;
   private SQLiteDatabase  db;
   private List<Equipment> equipList = new ArrayList<>();
   private Equipment       e;

    public EquipOperators(Context context) {
        super();
        this.context = context;
        db = DatabaseTool.getSqLiteDatabase(context);
    }

    @Override
    /*插入一条设备信息*/
    public void insertEquipment(Equipment e) {
        String insert = "insert into Equipment(id,name,ip,type,status,port) values("+e.getId()+",'" + e.getName() + "','" +
                e.getAddress() + "'," + e.getType() + "," + e.getStatus() + "," +
                e.getPort() + ")";
        db.execSQL(insert);//返回值为void

    }

    public long insertEquipWithReturnId(Equipment t){
        ContentValues contentValue=new ContentValues();
        contentValue.put("name",t.getName());
        contentValue.put("ip",t.getAddress());
        contentValue.put("type",t.getType());
        contentValue.put("status",t.getStatus());
        contentValue.put("port",t.getPort());
        return db.insert("Transmission", null, contentValue);
    }


    @Override
    /*查询设备信息*/
    public Equipment getEquipment(int id) {
        String select = "select * from Equipment where id = ?";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        if (cursor.isAfterLast()){
            return null;
        }
        return get(cursor);
    }

    @Override
    /*获取所有设备信息*/
    public List<Equipment> getAllEquipment() {
        String select = "select * from Equipment";
        Cursor cursor = db.rawQuery(select, null);
        equipList.clear();
        if (cursor.moveToFirst()) {
            do {
                equipList.add(get(cursor));
            } while (cursor.moveToNext());
        }
        return equipList;
    }

    @Override
    /*更细设备名称*/
    public void updateEquipmentName(int id, String name) {
        String update = "update Equipment set name='" + name + "' where id=?";
        db.execSQL(update, new String[]{String.valueOf(id)});
    }

    @Override
    /*更新设备状态*/
    public void updateEquipmentStatus(int id, int status) {
        String update = "update Equipment set status=" + status + " where id=?";
        db.execSQL(update, new String[]{String.valueOf(id)});
    }

    @Override
    /*删除一条设备记录*/
    public void deleteEuipment(int id) {
        String delete = "delete from Equipment where id = ?";
        db.execSQL(delete, new String[]{String.valueOf(id)});
    }

    public Equipment get(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String ip = cursor.getString(cursor.getColumnIndex("ip"));
        int type = cursor.getInt(cursor.getColumnIndex("type"));
        int status = cursor.getInt(cursor.getColumnIndex("status"));
        int port = cursor.getInt(cursor.getColumnIndex("port"));
        e = new Equipment(name, ip, type, status, port);
        return e;
    }
}
