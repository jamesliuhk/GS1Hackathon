package com.example.yzxing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "create Database------------->");
        String createSQL1 = "CREATE TABLE companies (\n" +
                "  barcode VARCHAR(100) NOT NULL,\n" +
                "  company VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (barcode))";

        String createSQL2 = "CREATE TABLE oreo (\n" +
                "  barcode VARCHAR(100) NOT NULL,\n" +
                "  data VARCHAR(200),\n" +
                "  supplier VARCHAR(200),\n" +
                "  PRIMARY KEY (barcode))";

        db.execSQL(createSQL1);
        db.execSQL(createSQL2);
        ContentValues values = new ContentValues();
        values.put("barcode", "0467589");
        values.put("company", "oreo");
        db.insert("companies", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
