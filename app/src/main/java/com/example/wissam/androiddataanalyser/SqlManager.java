package com.example.wissam.androiddataanalyser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by wissam on 10/10/17.
 */

public class SqlManager{

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.KEY_ID_,
            MySQLiteHelper.KEY_TIME, MySQLiteHelper.KEY_VALUE};

    public SqlManager(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertData(Data c) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_TIME, c.getTime());
        values.put(MySQLiteHelper.KEY_VALUE, c.getNumberSolution());

        return database.insertWithOnConflict(MySQLiteHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long updateContact(int id, Data c){

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_TIME, c.getTime());
        values.put(MySQLiteHelper.KEY_VALUE, c.getNumberSolution());
        return database.update(MySQLiteHelper.TABLE_NAME, values, MySQLiteHelper.KEY_ID_ + " = " +id, null);
    }

    public ArrayList<DataPoint> getAllData() {
        this.open();
        ArrayList<DataPoint> allData = new ArrayList<DataPoint>();


        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Data c = cursorToData(cursor);
            allData.add(new DataPoint(c.getNumberSolution(), c.getTime() ));
            cursor.moveToNext();
        }

        cursor.close();
        this.close();
        return allData;
    }

    private Data cursorToData(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

       // Log.i("test", String.valueOf(c.getColumnCount()));
        Data data = new Data();
        data.setId(c.getLong(c.getColumnIndexOrThrow(MySQLiteHelper.KEY_VALUE)));
        data.setTime(c.getDouble(c.getColumnIndexOrThrow(MySQLiteHelper.KEY_TIME)));
        data.setNumberSolution(c.getDouble(c.getColumnIndexOrThrow(MySQLiteHelper.KEY_VALUE)));

        return data;
    }
}
