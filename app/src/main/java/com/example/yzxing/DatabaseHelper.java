package com.example.yzxing;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "gsHack";
    private final static int VERSION_CODE = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "create Database------------->");
        String createSQL = "CREATE TABLE companies (\n" +
                "  barcode VARCHAR(100) NOT NULL,\n" +
                "  company VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (barcode))";

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

        Gson gson = new Gson();
        List<String> dataBar = new ArrayList<>();
        String dataBarcode = gson.toJson(dataBar);
        List<String> supplier = new ArrayList<>();
        supplier.add("NetWeight: 16.8 lb");
        supplier.add("Gross Weight: 19.2 lb");
        supplier.add("Use By Date: May 28, 2019");
        supplier.add("Batch/ Lot No - Case: 354671 / 807");
        String suppliers = gson.toJson(supplier);
        Log.v(TAG, suppliers+"faffafasfasffas");
        values.put("data", suppliers);
        values.put("barcode", "1807");
        values.put("supplier", dataBarcode);
        db.insert("Lettuce", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
