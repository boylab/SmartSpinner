package com.boylab.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.boylab.smartspinner.SmartSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SmartSpinner smartSpinner01, smartSpinner02, smartSpinner03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smartSpinner01 = findViewById(R.id.smart_Spinner01);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven"));
        smartSpinner01.attachDataSource(dataset);

        smartSpinner02 = findViewById(R.id.smart_Spinner02);
        smartSpinner02.attachDataSource(dataset);

        smartSpinner03 = findViewById(R.id.smart_Spinner03);
        smartSpinner03.attachDataSource(dataset);

    }

}
