package com.example.yzxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main2Activity_ChooseMode extends AppCompatActivity {

    Button producerMode, customerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__choose_mode);


        //Connect to buttons
        producerMode = findViewById(R.id.ProducerMode);
        customerMode = findViewById(R.id.CustomerMode);

        //jump to customer page
        customerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToCustomer = new Intent(Main2Activity_ChooseMode.this, MainActivity_Customer.class);
                startActivity(intentJumpToCustomer);
            }
        });

        //jump to producer page
        /**
        producerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJumpToProducer = new Intent(Main2Activity_ChooseMode.this, Main2Activity_Producer.class);
                startActivity(intentJumpToProducer);
            }
        });*/





    }
}
