package com.example.iquarium;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.annotation.Target;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "aquarium.db";
    // private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "aquarium_table";
    private static final String FTABLE_NAME = "fish_table";
    private static final String DTABLE_NAME = "decor_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "length";
    private static final String COL4 = "width";
    private static final String COL5 = "height";
    private static final String COL6 = "fqty";
    private static final String COL8 = "pH";
    private static final String COL9 = "temperature";
    private static final String DATABASE_CREATE_AQUARIUM = "CREATE TABLE "
            + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, LENGTH FLOAT, WIDTH FLOAT, HEIGHT FLOAT, FQTY INTEGER, PH FLOAT, TEMPERATURE FLOAT)";


    private static final String FCOL1 = "FID";
    private static final String FCOL2 = "Ftype";
    private static final String FCOL3 = "Fsize";
    private static final String FCOL4 = "pHmin";
    private static final String FCOL5 = "pHmax";
    private static final String FCOL6 = "tempmin";
    private static final String FCOL7 = "tempmax";
    private static final String FCOL8 = "tankmin";
    private static final String FCOL9 = "topd";
    private static final String FCOL10 = "midd";
    private static final String FCOL11 = "botd";
    private static final String FCOL12 = "FaqID";
    private static final String FCOL13 = "Qty";
    private static final String FCOL14 = "btype";

    private static final String DATABASE_CREATE_FISH = "CREATE TABLE "
            + FTABLE_NAME + "(" + FCOL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, FTYPE TEXT, FSIZE FLOAT, PHMIN FLOAT, PHMAX FLOAT, TEMPMIN INTEGER," +
            " TEMPMAX INTEGER, TANKMIN INTEGER, TOPD TEXT, MIDD TEXT, BOTD TEXT, FAQID INTEGER, QTY INTEGER, BTYPE TEXT)";

    private static final String DCOL1 = "DID";
    private static final String DCOL2 = "Dtype";
    private static final String DCOL3 = "DaqID";
    private static final String DATABASE_CREATE_DECORATION = "CREATE TABLE "
            + DTABLE_NAME + "(" + DCOL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, DTYPE TEXT, DAQID INTEGER)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 18);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_AQUARIUM);
        db.execSQL(DATABASE_CREATE_FISH);
        db.execSQL(DATABASE_CREATE_DECORATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FTABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DTABLE_NAME);
        onCreate(db);

    }


    public boolean insertData(String name, String length, String width, String height, String ph, String temp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, length);
        contentValues.put(COL4, width);
        contentValues.put(COL5, height);
        contentValues.put(COL6, 0);
        contentValues.put(COL8, ph);
        contentValues.put(COL9, temp);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getAqID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getAqData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }


    public void deleteAquarium(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        db.execSQL(query);

        query = "DELETE FROM " + FTABLE_NAME + " WHERE "
                + FCOL12 + " = '" + id + "'";
        db.execSQL(query);
    }


    public boolean updateAquarium(int id, String name, String length, String width, String height, String ph, String temp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, length);
        contentValues.put(COL4, width);
        contentValues.put(COL5, height);
        contentValues.put(COL8, ph);
        contentValues.put(COL9, temp);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id + ""});
        if (result == 0) {
            return false;
        } else
            return true;


    }

    // Retrieve all fish data from a specific virtual aquarium
    public Cursor getFishData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + FTABLE_NAME + " WHERE "
                + FCOL12 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        return data;

    }

    public Cursor getAqDecorData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DTABLE_NAME + " WHERE "
                + DCOL3 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        return data;

    }


    public Cursor getOneFishData(int aqid, int fid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + FTABLE_NAME + " WHERE "
                + FCOL12 + " = '" + aqid + "'" + " AND " + FCOL1 + " = '" + fid + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }


    public Cursor showFishData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + FTABLE_NAME + " WHERE "
                + FCOL1 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public void incrementFishQty(int id, int qty) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        int currentQty = 0;
        while (data.moveToNext()) {
            currentQty = data.getInt(5);
        }
        currentQty += qty;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL6, currentQty);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id + ""});

    }

    public int getFishQty(int id) {
        int qty = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + FTABLE_NAME + " WHERE "
                + FCOL1 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        while (data.moveToNext()) {
            qty = data.getInt(12);
        }

        return qty;
    }

    public void decrementFishQty(int id, int fqty) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        int currentQty = 0;
        while (data.moveToNext()) {
            currentQty = data.getInt(5);
        }
        currentQty -= fqty;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL6, currentQty);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id + ""});

    }

    public boolean insertDecorData(String decorType, int daqid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DCOL2, decorType);
        contentValues.put(DCOL3, daqid);
        long result = db.insert(DTABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insertFishData(String ftype, String fsize, int faqID, float phlvlmin, float phlvlmax, int tempmin, int tempmax,
                                  int tankmin, String topd, String midd, String botd, int qty, boolean updatefish, int fid, int aqfqty, String btype) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(FCOL2, ftype);
        contentValues.put(FCOL3, fsize);
        contentValues.put(FCOL4, phlvlmin);
        contentValues.put(FCOL5, phlvlmax);
        contentValues.put(FCOL6, tempmin);
        contentValues.put(FCOL7, tempmax);
        contentValues.put(FCOL8, tankmin);
        contentValues.put(FCOL9, topd);
        contentValues.put(FCOL10, midd);
        contentValues.put(FCOL11, botd);
        contentValues.put(FCOL12, faqID);
        contentValues.put(FCOL13, qty);
        contentValues.put(FCOL14, btype);
        if(updatefish == false) {
            result = db.insert(FTABLE_NAME, null, contentValues);
        }
        else{
            result = db.update(FTABLE_NAME, contentValues, "FID = ?", new String[]{fid + ""});
       }
        if (result == -1) {
            return false;
        } else {
            incrementFishQty(faqID, aqfqty);
            return true;
        }
    }



    public void deleteFishAquarium(int id, int aqID, int fqty) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + FTABLE_NAME + " WHERE "
                + FCOL1 + " = '" + id + "'";
        db.execSQL(query);

        decrementFishQty(aqID, fqty);

    }

    public void deleteDecorAquarium(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + DTABLE_NAME + " WHERE "
                + DCOL1 + " = '" + id + "'";
        db.execSQL(query);

    }

    public void RemoveAllFish(int aqID, int fqty)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + FTABLE_NAME + " WHERE "
                + FCOL12 + " = '" + aqID + "'";
        db.execSQL(query);

        decrementFishQty(aqID, fqty);
    }

}

