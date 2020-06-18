package com.boylab.example;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.smartspinner.twowayview.TwoWayLayoutManager;
import com.boylab.smartspinner.twowayview.widget.ListLayoutManager;
import com.boylab.smartspinner.twowayview.widget.TwoWayView;
import com.boylab.smartspinner.R;

import java.util.List;

/**
 * Created by pengle on 2020/06/18
 * Email: pengle609@163.com
 */
public class MyPopupWindow {

    private PopupWindow popupWindow;
    TwoWayView twoWayView;

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    public void init(Context context, List<String> list){
        View rootView = LayoutInflater.from(context).inflate(com.boylab.smartspinner.R.layout.spinner_popupview, null);
        TwoWayView twoWayView = rootView.findViewById(R.id.spinner_TwoWayView);
        twoWayView.setHasFixedSize(true);
        twoWayView.setLongClickable(true);

       /* final Drawable divider = context.getResources().getDrawable(R.drawable.spinner_divider);
        int dividerWidth = divider.getIntrinsicWidth();
        int dividerHeight = divider.getIntrinsicHeight();*/

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

    }

    public void show(View anchor){
        popupWindow.showAsDropDown(anchor, 0, 0);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }



}
