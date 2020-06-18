package com.boylab.example;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.boylab.smartspinner.R;
import com.boylab.smartspinner.SmartSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    SmartSpinner smartSpinner01, smartSpinner02, smartSpinner03;
    List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        smartSpinner01 = findViewById(R.id.smart_Spinner01);
        smartSpinner01.attachDataSource(dataset);
        Log.i(">>>boylab>>", ">>>onCreate: " + smartSpinner01.findFocus());

        smartSpinner02 = findViewById(R.id.smart_Spinner02);
        smartSpinner02.attachDataSource(dataset);
        Log.i(">>>boylab>>", ">>>onCreate: " + smartSpinner02.findFocus());

        smartSpinner03 = findViewById(R.id.smart_Spinner03);
        smartSpinner03.attachDataSource(dataset);
        Log.i(">>>boylab>>", ">>>onCreate: " + smartSpinner03.findFocus());























    }

    /*TwoWayLayoutManager layoutManager;

     *//*View rootView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layoutview, null);
    TwoWayView twoWayView = rootView.findViewById(R.id.TwoWayView);*//*
    TwoWayView twoWayView = new TwoWayView(getApplicationContext());

                twoWayView.setHasFixedSize(true);
                twoWayView.setLongClickable(true);
    final Drawable divider = getResources().getDrawable(R.drawable.spinner_divider);
                twoWayView.addItemDecoration(new DividerItemDecoration(divider));

//                layoutManager = new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 3,2);
    layoutManager = new ListLayoutManager(getApplicationContext(), TwoWayLayoutManager.Orientation.VERTICAL);
                twoWayView.setLayoutManager(layoutManager);

    LayoutAdapter layoutAdapter = new LayoutAdapter(getApplicationContext(), twoWayView, 0);
                twoWayView.setAdapter(layoutAdapter);

    PopupWindow mPopupWindow = new PopupWindow(getApplicationContext());

    int height = twoWayView.getLayoutManager().getHeight();
    int width = twoWayView.getLayoutManager().getWidth();
                Log.i(">>>boylab>>", ">>>onClick: height =" + height + " >>>>> width =" + width);

                mPopupWindow.setHeight(500);
                mPopupWindow.setWidth(500);
                mPopupWindow.setContentView(twoWayView);
                mPopupWindow.setTouchable(true);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setOutsideTouchable(true);

                mPopupWindow.setWidth(800);
                mPopupWindow.showAsDropDown(btn_Click);
    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.spinner_drawable);

    ColorDrawable dw = new ColorDrawable(getApplication().getResources().getColor(android.R.color.holo_red_dark));
                mPopupWindow.setBackgroundDrawable(dw);

    height = twoWayView.getHeight();
    width = twoWayView.getWidth();
                Log.i(">>>boylab>>", ">>>onClick: height =" + height + " >>>>> width =" + width);*/

}
