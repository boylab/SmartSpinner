package com.boylab.smartspinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.boylab.library.SmartSpinner;

import com.boylab.library.twowayview.ItemClickSupport;
import com.boylab.library.twowayview.TwoWayLayoutManager;
import com.boylab.library.twowayview.widget.DividerItemDecoration;
import com.boylab.library.twowayview.widget.GridLayoutManager;
import com.boylab.library.twowayview.widget.ListLayoutManager;
import com.boylab.library.twowayview.widget.TwoWayView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SmartSpinner smartSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smartSpinner = findViewById(R.id.smart_Spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        Log.i(">>>boylab>>", ">>>onCreate: " + dataset.size());
        smartSpinner.attachDataSource(dataset);


        final Button btn_Click = findViewById(R.id.click);
        btn_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TwoWayLayoutManager layoutManager;

                /*View rootView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layoutview, null);
                TwoWayView twoWayView = rootView.findViewById(R.id.TwoWayView);*/
                TwoWayView twoWayView = new TwoWayView(getApplicationContext());

                twoWayView.setHasFixedSize(true);
                twoWayView.setLongClickable(true);
                final Drawable divider = getResources().getDrawable(R.drawable.divider);
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
                Log.i(">>>boylab>>", ">>>onClick: height =" + height + " >>>>> width =" + width);
            }
        });


        /*layoutAdapter.setOnItemClickLitener(new LayoutAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(">>>boylab>>", ">>>onItemClick: position = "+position);
            }
        });*/

    }
}
