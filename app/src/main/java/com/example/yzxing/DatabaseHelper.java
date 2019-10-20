package com.example.yzxing;

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
        String createSQL = "CREATE TABLE company (\n" +
                "  barcode VARCHAR(45) NOT NULL,\n" +
                "  company VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (barcode))";

        db.execSQL(createSQL);


        Log.v(TAG, "update Database------------->");
        this.getWritableDatabase().insert();
        db.execSQL("INSERT INTO company (barcode, company) VALUES ('0496580', 'CokeCola')");
        db.execSQL("INSERT INTO company (barcode, company) VALUES ('0496581', 'CokeCole')");
        Cursor cursor = this.getReadableDatabase().query("company", new String[]{"barcode","company"}, "barcode = ?", new String[]{"0496580"}, null, null, null);
        String name = cursor.getString(cursor.getColumnIndex("barcode"));
        Log.v(TAG, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                break;

            default:
                break;
        }
    }
}
