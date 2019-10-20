package com.example.yzxing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity_Producer_Confirm extends AppCompatActivity {

    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__producer__confirm);

        //connect buttons
        confirm = findViewById(R.id.confirm);

        // from Producer to here
        Intent fromProducer = getIntent();
        //Todo: you can get date form previous activity at here



        //Todo: connect to ListView, set textView

        //jump to GenerateCode page
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(MainActivity_Producer_Confirm.this, MainActivity_Producer_GenerateCode.class);


                //Todo: you can send data to Generator at here

                startActivity(intentJumpToProducer);
            }
        });

        //Todo: processing data and generate new Barcode
    }
}
