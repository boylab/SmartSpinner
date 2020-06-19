package com.boylab.smartspinner;

import android.content.res.TypedArray;
import android.graphics.Color;

import androidx.annotation.DrawableRes;

public class SpinnerItemAttrs {

    private int itemWidth = 100, itemHeight = 50, spacing = 6, defaultPadding = 12;

    private int horizontalGap = 10;

    private int verticalGap = 10;

    private int textColor = Color.BLACK;

    private @DrawableRes int itemDrawableResId;

    private SpinnerItemGravity itemGravity;

    private boolean itemChecked;

    private int itemPaddingTop, itemPaddingLeft, itemPaddingBottom, itemPaddingRight;

    public void parseTyped(TypedArray typedArray){
        itemWidth = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemWidth, 100);
        itemHeight = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemHeight, 50);

        itemPaddingTop = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingTop, 0);
        itemPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingLeft, 0);
        itemPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingBottom, 0);
        itemPaddingRight = typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_itemPaddingRight, 0);

        itemDrawableResId = typedArray.getResourceId(R.styleable.SmartSpinner_itemDrawable, R.drawable.item_text_selector);
        itemGravity = SpinnerItemGravity.fromId(typedArray.getInt(R.styleable.SmartSpinner_ItemGravity, SpinnerItemGravity.CENTER.ordinal()));
        itemChecked = typedArray.getBoolean(R.styleable.SmartSpinner_itemChecked, false);
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public int getDefaultPadding() {
        return defaultPadding;
    }

    public void setDefaultPadding(int defaultPadding) {
        this.defaultPadding = defaultPadding;
    }

    public int getHorizontalGap() {
        return horizontalGap;
    }

    public void setHorizontalGap(int horizontalGap) {
        this.horizontalGap = horizontalGap;
    }

    public int getVerticalGap() {
        return verticalGap;
    }

    public void setVerticalGap(int verticalGap) {
        this.verticalGap = verticalGap;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getItemDrawableResId() {
        return itemDrawableResId;
    }

    public void setItemDrawableResId(int itemDrawableResId) {
        this.itemDrawableResId = itemDrawableResId;
    }

    public SpinnerItemGravity getItemGravity() {
        return itemGravity;
    }

    public void setItemGravity(SpinnerItemGravity itemGravity) {
        this.itemGravity = itemGravity;
    }

    public boolean isItemChecked() {
        return itemChecked;
    }

    public void setItemChecked(boolean itemChecked) {
        this.itemChecked = itemChecked;
    }

    public int getItemPaddingTop() {
        return itemPaddingTop;
    }

    public void setItemPaddingTop(int itemPaddingTop) {
        this.itemPaddingTop = itemPaddingTop;
    }

    public int getItemPaddingLeft() {
        return itemPaddingLeft;
    }

    public void setItemPaddingLeft(int itemPaddingLeft) {
        this.itemPaddingLeft = itemPaddingLeft;
    }

    public int getItemPaddingBottom() {
        return itemPaddingBottom;
    }

    public void setItemPaddingBottom(int itemPaddingBottom) {
        this.itemPaddingBottom = itemPaddingBottom;
    }

    public int getItemPaddingRight() {
        return itemPaddingRight;
    }

    public void setItemPaddingRight(int itemPaddingRight) {
        this.itemPaddingRight = itemPaddingRight;
    }

    @Override
    public String toString() {
        return "SpinnerItemAttrs{" +
                "itemWidth=" + itemWidth +
                ", itemHeight=" + itemHeight +
                ", spacing=" + spacing +
                ", defaultPadding=" + defaultPadding +
                ", horizontalGap=" + horizontalGap +
                ", verticalGap=" + verticalGap +
                ", textColor=" + textColor +
                ", itemDrawableResId=" + itemDrawableResId +
                ", itemGravity=" + itemGravity +
                ", itemChecked=" + itemChecked +
                ", itemPaddingTop=" + itemPaddingTop +
                ", itemPaddingLeft=" + itemPaddingLeft +
                ", itemPaddingBottom=" + itemPaddingBottom +
                ", itemPaddingRight=" + itemPaddingRight +
                '}';
    }
}
