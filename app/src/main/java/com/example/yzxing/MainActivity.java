package com.example.yzxing;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.Constant;
import com.example.qrcode.ScannerActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    ArrayList<String> codeList;
    TextView textView;
    ListView listView;
    MyAdapter myAdapter;

//    private HashMap<String, Set> mHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        codeList = new ArrayList<>();
        //codeList.add("123");

        setContentView(R.layout.activity_main);
        Button mScanner = (Button) findViewById(R.id.scanner);
        mScanner.setOnClickListener(mScannerListener);
        //textView = findViewById(R.id.textView2);


        //textView.setText("222");


        myAdapter = new MyAdapter(MainActivity.this, codeList);
        //绑定listView控件
        listView = (ListView) findViewById(R.id.codeList);
        //绑定自定义适配器到listView
        listView.setAdapter(myAdapter);


        //textView.setText("222");

//        Set<BarcodeFormat> codeFormats = EnumSet.of(BarcodeFormat.QR_CODE
//                , BarcodeFormat.CODE_128
//                , BarcodeFormat.CODE_93 );
//        mHashMap.put(ScannerActivity.BARCODE_FORMAT, codeFormats);

        DBOperation db = new DBOperation();
    }

    private View.OnClickListener mScannerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                goScanner();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISION_CODE_CAMARE);
            }
        }
    };

    private void goScanner() {
        Intent intent = new Intent(this, ScannerActivity.class);
        //这里可以用intent传递一些参数，比如扫码聚焦框尺寸大小，支持的扫码类型。
//        //设置扫码框的宽
//        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, 400);
//        //设置扫码框的高
//        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, 400);
//        //设置扫码框距顶部的位置
//        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_TOP_PADDING, 100);
//        //设置是否启用从相册获取二维码。
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true);
//        Bundle bundle = new Bundle();
//        //设置支持的扫码类型
//        bundle.putSerializable(Constant.EXTRA_SCAN_CODE_TYPE, mHashMap);
//        intent.putExtras(bundle);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISION_CODE_CAMARE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);

                    codeList.add(content);
                    //textView.setText(content);
                    listView.setAdapter(myAdapter);


                    Toast.makeText(MainActivity.this, "codeType:" + type
                            + "-----content:" + content, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class DBOperation {
        DatabaseHelper helper;
        SQLiteDatabase db;
        Gson gson;

        public DBOperation() {
            this.helper = new DatabaseHelper(MainActivity.this);
            this.db  = helper.getWritableDatabase();
            this.gson = new Gson();
        }

        public void insertBarcode(String barcode, List<String> supBarcode, List<String> comData) {
            String comBarcode = barcode.substring(3, 10);                                                              //7 digits
            String dataBarcode = barcode.substring(barcode.length() - 3);                       //last 2 digits
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
            String dataBarcode = barcode.substring(barcode.length() - 3);                       //last 2 digits
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

            List<String> supList= gson.fromJson(supplier, List.class);
            List<String> detailList= gson.fromJson(detail, List.class);

            List<String>[] res = new ArrayList[2];


            res[0] = supList;
            res[1] = detailList;

            cursor.close();

            return res;
        }
    }
}
