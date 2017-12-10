package com.example.wissam.androiddataanalyser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wissam on 11/10/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sqlite.projet.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_NAME = "projet";
    public static final String KEY_ID_ = "id_projet";
    public static final String KEY_TIME = "time";
    public static final String KEY_VALUE = "value";
    public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME +
            " (" + KEY_ID_+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_TIME +" REAL unique, " +
            KEY_VALUE +" REAL);";




    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
