package com.boylab.smartspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

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

    private @DrawableRes int textDrawableRes;
    private int textTint;

    private boolean isArrowHidden;
    private int arrowTint;
    private @DrawableRes int arrowDrawableRes;

    private int numColumns = 1, numRows;

    private SpinnerItemAttrs itemAttrs = new SpinnerItemAttrs();
    private List<CharSequence> charSequences;

    private PopupWindow popupWindow;
    private ViewGroup rootView;
    private TwoWayView twoWayView;
    private TwoWayLayoutManager layoutManager;
    public SmartViewAdapter adapter;

    private OnSpinnerItemListener onSpinnerItemListener;

    private int[] location = new int[4];
    private int displayHeight;
    private int parentVerticalOffset;

    public SmartSpinner(Context context) {
        super(context);
        initView();
    }

    public SmartSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseRes(context, attrs);

        initView();
    }

    public SmartSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        parseRes(context, attrs);

        initView();
    }

    public void setOnSpinnerItemListener(OnSpinnerItemListener onSpinnerItemListener) {
        this.onSpinnerItemListener = onSpinnerItemListener;
    }

    private void parseRes(Context context, AttributeSet attrs) {

        int defaultPadding = getResources().getDimensionPixelSize(R.dimen.one_and_a_half_grid_unit);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartSpinner);
        textDrawableRes = typedArray.getResourceId(R.styleable.SmartSpinner_textDrawable, R.drawable.item_text_selector);
        setBackgroundResource(textDrawableRes);
        setMaxLines(1);

        isArrowHidden = typedArray.getBoolean(R.styleable.SmartSpinner_arrowHide, false);
        arrowTint = typedArray.getColor(R.styleable.SmartSpinner_arrowTint, getResources().getColor(android.R.color.black));
        arrowDrawableRes = typedArray.getResourceId(R.styleable.SmartSpinner_arrowDrawable, R.drawable.smart_arrow_default);

        numColumns = typedArray.getInt(R.styleable.SmartSpinner_numColumns, 2);
        numRows = typedArray.getInt(R.styleable.SmartSpinner_numRows, 1);
        itemAttrs.parseTyped(typedArray);

        // TODO: 2019/12/4 need to use
        CharSequence[] entries = typedArray.getTextArray(R.styleable.SmartSpinner_entries);
        charSequences = ((entries == null) ? new ArrayList<CharSequence>() : Arrays.asList(entries));

        typedArray.recycle();
    }

    private void initView() {
        setClickable(true);
        setWidth(itemAttrs.getItemWidth());
        setHeight(itemAttrs.getItemHeight());
        setPadding(itemAttrs.getItemPaddingLeft(), itemAttrs.getItemPaddingTop(), itemAttrs.getItemPaddingRight(), itemAttrs.getItemPaddingBottom());

        /*SmartUtil.initArrowDrawable(getContext(), arrowDrawableRes, );
        arrowDrawable = initArrowDrawable(arrowTint);*/

        rootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.spinner_popupview, null);
        rootView.setBackgroundResource(R.drawable.spinner_drawable);

        twoWayView = rootView.findViewById(R.id.spinner_TwoWayView);
        twoWayView.setHasFixedSize(true);
        twoWayView.setLongClickable(true);
    }

    private void initPopupWindow(){

        final Drawable divider = getResources().getDrawable(R.drawable.spinner_divider);
        int dividerWidth = divider.getIntrinsicWidth();
        int dividerHeight = divider.getIntrinsicHeight();

        twoWayView.addItemDecoration(new DividerItemDecoration(divider));
        if (numColumns == 1) {
            layoutManager = new ListLayoutManager(getContext(), TwoWayLayoutManager.Orientation.VERTICAL);
        } else {
            layoutManager = new GridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, numColumns, numRows);
        }
        twoWayView.setLayoutManager(layoutManager);

        popupWindow = new PopupWindow(rootView);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.smart_arrow_default);
        popupWindow.setBackgroundDrawable(drawable);

        measureDisplayLocation();
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

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

    public void attachDataSource(@NonNull List<String> list) {
        if (list.isEmpty()){
            return;
        }
        setText(list.get(0));
        initPopupWindow();

        freshPopupWindow(list.size());

        adapter = new SmartViewAdapter(getContext(), itemAttrs, list);
        twoWayView.setAdapter(adapter);

        adapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (onSpinnerItemListener != null) {
                    onSpinnerItemListener.onItemClick(SmartSpinner.this, view, position, id);
                }

                adapter.setSelectedIndex(position);
                setText(adapter.getSelectedValue());
                dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (adapter == null){
            return super.onTouchEvent(event);
        }else if (adapter.getItemCount() <= 0){
            return super.onTouchEvent(event);
        }
        if (isEnabled() && event.getAction() == MotionEvent.ACTION_UP) {
            if (popupWindow.isShowing()) {
                dismiss();
            } else {
                showPopupWindow(this);
            }
        }
        return super.onTouchEvent(event);
    }

    public void showPopupWindow(View anchor){
        popupWindow.showAsDropDown(anchor, 0, 10);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public void freshPopupWindow(double childSize) {
        if (childSize == 0) {
            childSize = this.charSequences.size();
        }
        numRows = (int) Math.ceil(childSize / numColumns);

        int spacing = itemAttrs.getSpacing();
        int defaultPadding = itemAttrs.getDefaultPadding();
        int popupWidth = numColumns * (itemAttrs.getItemWidth() + spacing) - spacing + defaultPadding * 2;
        int popupHeight = numRows * (itemAttrs.getItemHeight() + spacing) - spacing + defaultPadding * 2;

        popupWindow.setWidth(popupWidth);
        popupWindow.setHeight(popupHeight);
    }

    public int getSelectedItemPosition(){
        return adapter.getSelectedIndex();
    }

    public void setSelection(int position){
        if (adapter == null){
            return;
        }
        adapter.setSelectedIndex(position);
        setText(adapter.getSelectedValue());
    }

    public int getTextDrawableRes() {
        return textDrawableRes;
    }

    public void setTextDrawableRes(int textDrawableRes) {
        this.textDrawableRes = textDrawableRes;
    }

    public int getTextTint() {
        return textTint;
    }

    public void setTextTint(int textTint) {
        this.textTint = textTint;
    }

    public boolean isArrowHidden() {
        return isArrowHidden;
    }

    public void setArrowHidden(boolean arrowHidden) {
        isArrowHidden = arrowHidden;
    }

    public int getArrowTint() {
        return arrowTint;
    }

    public void setArrowTint(int arrowTint) {
        this.arrowTint = arrowTint;
    }

    public int getArrowDrawableRes() {
        return arrowDrawableRes;
    }

    public void setArrowDrawableRes(int arrowDrawableRes) {
        this.arrowDrawableRes = arrowDrawableRes;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public SpinnerItemAttrs getItemAttrs() {
        return itemAttrs;
    }

    public void setItemAttrs(SpinnerItemAttrs itemAttrs) {
        this.itemAttrs = itemAttrs;
    }

    public List<CharSequence> getCharSequences() {
        return charSequences;
    }

    public void setCharSequences(List<CharSequence> charSequences) {
        this.charSequences = charSequences;
    }
}
