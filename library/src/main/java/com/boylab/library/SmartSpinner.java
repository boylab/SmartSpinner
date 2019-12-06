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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;


import android.widget.ListPopupWindow;
import android.widget.ListView;

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

    private int backgroundSelector;
    private int textTint;

    private boolean isArrowHidden;
    private int arrowTint;
    private @DrawableRes int arrowDrawableResId;

    private int numColumns = 1;
    private int numRows;
    private @DrawableRes int itemDrawableResId;
    private SpinnerItemGravity itemGravity;
    private boolean itemChecked ;
    private int itemPaddingTop, itemPaddingLeft, itemPaddingBottom, itemPaddingRight;
    private List<CharSequence> charSequences;

    private int selectedIndex;
    private Drawable arrowDrawable;
    private PopupWindow popupWindow;
    private TwoWayView twoWayView ;
    private TwoWayLayoutManager layoutManager;
    private LayoutAdapter adapter;

    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    private OnSpinnerItemListener onSpinnerItemListener;

    private int[] location = new int[4];

    private int displayHeight;
    private int parentVerticalOffset;

    private TextFormat spinnerTextFormatter = new SpinnerTextFormat();
    private TextFormat selectedTextFormatter = new SpinnerTextFormat();

    @Nullable
    private ObjectAnimator arrowAnimator = null;

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

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(SELECTED_INDEX, selectedIndex);
        bundle.putBoolean(IS_ARROW_HIDDEN, isArrowHidden);
        bundle.putInt(ARROW_DRAWABLE_RES_ID, arrowDrawableResId);
        if (popupWindow != null) {
            bundle.putBoolean(IS_POPUP_SHOWING, popupWindow.isShowing());
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            selectedIndex = bundle.getInt(SELECTED_INDEX);
            if (adapter != null) {
                setTextInternal(selectedTextFormatter.format(adapter.getItemInDataset(selectedIndex)).toString());
                adapter.setSelectedIndex(selectedIndex);
            }

            if (bundle.getBoolean(IS_POPUP_SHOWING)) {
                if (popupWindow != null) {
                    // Post the show request into the looper to avoid bad token exception
                    // TODO: 2019/11/21
                    showDropDown();
                    //post(this::showDropDown);
                }
            }
            isArrowHidden = bundle.getBoolean(IS_ARROW_HIDDEN, false);
            arrowDrawableResId = bundle.getInt(ARROW_DRAWABLE_RES_ID);
            savedState = bundle.getParcelable(INSTANCE_STATE);
        }
        super.onRestoreInstanceState(savedState);
    }

    private void parseRes(Context context, AttributeSet attrs){
        Resources resources = getResources();
        int defaultPadding = resources.getDimensionPixelSize(R.dimen.one_and_a_half_grid_unit);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        setPadding(resources.getDimensionPixelSize(R.dimen.three_grid_unit), defaultPadding, defaultPadding, defaultPadding);
        setClickable(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartSpinner);
        backgroundSelector = typedArray.getResourceId(R.styleable.SmartSpinner_backgroundSelector, R.drawable.selector);
        textTint = typedArray.getColor(R.styleable.SmartSpinner_textTint, getDefaultTextColor(context));

        isArrowHidden = typedArray.getBoolean(R.styleable.SmartSpinner_arrowHide, false);
        arrowTint = typedArray.getColor(R.styleable.SmartSpinner_arrowTint, getResources().getColor(android.R.color.black));
        arrowDrawableResId = typedArray.getResourceId(R.styleable.SmartSpinner_arrowDrawable, R.drawable.smart_arrow);

        numColumns = typedArray.getInt(R.styleable.SmartSpinner_numColumns, 1);
        numRows = typedArray.getInt(R.styleable.SmartSpinner_numRows, 1);

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

        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        parseRes(context, attrs);

        setBackgroundResource(backgroundSelector);
        setTextColor(textTint);

        initArrowDrawable(arrowDrawableResId);
        arrowDrawable = initArrowDrawable(arrowTint);

        twoWayView = new TwoWayView(context);
        twoWayView.setHasFixedSize(true);
        twoWayView.setLongClickable(true);
        final Drawable divider = getResources().getDrawable(R.drawable.divider);
        twoWayView.addItemDecoration(new DividerItemDecoration(divider));

        if (numColumns == 1){
            layoutManager = new ListLayoutManager(context, TwoWayLayoutManager.Orientation.VERTICAL);
        }else {
            layoutManager = new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, numColumns,numRows);
        }

        twoWayView.setLayoutManager(layoutManager);
        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(twoWayView);
        popupWindow.setTouchable(true);




        /*popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;

                if (onSpinnerItemListener != null) {
                    onSpinnerItemListener.onItemClick(SmartSpinner.this, view, position, id);
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }

                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }

                adapter.setSelectedIndex(position);

                setTextInternal(adapter.getItemInDataset(position));

                dismissDropDown();
            }
        });

        popupWindow.setModal(true);*/

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isArrowHidden) {
                    animateArrow(false);
                }
            }
        });

        measureLocation();


        measureDisplayHeight();

    }

    private void measureDisplayHeight() {
        displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;


        this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        getParentVerticalOffset();

        int measuredHeight = getMeasuredHeight();

        int i = displayHeight - getParentVerticalOffset() - measuredHeight;










        Log.i(">>>boylab>>", ">>>measureDisplayHeight: "+displayHeight);



    }

    public void measureLocation(){
        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);
        location[0] = locationOnScreen[0];
        location[1] = locationOnScreen[1];

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        measure(w, h);

        location[2] = getMeasuredWidth();
        location[3] = getMeasuredHeight();
    }


    private int getParentVerticalOffset() {

        return parentVerticalOffset = locationOnScreen[VERTICAL_OFFSET];
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
        if (arrowDrawableResId == 0) return null;
        Drawable drawable = ContextCompat.getDrawable(getContext(), arrowDrawableResId);
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
        context.getTheme()
                .resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data,
                new int[]{android.R.attr.textColorPrimary});
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
        arrowDrawableResId = drawableId;
        arrowDrawable = initArrowDrawable(R.drawable.smart_arrow);
        setArrowDrawableOrHide(arrowDrawable);
    }

    public void setArrowDrawable(Drawable drawable) {
        arrowDrawable = drawable;
        setArrowDrawableOrHide(arrowDrawable);
    }

    private void setTextInternal(Object item) {
        if (selectedTextFormatter != null) {
            setText(selectedTextFormatter.format(item));
        } else {
            setText(item.toString());
        }
    }

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
                setTextInternal(selectedTextFormatter.format(adapter.getItemInDataset(position)).toString());
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    /**
     * @deprecated use setOnSpinnerItemListener instead.
     */
    @Deprecated
    public void addOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * @deprecated use setOnSpinnerItemListener instead.
     */
    @Deprecated
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public <T> void attachDataSource(@NonNull List<String> list) {
        adapter = new SmartSpinnerAdapter(getContext(), list, textTint, backgroundSelector, itemGravity);
        setAdapterInternal(adapter);
    }

    public SpinnerItemGravity getPopUpTextAlignment() {
        return itemGravity;
    }

    private <T> void setAdapterInternal(LayoutAdapter adapter) {
        if (adapter.getItemCount() >= 0) {
            // If the adapter needs to be set again, ensure to reset the selected index as well
            selectedIndex = 0;
            //popupWindow.setAdapter(adapter);

            twoWayView.setAdapter(adapter);
            popupWindow.setContentView(twoWayView);

            setTextInternal(adapter.getItemInDataset(selectedIndex));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.i(">>>boylab>>11", ">>>onTouchEvent: "+isEnabled());
        Log.i(">>>boylab>>11", ">>>onTouchEvent: "+(event.getAction() == MotionEvent.ACTION_UP));
        if (isEnabled() && event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(">>>boylab>>22", ">>>onTouchEvent: "+(!popupWindow.isShowing()));
            Log.i(">>>boylab>>22", ">>>onTouchEvent: "+(adapter.getItemCount()));
            if (!popupWindow.isShowing() && adapter.getItemCount() > 0) {
                Log.i(">>>boylab>>", ">>>onTouchEvent: show ");
                showDropDown();
            } else {
                dismissDropDown();
                Log.i(">>>boylab>>", ">>>onTouchEvent: dismiss ");
            }
        }
        return super.onTouchEvent(event);
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
        popupWindow.dismiss();
    }

    public void showDropDown() {
        if (!isArrowHidden) {
            animateArrow(true);
        }
        popupWindow.showAsDropDown(this,0,0);
        final TwoWayView wayView = (TwoWayView) popupWindow.getContentView();
        if(wayView != null) {
            wayView.setVerticalScrollBarEnabled(false);
            wayView.setHorizontalScrollBarEnabled(false);
            wayView.setVerticalFadingEdgeEnabled(false);
            wayView.setHorizontalFadingEdgeEnabled(false);
        }
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


    public void performItemClick( int position,boolean showDropdown) {
        if(showDropdown) showDropDown();
        setSelectedIndex(position);
    }

    /**
     * only applicable when popup is shown .
     * @param view
     * @param position
     * @param id
     */
    public void performItemClick(View view, int position, int id) {
        showDropDown();
        final TwoWayView listView = (TwoWayView) popupWindow.getContentView();
        if(listView != null) {
            //listView.performItemClick(view, position, id);
        }
    }

    public OnSpinnerItemListener getOnSpinnerItemListener() {
        return onSpinnerItemListener;
    }

    public void setOnSpinnerItemListener(OnSpinnerItemListener onSpinnerItemListener) {
        this.onSpinnerItemListener = onSpinnerItemListener;
    }


}

