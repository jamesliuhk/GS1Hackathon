package com.example.yzxing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.Constant;
import com.example.qrcode.ScannerActivity;

import java.util.ArrayList;

public class MainActivity_Producer extends AppCompatActivity {

    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity_Customer";

    private Button generate, addSupplier, addDetail;

    //!!!String from activity "AddDetail"
    private String addedDetail;

    //!!!codeLise store all BarCode we scanned.
    private ArrayList<String > codeList;

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
        addedDetail=fromChooseModePage.getStringExtra("Detail");
        //Todo: you can get date form previous activity at here



        //jump to AddDetail page
        //Done: wrong method, should use View.OnClickListener!!!!!!!!!!!!!!
        addDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(MainActivity_Producer.this, MainActivity_Producer_AddDetail.class);

                //Done: receive addDetail data from page AddDetail and process it, this
                //Todo: you can send date to another activity at here

                startActivity(intentJumpToProducer);
            }
        });



        //!!!jump to Confirm page
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(MainActivity_Producer.this, MainActivity_Producer_Confirm.class);

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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);

                    codeList.add(content);


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


