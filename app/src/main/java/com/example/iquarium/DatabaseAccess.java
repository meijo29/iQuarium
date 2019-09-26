package com.example.iquarium;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    // private constructor so that ojjbect creation from outside the class is avoided
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    // to return the single instance of database
    public static DatabaseAccess getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    // to open the database
    public void open()
    {
        this.db = openHelper.getReadableDatabase();
    }

    public void close()
    {
        if(db != null)
        {
            this.db.close();
        }
    }

    // create a method to query and return the result from database
    // query for address by passing name
    public Cursor getFishData()
    {
        Cursor data;
        String query = "SELECT * FROM FishTable";
        data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDecorData()
    {
        Cursor data;
        String query = "SELECT * FROM DecorationTable";
        data = db.rawQuery(query, null);
        return data;

    }

    public Cursor getDecorID(String name)
    {
        String query = "SELECT * FROM DecorationTable WHERE decorType = '" + name + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }



    public Cursor getFishID(String name)
    {

        String query = "SELECT * FROM FishTable WHERE fishType = '" + name + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getCompData(String selectedFish)
    {
        Cursor data;
        String query = "SELECT [" + selectedFish + "] FROM FishCompTable";
        data = db.rawQuery(query, null);

      return data;
    }

    public Cursor getCompData2(String selectedFish){

        Cursor data;
        String query = "SELECT [" + selectedFish + "] FROM FishCompTable2";
        data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getCautionData(String fish){
        Cursor data;
        String query = "SELECT [" + fish + "] FROM FishCompTableC";
        data = db.rawQuery(query, null);

        return data;
    }


}
