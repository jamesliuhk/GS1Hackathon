package com.example.yzxing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainActivity_Customer extends AppCompatActivity {

    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity_Customer";
    ArrayList<String > codeList;
    ArrayList<String > showList;
    ListView listView;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button mScanner = (Button) findViewById(R.id.scanner);

        codeList = new ArrayList<>();
        showList = new ArrayList<>();
        mScanner.setOnClickListener(mScannerListener);


        myAdapter = new MyAdapter(MainActivity_Customer.this, showList);
        //绑定listView控件
        listView = (ListView)findViewById(R.id.codeList);
        //绑定自定义适配器到listView
        listView.setAdapter(myAdapter);

        // from ChooseModePage to here
        Intent fromChooseModePage = getIntent();
        //Todo: you can get date form previous activity at here




    }

    private View.OnClickListener mScannerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(MainActivity_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                goScanner();
            } else {
                ActivityCompat.requestPermissions(MainActivity_Customer.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISION_CODE_CAMARE);
            }
        }
    };

    private void goScanner() {
        Intent intent = new Intent(this, ScannerActivity.class);

        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC,true);
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
        DBOperation db = new DBOperation(this);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
                    List<String>[] res = db.getTrace(content);

                    codeList.add(content);
                    showList.add("Upstream:");
                    showList .addAll(res[0]);

                    showList.add("Detail:");
                    showList.addAll(res[1]);
                    //textView.setText(content);
                    listView.setAdapter(myAdapter);


                    Toast.makeText(MainActivity_Customer.this, "codeType:" + type
                            + "-----content:" + content, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
