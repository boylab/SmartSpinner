package com.boylab.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.boylab.smartspinner.OnSpinnerItemListener;
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
        smartSpinner01.setEnabled(false);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven"));
        smartSpinner01.attachDataSource(dataset);
        smartSpinner01.setOnSpinnerItemListener(new OnSpinnerItemListener() {
            @Override
            public void onItemClick(SmartSpinner parent, View view, int position, long id) {
                Log.i(">>>>>", "onItemClick: "+position);
            }
        });

        smartSpinner02 = findViewById(R.id.smart_Spinner02);
        // TODO: 2020/9/7 重复设置
        /*smartSpinner02.setNumColumns(3);
        smartSpinner02.attachDataSource(dataset);*/
        smartSpinner02.setSelection(2);


        smartSpinner03 = findViewById(R.id.smart_Spinner03);
        smartSpinner03.attachDataSource(dataset);

        smartSpinner01.setSelection(0);


    }

}
