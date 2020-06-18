package com.boylab.smartspinner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.smartspinner.twowayview.TwoWayLayoutManager;
import com.boylab.smartspinner.twowayview.widget.DividerItemDecoration;
import com.boylab.smartspinner.twowayview.widget.GridLayoutManager;
import com.boylab.smartspinner.twowayview.widget.ListLayoutManager;
import com.boylab.smartspinner.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pengle on 2020/06/18
 * Email: pengle609@163.com
 */
public class SmartSpinner extends AppCompatTextView {

    private @DrawableRes
    int textDrawableRes;
    private int textTint;

    private boolean isArrowHidden;
    private int arrowTint;
    private @DrawableRes int arrowDrawableRes;

    private int numColumns = 1, numRows;
    private int itemWidth = 100, itemHeight = 50, spacing = 6, defaultPadding = 12;
    private @DrawableRes
    int itemDrawableResId;
    private SpinnerItemGravity itemGravity;
    private boolean itemChecked;
    private int itemPaddingTop, itemPaddingLeft, itemPaddingBottom, itemPaddingRight;
    private List<CharSequence> charSequences;

    private PopupWindow popupWindow;
    private ViewGroup rootView;
    private TwoWayView twoWayView;
    private TwoWayLayoutManager layoutManager;
    public RecyclerViewAdapter adapter;

    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    private OnSpinnerItemListener onSpinnerItemListener;

    private int[] location = new int[4];
    private int displayHeight;
    private int parentVerticalOffset;

    public SmartSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public SmartSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseRes(context, attrs);

        init(context, attrs);
    }

    public SmartSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        parseRes(context, attrs);

        init(context, attrs);
    }

    private void parseRes(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        int defaultPadding = resources.getDimensionPixelSize(R.dimen.one_and_a_half_grid_unit);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        setPadding(resources.getDimensionPixelSize(R.dimen.three_grid_unit), defaultPadding, defaultPadding, defaultPadding);
        setClickable(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartSpinner2);
        textDrawableRes = typedArray.getResourceId(R.styleable.SmartSpinner2_textDrawable, R.drawable.ic_text_selector);
        textTint = typedArray.getColor(R.styleable.SmartSpinner2_textTint, SmartUtil.getDefaultTextColor(context));

        isArrowHidden = typedArray.getBoolean(R.styleable.SmartSpinner2_arrowHide, false);
        arrowTint = typedArray.getColor(R.styleable.SmartSpinner2_arrowTint, getResources().getColor(android.R.color.black));
        arrowDrawableRes = typedArray.getResourceId(R.styleable.SmartSpinner2_arrowDrawable, R.drawable.smart_arrow);

        numColumns = typedArray.getInt(R.styleable.SmartSpinner2_numColumns, 1);
        numRows = typedArray.getInt(R.styleable.SmartSpinner2_numRows, 1);
        itemWidth = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner2_itemWidth, 100);
        itemHeight = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner2_itemHeight, 50);

        itemPaddingTop = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner2_itemPaddingTop, 0);
        itemPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner2_itemPaddingLeft, 0);
        itemPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner2_itemPaddingBottom, 0);
        itemPaddingRight = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner2_itemPaddingRight, 0);

        itemDrawableResId = typedArray.getResourceId(R.styleable.SmartSpinner2_itemDrawable, R.drawable.smart_arrow);
        itemGravity = SpinnerItemGravity.fromId(typedArray.getInt(R.styleable.SmartSpinner2_itemGravity, SpinnerItemGravity.CENTER.ordinal()));
        itemChecked = typedArray.getBoolean(R.styleable.SmartSpinner2_itemChecked, false);

        // TODO: 2019/12/4 need to use
        CharSequence[] entries = typedArray.getTextArray(R.styleable.SmartSpinner2_entries);
        charSequences = ((entries == null) ? new ArrayList<CharSequence>() : Arrays.asList(entries));

        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs) {

        setBackgroundResource(textDrawableRes);
        setTextColor(textTint);
        setMaxLines(1);
        setWidth(itemWidth);
        setHeight(itemHeight);

        /*SmartUtil.initArrowDrawable(getContext(), arrowDrawableRes, );
        arrowDrawable = initArrowDrawable(arrowTint);*/

        rootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.spinner_popupview, null);
        twoWayView = rootView.findViewById(R.id.spinner_TwoWayView);
        twoWayView.setHasFixedSize(true);
        twoWayView.setLongClickable(true);
        //twoWayView.setFocusable(true);

        final Drawable divider = getResources().getDrawable(R.drawable.spinner_divider);
        int dividerWidth = divider.getIntrinsicWidth();
        int dividerHeight = divider.getIntrinsicHeight();


        twoWayView.addItemDecoration(new DividerItemDecoration(divider));
        if (numColumns == 1) {
            layoutManager = new ListLayoutManager(context, TwoWayLayoutManager.Orientation.VERTICAL);
        } else {
            layoutManager = new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, numColumns, numRows);
        }
        twoWayView.setLayoutManager(layoutManager);
    }

    private void initPopupWindow(){
        popupWindow = new PopupWindow(rootView);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.smart_arrow);
        popupWindow.setBackgroundDrawable(drawable);

        measureDisplayLocation();


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isArrowHidden) {
                    SmartUtil.animateArrow(arrowDrawableRes,false);
                }
            }
        });
    }

    public void measureDisplayLocation() {
        int[] locationOnScreen = new int[2];
        this.getLocationOnScreen(locationOnScreen);
        location[0] = locationOnScreen[0];
        location[1] = locationOnScreen[1];

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        this.measure(w, h);
        location[2] = getMeasuredWidth();
        location[3] = getMeasuredHeight();

        displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        parentVerticalOffset = location[1];
        int measuredHeight = getMeasuredHeight();
        int remainHeight = displayHeight - parentVerticalOffset - measuredHeight;

    }

    public <T> void attachDataSource(@NonNull List<String> list) {
        initPopupWindow();

        freshPopupWindow(list.size());

        adapter = new RecyclerViewAdapter(getContext(), list);
        twoWayView.setAdapter(adapter);

        adapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }

                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }

                adapter.setSelectedIndex(position);
                setText(adapter.getSelectedValue());

                Log.i(">>>boylab>>", ">>>onItemSelected: popupWindow.isShowing() = "+popupWindow.isShowing());
                Log.i(">>>boylab>>", ">>>onItemSelected: popupWindow.isTouchable() = "+isFocusable());
                dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void showPopupWindow(View anchor){
        popupWindow.showAsDropDown(anchor, 0, 0);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public void freshPopupWindow(double childSize) {
        if (childSize == 0) {
            childSize = this.charSequences.size();
        }
        numRows = (int) Math.ceil(childSize / numColumns);

        int popupWidth = numColumns * (itemWidth + spacing) - spacing + defaultPadding * 2;
        int popupHeight = numRows * (itemHeight + spacing) - spacing + defaultPadding * 2;

        popupWindow.setWidth(popupWidth);
        popupWindow.setHeight(popupHeight);

    }

}
