package com.example.iquarium;
import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME2 = "aquarium30.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseOpenHelper(Context context)
    {
        super(context, DATABASE_NAME2, null, DATABASE_VERSION);

    }


}
