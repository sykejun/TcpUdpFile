package com.kejun.trans.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LanDataBase extends SQLiteOpenHelper {

   //传输信息表：id自动增加
   public static final String CREATE_TRANSMISSION=
            "create table Transmission("+
            "id integer primary key autoincrement," +
            "name text," +           // 传输文件名称
            "type integer," +           // 传输文件类型
            "message text," +           //传输内容：文本（文本内容）或文件（文件名）
            "length integer,"+          //文件大小
            "time integer,"+            //传输时间
            "userId integer,"+          //传输设备的ID
            "sendPath text,"+           //发送路径
            "savePath text,"+           //保存路径
            "status integer,"+          //传输状态：已完成、正在传输
            "sr integer)";           //文件资源


    //设备信息表：id自动增加
    public static final String CREATE_EQUIPMENT =
            "create table Equipment(" +
            "id integer," +
            "name text," +              //设备名称
            "ip text," +                //设备ip
            "type int,"+                //设备类型
            "status int,"+              //设备状态
            "port int)";                //设备端口号

    private static final int VERSION= 1;
    private static final String DATABASE_NAME = "LanDataBase.db";


    public LanDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory , int version) {
        super(context, name, factory, version);

    }

    public LanDataBase(Context context){
        this(context,DATABASE_NAME,null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行建表语句
        db.execSQL(CREATE_TRANSMISSION);
        db.execSQL(CREATE_EQUIPMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
