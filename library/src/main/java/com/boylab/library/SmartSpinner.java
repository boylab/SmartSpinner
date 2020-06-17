package com.boylab.library;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.boylab.library.twowayview.TwoWayLayoutManager;
import com.boylab.library.twowayview.widget.DividerItemDecoration;
import com.boylab.library.twowayview.widget.GridLayoutManager;
import com.boylab.library.twowayview.widget.ListLayoutManager;
import com.boylab.library.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
 * Copyright (C) 2015 Angelo Marchesin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SmartSpinner extends AppCompatTextView {

    private static final int MAX_LEVEL = 10000;
    private static final int VERTICAL_OFFSET = 1;
    private static final String INSTANCE_STATE = "instance_state";
    private static final String SELECTED_INDEX = "selected_index";
    private static final String IS_POPUP_SHOWING = "is_popup_showing";
    private static final String IS_ARROW_HIDDEN = "is_arrow_hidden";
    private static final String ARROW_DRAWABLE_RES_ID = "arrow_drawable_res_id";

     /*<attr name="textDrawable" format="reference|color" />
        <attr name="textTint" format="color" />

        <!--arrow ishide、tint、drawable-->
        <attr name="arrowHide" format="boolean" />
        <attr name="arrowTint" format="color" />
        <attr name="arrowDrawable" format="reference|color" />

        <attr name="numColumns" format="integer" />
        <attr name="numRows" format="integer" />
        <attr name="itemWidth" format="dimension" />
        <attr name="itemHeight" format="dimension" />
        <attr name="itemDrawable" format="reference|color" />
        <attr name="itemGravity" />
        <attr name="itemChecked" format="boolean" />
        <attr name="itemPaddingTop" format="dimension" />
        <attr name="itemPaddingLeft" format="dimension" />
        <attr name="itemPaddingBottom" format="dimension" />
        <attr name="itemPaddingRight" format="dimension" />
        <attr name="entries" format="reference" />*/


    private @DrawableRes
    int textDrawableRes;
    private int textTint;

    private boolean isArrowHidden;
    private int arrowTint;
    private @DrawableRes
    int arrowDrawableRes;

    private int numColumns = 1, numRows;
    private int itemWidth = 100, itemHeight = 50, spacing = 6, defaultPadding = 12;
    private @DrawableRes
    int itemDrawableResId;
    private SpinnerItemGravity itemGravity;
    private boolean itemChecked;
    private int itemPaddingTop, itemPaddingLeft, itemPaddingBottom, itemPaddingRight;
    private List<CharSequence> charSequences;

    private int selectedIndex;
    private Drawable arrowDrawable;
    private PopupWindow popupWindow;
    private ViewGroup rootView;
    private TwoWayView twoWayView;
    private TwoWayLayoutManager layoutManager;
    public LayoutAdapter adapter;

    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    private OnSpinnerItemListener onSpinnerItemListener;

    private int[] location = new int[4];

    private int displayHeight;
    private int parentVerticalOffset;

    private TextFormat spinnerTextFormatter = new SpinnerTextFormat();
    private TextFormat selectedTextFormatter = new SpinnerTextFormat();

    @Nullable
    private ObjectAnimator arrowAnimator = null;

    private boolean isShowing = false;

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public SmartSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public SmartSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void parseRes(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        int defaultPadding = resources.getDimensionPixelSize(R.dimen.one_and_a_half_grid_unit);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        setPadding(resources.getDimensionPixelSize(R.dimen.three_grid_unit), defaultPadding, defaultPadding, defaultPadding);
        setClickable(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartSpinner);
        textDrawableRes = typedArray.getResourceId(R.styleable.SmartSpinner_textDrawable, R.drawable.ic_text_selector);
        textTint = typedArray.getColor(R.styleable.SmartSpinner_textTint, getDefaultTextColor(context));

        isArrowHidden = typedArray.getBoolean(R.styleable.SmartSpinner_arrowHide, false);
        arrowTint = typedArray.getColor(R.styleable.SmartSpinner_arrowTint, getResources().getColor(android.R.color.black));
        arrowDrawableRes = typedArray.getResourceId(R.styleable.SmartSpinner_arrowDrawable, R.drawable.smart_arrow);

        numColumns = typedArray.getInt(R.styleable.SmartSpinner_numColumns, 1);
        numRows = typedArray.getInt(R.styleable.SmartSpinner_numRows, 1);
        itemWidth = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemWidth, 100);
        itemHeight = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemHeight, 50);

        itemPaddingTop = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingTop, 0);
        itemPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingLeft, 0);
        itemPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingBottom, 0);
        itemPaddingRight = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingRight, 0);

        itemDrawableResId = typedArray.getResourceId(R.styleable.SmartSpinner_itemDrawable, R.drawable.smart_arrow);
        itemGravity = SpinnerItemGravity.fromId(typedArray.getInt(R.styleable.SmartSpinner_itemGravity, SpinnerItemGravity.CENTER.ordinal()));
        itemChecked = typedArray.getBoolean(R.styleable.SmartSpinner_itemChecked, false);

        // TODO: 2019/12/4 need to use
        CharSequence[] entries = typedArray.getTextArray(R.styleable.SmartSpinner_entries);
        charSequences = ((entries == null) ? new ArrayList<CharSequence>() : Arrays.asList(entries));

        defaultPadding = resources.getDimensionPixelSize(R.dimen.one_and_a_half_grid_unit);

        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        parseRes(context, attrs);

        setBackgroundResource(textDrawableRes);
        setTextColor(textTint);
        setMaxLines(1);
        setWidth(itemWidth);
        setHeight(itemHeight);

        initArrowDrawable(arrowDrawableRes);
        arrowDrawable = initArrowDrawable(arrowTint);

        rootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.spinner_popupview, null);
        twoWayView = rootView.findViewById(R.id.spinner_TwoWayView);
        twoWayView.setHasFixedSize(true);
        twoWayView.setLongClickable(true);

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

        //popupWindow = new PopupWindow(context);
        popupWindow = new PopupWindow(rootView);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.smart_arrow);
        popupWindow.setBackgroundDrawable(drawable);

        popupWindow.setFocusable(false);
        popupWindow.setTouchable(false);
        popupWindow.setOutsideTouchable(false);

        //Drawable drawable = ContextCompat.getDrawable(context, R.drawable.spinner_drawable);

        //popupWindow.setContentView(rootView);

        measureDisplayLocation();
        freshPopupWindow(0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isArrowHidden) {
                    animateArrow(false);
                }
            }
        });

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
        getParentVerticalOffset();
        int measuredHeight = getMeasuredHeight();
        int remainHeight = displayHeight - getParentVerticalOffset() - measuredHeight;

    }

    private int getParentVerticalOffset() {
        return parentVerticalOffset = location[VERTICAL_OFFSET];
    }

    @Override
    protected void onDetachedFromWindow() {
        if (arrowAnimator != null) {
            arrowAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            onVisibilityChanged(this, getVisibility());
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        arrowDrawable = initArrowDrawable(arrowTint);
        setArrowDrawableOrHide(arrowDrawable);
    }

    private Drawable initArrowDrawable(int drawableTint) {
        if (arrowDrawableRes == 0) return null;
        Drawable drawable = ContextCompat.getDrawable(getContext(), arrowDrawableRes);
        if (drawable != null) {
            // Gets a copy of this drawable as this is going to be mutated by the animator
            drawable = DrawableCompat.wrap(drawable).mutate();
            if (drawableTint != Integer.MAX_VALUE && drawableTint != 0) {
                DrawableCompat.setTint(drawable, drawableTint);
            }
        }
        return drawable;
    }

    private void setArrowDrawableOrHide(Drawable drawable) {
        if (!isArrowHidden && drawable != null) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private int getDefaultTextColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorPrimary});
        int defaultTextColor = typedArray.getColor(0, Color.BLACK);
        typedArray.recycle();
        return defaultTextColor;
    }


    /*public Object getItemAtPosition(int position) {
        return adapter.getItemInDataset(position);
    }

    public Object getSelectedItem() {
        return adapter.getItemInDataset(selectedIndex);
    }*/

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setArrowDrawable(@DrawableRes @ColorRes int drawableId) {
        arrowDrawableRes = drawableId;
        arrowDrawable = initArrowDrawable(R.drawable.smart_arrow);
        setArrowDrawableOrHide(arrowDrawable);
    }

    public void setArrowDrawable(Drawable drawable) {
        arrowDrawable = drawable;
        setArrowDrawableOrHide(arrowDrawable);
    }

    /*private void setTextInternal(Object item) {
        if (selectedTextFormatter != null) {
            setText(selectedTextFormatter.format(item));
        } else {
            setText(item.toString());
        }
    }*/

    /**
     * Set the default spinner item using its index
     *
     * @param position the item's position
     */
    public void setSelectedIndex(int position) {
        if (adapter != null) {
            if (position >= 0 && position <= adapter.getItemCount()) {
                adapter.setSelectedIndex(position);
                selectedIndex = position;
                setText(selectedTextFormatter.format(adapter.getItemInDataset(position)).toString());
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public <T> void attachDataSource(int entriesRes) {
        CharSequence[] entries = getResources().getTextArray(entriesRes);
        charSequences = ((entries == null) ? new ArrayList<CharSequence>() : Arrays.asList(entries));

        //List<String> list = charSequences;

        //attachDataSource(list);

    }

    public <T> void attachDataSource(@NonNull List<String> list) {
        freshPopupWindow(list.size());

        adapter = new SmartSpinnerAdapter(getContext(), list, itemWidth, itemHeight, textTint, textDrawableRes, itemGravity,
                itemPaddingLeft, itemPaddingTop, itemPaddingRight, itemPaddingBottom);

        adapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;

                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }

                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }

                adapter.setSelectedIndex(position);

                setText(adapter.getItemInDataset(position));

                Log.i(">>>boylab>>", ">>>onItemSelected: popupWindow.isShowing() = "+popupWindow.isShowing());
                Log.i(">>>boylab>>", ">>>onItemSelected: popupWindow.isTouchable() = "+popupWindow.isTouchable());
                dismissDropDown();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setAdapterInternal(adapter);
    }

    private <T> void setAdapterInternal(LayoutAdapter adapter) {
        if (adapter.getItemCount() >= 0) {
            selectedIndex = 0;

            twoWayView.setAdapter(adapter);

            setText(adapter.getItemInDataset(selectedIndex));

            // TODO: 2019/12/9 监听
            //adapter.

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (adapter.getItemCount() <= 0){
            return super.onTouchEvent(event);
        }

        if (isEnabled() && event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(">>>boylab>>", ">>>onTouchEvent: "+(isShowing()));
            Log.i(">>>boylab>>", ">>>onTouchEvent: "+adapter.getItemCount());

            if (isShowing()) {
                dismissDropDown();
            } else {
                showDropDown();
            }
        }
        return true;
    }

    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : MAX_LEVEL;
        int end = shouldRotateUp ? MAX_LEVEL : 0;
        arrowAnimator = ObjectAnimator.ofInt(arrowDrawable, "level", start, end);
        arrowAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        arrowAnimator.start();
    }

    public void dismissDropDown() {
        if (!isArrowHidden) {
            animateArrow(false);
        }
        Log.i(">>>boylab>>", "dismissDropDown: "+popupWindow);
        Log.i(">>>boylab>>", "dismissDropDown: "+popupWindow.isFocusable());
        popupWindow.dismiss();
        setShowing(false);

    }

    public void showDropDown() {
        if (!isArrowHidden) {
            animateArrow(true);
        }

        popupWindow.showAsDropDown(this, 0, 0);
        setShowing(true);

        Log.i(">>>boylab>>", "showDropDown: "+popupWindow);
        popupWindow.setFocusable(true); //获取焦点
        popupWindow.setTouchable(true); //设置可触摸
        popupWindow.setOutsideTouchable(true);  //设置触摸区域外可隐藏PopupWindow
        popupWindow.update();
        Log.i(">>>boylab>>", ">>>showDropDown: popupWindow.isShowing() = "+popupWindow.isShowing());
        Log.i(">>>boylab>>", ">>>showDropDown: popupWindow.isTouchable() = "+popupWindow.isTouchable());

        /*final RelativeLayout wayView = (RelativeLayout) popupWindow.getContentView();
        if (wayView != null) {
            wayView.setVerticalScrollBarEnabled(false);
            wayView.setHorizontalScrollBarEnabled(false);
            wayView.setVerticalFadingEdgeEnabled(false);
            wayView.setHorizontalFadingEdgeEnabled(false);
        }*/
    }

    private int getPopUpHeight() {
        return Math.max(verticalSpaceBelow(), verticalSpaceAbove());
    }

    private int verticalSpaceAbove() {
        return getParentVerticalOffset();
    }

    private int verticalSpaceBelow() {
        return displayHeight - getParentVerticalOffset() - getMeasuredHeight();
    }

    public void setTintColor(@ColorRes int resId) {
        if (arrowDrawable != null && !isArrowHidden) {
            DrawableCompat.setTint(arrowDrawable, ContextCompat.getColor(getContext(), resId));
        }
    }

    public void setArrowTintColor(int resolvedColor) {
        if (arrowDrawable != null && !isArrowHidden) {
            DrawableCompat.setTint(arrowDrawable, resolvedColor);
        }
    }

    public void hideArrow() {
        isArrowHidden = true;
        setArrowDrawableOrHide(arrowDrawable);
    }

    public void showArrow() {
        isArrowHidden = false;
        setArrowDrawableOrHide(arrowDrawable);
    }

    public boolean isArrowHidden() {
        return isArrowHidden;
    }

    public void setItemPaddingBottom(int paddingBottom) {
        itemPaddingBottom = paddingBottom;
    }

    public int getItemPaddingBottom() {
        return itemPaddingBottom;
    }

    public void setSpinnerTextFormatter(TextFormat spinnerTextFormat) {
        this.spinnerTextFormatter = spinnerTextFormat;
    }

    public void setSelectedTextFormatter(TextFormat textFormat) {
        this.selectedTextFormatter = textFormat;
    }


    public void performItemClick(int position, boolean showDropdown) {
        if (showDropdown) showDropDown();
        setSelectedIndex(position);
    }

    /**
     * only applicable when popup is shown .
     *
     * @param view
     * @param position
     * @param id
     */
    public void performItemClick(View view, int position, int id) {
        showDropDown();
        final TwoWayView listView = (TwoWayView) popupWindow.getContentView();
        if (listView != null) {
            //listView.performItemClick(view, position, id);
        }
    }

    public OnSpinnerItemListener getOnSpinnerItemListener() {
        return onSpinnerItemListener;
    }

    public void setOnSpinnerItemListener(OnSpinnerItemListener onSpinnerItemListener) {
        this.onSpinnerItemListener = onSpinnerItemListener;
    }

    public SpinnerItemGravity getPopUpTextAlignment() {
        return itemGravity;
    }


}

