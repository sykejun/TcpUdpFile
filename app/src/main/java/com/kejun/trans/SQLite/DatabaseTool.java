package com.kejun.trans.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseTool {
    private static SQLiteDatabase sqLiteDatabase;
/*
    public static SQLiteDatabase getSqLiteDatabase(Context context) {

        if (null != sqLiteDatabase && sqLiteDatabase.isOpen()) {
            return sqLiteDatabase;
        } else {
            synchronized (DatabaseTool.class) {
                if (null != sqLiteDatabase && sqLiteDatabase.isOpen()) {
                    return sqLiteDatabase;
                }else{
                    sqLiteDatabase=new LanDataBase(context).getWritableDatabase();
                    return sqLiteDatabase;
                }
            }
        }
    }*/

   public static SQLiteDatabase  getSqLiteDatabase(Context context){
       if (sqLiteDatabase!=null&&sqLiteDatabase.isOpen()){
           return sqLiteDatabase;
       }else {
           synchronized (DatabaseTool.class){
               sqLiteDatabase=new LanDataBase(context).getWritableDatabase();
               return sqLiteDatabase;
           }
       }
   }
}
