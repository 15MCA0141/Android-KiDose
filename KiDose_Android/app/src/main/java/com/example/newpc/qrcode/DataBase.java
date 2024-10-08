package com.example.newpc.qrcode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ku$haL on 15-05-2017.
 */
public class DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="immunization.db";
    public static final String TABLE_NAME="mother_table";

   // public static final String col1="phc";
   // public static final String col2="pin";
    public static final String col3="name";
    public static final String col4="aadhar";
    public static final String col5="mobile";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    db.execSQL("create table "+TABLE_NAME+"("+col3+" text,"+col4+" integer primary key,"+col5+" integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int ov, int nv) {
    db.execSQL("drop table if exist "+TABLE_NAME);
    onCreate(db);
    }
    public boolean insert(String name,String aadhar,String mobile){
    SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(col3,name);
        cv.put(col4,aadhar);
        cv.put(col5,mobile);
        long result=db.insert(TABLE_NAME,null,cv);
        if(result==-1)
        {
            return false;
        }
        else
            return true;
    }
    public Cursor retrieve(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select* from "+TABLE_NAME,null);
        return res;
    }
}
