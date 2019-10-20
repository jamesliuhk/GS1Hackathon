package com.example.yzxing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "create Database------------->");
        String createSQL = "CREATE TABLE companies (\n" +
                "  barcode VARCHAR(100) NOT NULL,\n" +
                "  company VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (barcode))";

//        String createSQL2 = "CREATE TABLE oreo (\n" +
//                "  barcode VARCHAR(100) NOT NULL,\n" +
//                "  data VARCHAR(200),\n" +
//                "  supplier VARCHAR(200),\n" +
//                "  PRIMARY KEY (barcode))";

        db.execSQL(createSQL);
        Map<String, String> comMap = new HashMap<>();                         //<barcode, company>"
        comMap.put("0028028", "Lettuce");
        comMap.put("0100169", "FreshCut");
        comMap.put("0169422", "Coke");

        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> entry : comMap.entrySet()) {
            db.execSQL("CREATE TABLE " + entry.getValue() +" (\n" +
                    "  barcode VARCHAR(100) NOT NULL,\n" +
                    "  data VARCHAR(200),\n" +
                    "  supplier VARCHAR(200),\n" +
                    "  PRIMARY KEY (barcode))");
            values.put("barcode", entry.getKey());
            values.put("company", entry.getValue());
            db.insert("companies", null, values);
            values.clear();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
