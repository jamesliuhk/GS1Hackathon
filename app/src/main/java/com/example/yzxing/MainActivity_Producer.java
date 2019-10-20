package com.example.yzxing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity_Producer extends AppCompatActivity {

    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity_Customer";

    String checkedInfoFromDatabase;


    private Button generate, addSupplier, addDetail;

    //!!!String from activity "AddDetail"
    private String addedDetail;
    //private ArrayList<String> detailList;

    //!!!codeLise store all BarCode we scanned.
    private ArrayList<String> codeList;
    private String lastCode;

    //!!!save for future use, you can use myAdapter and listView to print any array on screen
    private ListView listView;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__producer);

        codeList = new ArrayList<>();

        //connect buttons
        generate = findViewById(R.id.generate);
        addSupplier = findViewById(R.id.addSupplier);
        addDetail = findViewById(R.id.addDetail);

        addSupplier.setOnClickListener(mScannerListener);


        // from ChooseModePage to here
        Intent fromChooseModePage = getIntent();
        addedDetail = null;
        addedDetail = fromChooseModePage.getStringExtra("Detail");
        lastCode = null;
        lastCode = fromChooseModePage.getStringExtra("lastCode");
        checkedInfoFromDatabase = null;
    //ArrayList<String> tmp = fromChooseModePage.getStringArrayListExtra("codeList");
        //codeList =  tmp == null ? new ArrayList<String>() : tmp;
        //addedDetail = null;
        //Todo: you can get date form previous activity at here

        if (addedDetail != null && addedDetail.length() != 0) {
            final String[] detailArr = addedDetail.split("\n");
            final List<String> detailList = new ArrayList<>();
            for (String elem : detailArr) {
                detailList.add(elem);
            }
            DBOperation db = new DBOperation(this);
//            List<String> upList = codeList;
//            upList.remove(upList.get(upList.size() - 1));
            ArrayList<String> code1List = new ArrayList<>();
            code1List.add(lastCode);
            db.insertBarcode("0110028028009000131905151035467121808", code1List, detailList);
            Log.v(TAG, db.getTrace("0110028028009000131905151035467121808")[0].toString() + "????" + db.getTrace("0110028028009000131905151035467121808")[1].toString());

            StringBuilder output = new StringBuilder();
            for (String s : code1List){
                Cursor cursor = db.db.query("companies", new String[]{"barcode", "company"}, "barcode = ?", new String[]{s.substring(3, 10)}, null, null, null);
                if (cursor.moveToFirst()) {
                    output.append("Suppliers: \n" + cursor.getString(cursor.getColumnIndex("company"))+ ": " + s.substring(3, 10));
                }
            }
            Log.v(TAG, "nice" + output);
            checkedInfoFromDatabase = output.toString();
        }



        //jump to AddDetail page
        //Done: wrong method, should use View.OnClickListener!!!!!!!!!!!!!!
        addDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(MainActivity_Producer.this, MainActivity_Producer_AddDetail.class);

                //Done: receive addDetail data from page AddDetail and process it, this
                //Todo: you can send date to another activity at here
                intentJumpToProducer.putExtra("lastCode",lastCode);

                startActivity(intentJumpToProducer);
            }
        });


        //!!!jump to Confirm page
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(MainActivity_Producer.this, MainActivity_Producer_Confirm.class);

                intentJumpToProducer.putExtra("codeList", codeList);
                intentJumpToProducer.putExtra("checkedInfoFromDatabase",checkedInfoFromDatabase);
                intentJumpToProducer.putExtra("addedDetail",addedDetail);
                //Todo: send data to Confirm Page(Detail, previous data/barcode)
                //Todo: you can send date to another activity at here

                startActivity(intentJumpToProducer);
            }
        });


    }

    private View.OnClickListener mScannerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(MainActivity_Producer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                goScanner();
            } else {
                ActivityCompat.requestPermissions(MainActivity_Producer.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISION_CODE_CAMARE);
            }
        }
    };

    private void goScanner() {
        Intent intent = new Intent(this, ScannerActivity.class);

        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true);

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
                    lastCode = content;


                    Toast.makeText(MainActivity_Producer.this, "codeType:" + type
                            + "-----content:" + content, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


