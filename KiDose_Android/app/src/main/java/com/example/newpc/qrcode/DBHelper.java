package com.example.newpc.qrcode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 8/4/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "health_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tbl_health(health_id integer primary key autoincrement,qrcode varchar,mothername varchar,fathername varchar,mobileno varchar,adharid varchar,childname varchar,dob date,gender varchar,hepb date,opvzero date,bcg date,pentaone date,opvone date,ipvone date,pentatwo date,opvtwo date,pentathree date,opvthree date,ipvtwo date,measlesone date,measlestwo date,opvbooster date,dptboosterone date,dptboostertwo date,next date)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists tbl_health");
        onCreate(db);
    }

    public void insertdata(String qrcode, String mothername, String fathername, String mobileno, String adharid, String childname, String dob, String gender, String hepb, String opvzero, String bcg, String pentaone, String opvone, String ipvone, String pentatwo, String opvtwo, String pentathree, String opvthree, String ipvtwo, String measlesone, String measlestwo, String opvbooster, String dptboosterone, String dptboostertwo, String next) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("qrcode", qrcode);
        cv.put("mothername", mothername);
        cv.put("fathername", fathername);
        cv.put("mobileno", mobileno);
        cv.put("adharid", adharid);
        cv.put("childname", childname);
        cv.put("dob", dob);
        cv.put("gender", gender);
        cv.put("hepb", hepb);
        cv.put("opvzero", opvzero);
        cv.put("bcg", bcg);
        cv.put("pentaone", pentaone);
        cv.put("opvone", opvone);
        cv.put("ipvone", ipvone);
        cv.put("pentatwo", pentatwo);
        cv.put("opvtwo", opvtwo);
        cv.put("pentathree", pentathree);
        cv.put("opvthree", opvthree);
        cv.put("ipvtwo", ipvtwo);
        cv.put("measlesone", measlesone);
        cv.put("measlestwo", measlestwo);
        cv.put("opvbooster", opvbooster);
        cv.put("dptboosterone", dptboosterone);
        cv.put("dptboostertwo", dptboostertwo);
        cv.put("next", next);

        db.insert("tbl_health", null, cv);
    }

    public void updatedata(String qrcode, String mothername, String fathername, String mobileno, String adharid, String childname, String dob, String gender, String hepb, String opvzero, String bcg, String pentaone, String opvone, String ipvone, String pentatwo, String opvtwo, String pentathree, String opvthree, String ipvtwo, String measlesone, String measlestwo, String opvbooster, String dptboosterone, String dptboostertwo, String next) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("qrcode", qrcode);
        cv.put("mothername", mothername);
        cv.put("fathername", fathername);
        cv.put("mobileno", mobileno);
        cv.put("adharid", adharid);
        cv.put("childname", childname);
        cv.put("dob", dob);
        cv.put("gender", gender);
        cv.put("hepb", hepb);
        cv.put("opvzero", opvzero);
        cv.put("bcg", bcg);
        cv.put("pentaone", pentaone);
        cv.put("opvone", opvone);
        cv.put("ipvone", ipvone);
        cv.put("pentatwo", pentatwo);
        cv.put("opvtwo", opvtwo);
        cv.put("pentathree", pentathree);
        cv.put("opvthree", opvthree);
        cv.put("ipvtwo", ipvtwo);
        cv.put("measlesone", measlesone);
        cv.put("measlestwo", measlestwo);
        cv.put("opvbooster", opvbooster);
        cv.put("dptboosterone", dptboosterone);
        cv.put("dptboostertwo", dptboostertwo);
        cv.put("next", next);

        db.update("tbl_health",cv, null, null);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tbl_fruits", null);
        return res;
    }

    public Cursor getAllData(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tbl_fruits where fruit_id=" + id, null);
        return res;

    }
    public Cursor getProfile(){
        SQLiteDatabase db=getReadableDatabase();
        return db.rawQuery("select * from tbl_health",null);

    }

    //    public Integer deleteContact (Integer id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("tbl_fruits", "id = ? ", new String[] { Integer.toString(id) });
//    }
    public long deleteFruit(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("tbl_fruits", "fruit_id=?", new String[]{id});
    }

}