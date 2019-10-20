package com.example.yzxing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity_Producer_AddDetail extends AppCompatActivity {

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__producer__add_detail);

        //connect buttons
        add = findViewById(R.id.add);

        //back to Producer page
        //Todo: Wrong!!!!!!!!!!!!
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(MainActivity_Producer_AddDetail.this, MainActivity_Producer.class);

                //Todo: send data to AddDetail Page(Detail, previous data/barcode)

                startActivity(intentJumpToProducer);
            }
        });


        // from Producer to here
        Intent fromProducer = getIntent();

        //Todo: Get data from "add" and return to Producer


    }
}
