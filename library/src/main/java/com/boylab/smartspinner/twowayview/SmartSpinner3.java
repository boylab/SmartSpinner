package com.boylab.smartspinner.twowayview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.smartspinner.R;
import com.boylab.smartspinner.RecyclerViewAdapter;
import com.boylab.smartspinner.twowayview.widget.ListLayoutManager;
import com.boylab.smartspinner.twowayview.widget.TwoWayView;

import java.util.List;

/**
 * Created by pengle on 2020/06/18
 * Email: pengle609@163.com
 */
public class SmartSpinner3 extends AppCompatTextView {

    /*private MyPopupWindow popupWindow;*/

    public SmartSpinner3(Context context) {
        super(context);
        init(context, null);
    }

    public SmartSpinner3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartSpinner3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // TODO: 2020/6/18 初始化
    }

    /*public <T> void attachDataSource(@NonNull List<String> list) {
        View rootView = LayoutInflater.from(context).inflate(com.boylab.smartspinner.R.layout.spinner_popupview, null);
        TwoWayView twoWayView = rootView.findViewById(R.id.spinner_TwoWayView);
        twoWayView.setHasFixedSize(true);
        twoWayView.setLongClickable(true);

       *//* final Drawable divider = context.getResources().getDrawable(R.drawable.spinner_divider);
        int dividerWidth = divider.getIntrinsicWidth();
        int dividerHeight = divider.getIntrinsicHeight();*//*

        RecyclerView.LayoutManager layoutManager = new ListLayoutManager(context, TwoWayLayoutManager.Orientation.VERTICAL);

        //layoutManager = new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 2, 2);
        twoWayView.setLayoutManager(layoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, list);
        twoWayView.setAdapter(adapter);

        adapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i(">>>boylab>>", ">>>onItemSelected: popupWindow.isShowing() = "+popupWindow.isShowing());
                Log.i(">>>boylab>>", ">>>onItemSelected: popupWindow.isTouchable() = "+popupWindow.isTouchable());
                popupWindow.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(rootView);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.spinner_drawable);
        popupWindow.setBackgroundDrawable(drawable);

        //popupWindow.showAsDropDown(anchor);


        popupWindow = new MyPopupWindow();
        popupWindow.init(getContext(), list);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(">>>boylab>>", ">>>onTouchEvent: "+isEnabled());
        Log.i(">>>boylab>>", ">>>onTouchEvent: "+(event.getAction() == MotionEvent.ACTION_UP));

        if (isEnabled() && event.getAction() != MotionEvent.ACTION_UP) {
            Log.i(">>>boylab>>", ">>>onTouchEvent123: "+popupWindow.getPopupWindow().isShowing());
            if (popupWindow.getPopupWindow().isShowing()) {
                popupWindow.dismiss();
            } else {
                popupWindow.show(this);
            }
        }
        return super.onTouchEvent(event);
    }*/
}
