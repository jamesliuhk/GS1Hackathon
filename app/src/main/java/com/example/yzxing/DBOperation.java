package com.example.yzxing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DBOperation {
    DatabaseHelper helper;
    SQLiteDatabase db;
    Gson gson;

    public DBOperation(Context context) {
        this.helper = new DatabaseHelper(context);
        this.db  = helper.getWritableDatabase();
        this.gson = new Gson();
    }

    public void insertBarcode(String barcode, List<String> supBarcode, List<String> comData) {
        String comBarcode = barcode.substring(3, 10);                                                              //7 digits
        String dataBarcode = barcode.substring(barcode.length() - 4, barcode.length());                       //last 2 digits
        String comName = "";
        Cursor cursor = db.query("companies", new String[]{"barcode", "company"}, "barcode = ?", new String[]{comBarcode}, null, null, null);
        if (cursor.moveToFirst()) {
            comName = cursor.getString(cursor.getColumnIndex("company"));
        }
        String suppliers = gson.toJson(supBarcode);
        cursor = db.query(comName, new String[]{"barcode", "data", "supplier"}, "barcode = ?", new String[]{dataBarcode}, null, null, null);
        if (cursor.getCount() > 0) {
            Log.v(TAG, "This barcode has been stored!");
            return;
        }
        cursor.close();

        String data = gson.toJson(comData);
        ContentValues values = new ContentValues();
        values.put("data", data);
        values.put("barcode", dataBarcode);
        values.put("supplier", suppliers);
        db.insert(comName, null, values);
    }

    public List<String>[] getTrace(String barcode) {
        String comBarcode = barcode.substring(3, 10);                                                              //7 digits
        String dataBarcode = barcode.substring(barcode.length() - 4, barcode.length());                       //last 3 digits
        String comName = "";

        Cursor cursor = db.query("companies", new String[]{"barcode", "company"}, "barcode = ?", new String[]{comBarcode}, null, null, null);
        if (cursor.moveToFirst()) {
            comName = cursor.getString(cursor.getColumnIndex("company"));
        }
        cursor = db.query(comName, new String[]{"barcode", "data", "supplier"}, "barcode = ?", new String[]{dataBarcode}, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            Log.v(TAG, "There is no such product!");
            return null;
        }

        String supplier = "";
        String detail = "";
        if (cursor.moveToFirst()) {
            supplier = cursor.getString(cursor.getColumnIndex("supplier"));
            detail = cursor.getString(cursor.getColumnIndex("data"));
        }

        List<String> supCodeList= gson.fromJson(supplier, List.class);
        List<String> detailList= gson.fromJson(detail, List.class);
        List<String> supList = new ArrayList<>();
        List<String> outputList = new ArrayList<>();

        for (String sup : supCodeList) {
            cursor = db.query("companies", new String[]{"barcode", "company"}, "barcode = ?", new String[]{sup.substring(3, 10)}, null, null, null);
            if (cursor.moveToFirst()) {
                comName = cursor.getString(cursor.getColumnIndex("company"));
                supList.add(comName);
            }
        }


        for (String sup : supList) {
            StringBuilder output = new StringBuilder();
            output.append(sup + "\n");
            cursor = db.rawQuery("select data from "+sup, null);
            if (cursor.moveToFirst()) {
                String test = cursor.getString(cursor.getColumnIndex("data"));
                List<String> list = gson.fromJson(test, List.class);
                for (String s : list){
                    output.append("\n"+s);
                }

            }
            outputList.add(output.toString());
        }

        List<String>[] res = new ArrayList[2];

        res[0] = outputList;
        res[1] = detailList;

        cursor.close();

        return res;
    }
}
