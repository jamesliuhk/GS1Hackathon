package com.example.yzxing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity_Producer_AddDetail extends AppCompatActivity {

    private Button add;
    private EditText editText;

    private  String detail;
    private String lastCode;
    private  ArrayList<String> codeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__producer__add_detail);

        //connect buttons
        add = findViewById(R.id.add);

        //connect editText and get String
        editText = findViewById(R.id.editText);




        // from Producer to here
        Intent fromProducer = getIntent();
        //Todo: you can get date form previous activity at here
        //codeList = fromProducer.getStringArrayListExtra("codeList");
        lastCode = null;
        lastCode = fromProducer.getStringExtra("lastCode");

        //back to Producer page and send "Detail" we input

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(MainActivity_Producer_AddDetail.this, MainActivity_Producer.class);

                intentJumpToProducer.putExtra("Detail",editText.getText().toString());
                //intentJumpToProducer.putExtra("codeList",codeList);
                intentJumpToProducer.putExtra("lastCode",lastCode);

                startActivity(intentJumpToProducer);
            }
        });






    }
}
