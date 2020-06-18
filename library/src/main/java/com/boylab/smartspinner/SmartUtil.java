package com.boylab.smartspinner;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

/**
 * Created by pengle on 2020/06/18
 * Email: pengle609@163.com
 */
public class SmartUtil {

    private static final int MAX_LEVEL = 10000;
    private static final int VERTICAL_OFFSET = 1;

    protected static int getDefaultTextColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorPrimary});
        int defaultTextColor = typedArray.getColor(0, Color.BLACK);
        typedArray.recycle();
        return defaultTextColor;
    }

    protected static Drawable initArrowDrawable(Context context, int arrowDrawableRes, int drawableTint) {
        if (arrowDrawableRes == 0) return null;
        Drawable drawable = ContextCompat.getDrawable(context, arrowDrawableRes);
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable).mutate();
            if (drawableTint != Integer.MAX_VALUE && drawableTint != 0) {
                DrawableCompat.setTint(drawable, drawableTint);
            }
        }
        return drawable;
    }

    protected static void setArrowDrawableOrHide(AppCompatTextView textView, Drawable drawable, boolean isArrowHidden) {
        if (!isArrowHidden && drawable != null) {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    protected static void animateArrow( int arrowDrawable, boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : MAX_LEVEL;
        int end = shouldRotateUp ? MAX_LEVEL : 0;
        ObjectAnimator arrowAnimator = ObjectAnimator.ofInt(arrowDrawable, "level", start, end);
        arrowAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        arrowAnimator.start();
    }
}
