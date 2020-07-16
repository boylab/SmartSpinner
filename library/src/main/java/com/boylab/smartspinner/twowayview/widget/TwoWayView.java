/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boylab.smartspinner.twowayview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.boylab.smartspinner.R;

import com.boylab.smartspinner.twowayview.TwoWayLayoutManager;

import java.lang.reflect.Constructor;

public class TwoWayView extends RecyclerView {
    private static final String LOGTAG = "TwoWayView";

    private static final Class<?>[] sConstructorSignature = new Class[] {
            Context.class, AttributeSet.class};

    final Object[] sConstructorArgs = new Object[2];

    public TwoWayView(Context context) {
        this(context, null);
    }

    public TwoWayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoWayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.twowayview_TwoWayView, defStyle, 0);

        final String name = a.getString(R.styleable.twowayview_TwoWayView_twowayview_layoutManager);
        if (!TextUtils.isEmpty(name)) {
            loadLayoutManagerFromName(context, attrs, name);
        }

        a.recycle();
    }

    private void loadLayoutManagerFromName(Context context, AttributeSet attrs, String name) {
        try {
            final int dotIndex = name.indexOf('.');
            if (dotIndex == -1) {
                name = "com.boylab.library.twowayview.widget." + name;
            } else if (dotIndex == 0) {
                final String packageName = context.getPackageName();
                name = packageName + "." + name;
            }

            Class<? extends TwoWayLayoutManager> clazz =
                    context.getClassLoader().loadClass(name).asSubclass(TwoWayLayoutManager.class);

            Constructor<? extends TwoWayLayoutManager> constructor =
                    clazz.getConstructor(sConstructorSignature);

            sConstructorArgs[0] = context;
            sConstructorArgs[1] = attrs;

            setLayoutManager(constructor.newInstance(sConstructorArgs));
        } catch (Exception e) {
            throw new IllegalStateException("Could not load TwoWayLayoutManager from " +
                                             "class: " + name, e);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof TwoWayLayoutManager)) {
            throw new IllegalArgumentException("TwoWayView can only use TwoWayLayoutManager " +
                                                "subclasses as its layout manager");
        }

        super.setLayoutManager(layout);
    }

    public TwoWayLayoutManager.Orientation getOrientation() {
        TwoWayLayoutManager layout = (TwoWayLayoutManager) getLayoutManager();
        return layout.getOrientation();
    }

    public void setOrientation(TwoWayLayoutManager.Orientation orientation) {
        TwoWayLayoutManager layout = (TwoWayLayoutManager) getLayoutManager();
        layout.setOrientation(orientation);
    }

    public int getFirstVisiblePosition() {
        TwoWayLayoutManager layout = (TwoWayLayoutManager) getLayoutManager();
        return layout.getFirstVisiblePosition();
    }

    public int getLastVisiblePosition() {
        TwoWayLayoutManager layout = (TwoWayLayoutManager) getLayoutManager();
        return layout.getLastVisiblePosition();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        /*TwoWayLayoutManager layout = (TwoWayLayoutManager) getLayoutManager();
        super.onMeasure(widthSpec, heightSpec);*/

        //Log.i(">>>boylab>>11", ">>>onClick: widthSpec ="+widthSpec+" >>>>> heightSpec ="+heightSpec);
        super.onMeasure(widthSpec, heightSpec);

        /*if (mLayout == null) {
            defaultOnMeasure(widthSpec, heightSpec);
            return;
        }
        if (mLayout.mAutoMeasure) {     //这里为true会进入该分支
            final int widthMode = MeasureSpec.getMode(widthSpec);
            final int heightMode = MeasureSpec.getMode(heightSpec);
            final boolean skipMeasure = widthMode == MeasureSpec.EXACTLY
                    && heightMode == MeasureSpec.EXACTLY;
            mLayout.onMeasure(mRecycler, mState, widthSpec, heightSpec);   //在调用mLayout的onMeasure方法时（被自定义复写的方法），mState.mItemCount为0，造成越界异常
            if (skipMeasure || mAdapter == null) {
                return;
            }
            if (mState.mLayoutStep == State.STEP_START) {
                dispatchLayoutStep1();
            }
            // set dimensions in 2nd step. Pre-layout should happen with old dimensions for
            // consistency
            mLayout.setMeasureSpecs(widthSpec, heightSpec);
            mState.mIsMeasuring = true;
            dispatchLayoutStep2();

            // now we can get the width and height from the children.
            mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);

            // if RecyclerView has non-exact width and height and if there is at least one child
            // which also has non-exact width & height, we have to re-measure.
            if (mLayout.shouldMeasureTwice()) {
                mLayout.setMeasureSpecs(
                        MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
                mState.mIsMeasuring = true;
                dispatchLayoutStep2();
                // now we can get the width and height from the children.
                mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
            }
        } else {
            if (mHasFixedSize) {   //这里不设置为false会造成与上面相同的问题
                mLayout.onMeasure(mRecycler, mState, widthSpec, heightSpec);
                return;
            }
            // custom onMeasure
            if (mAdapterUpdateDuringMeasure) {
                eatRequestLayout();
                processAdapterUpdatesAndSetAnimationFlags();

                if (mState.mRunPredictiveAnimations) {
                    mState.mInPreLayout = true;
                } else {
                    // consume remaining updates to provide a consistent state with the layout pass.
                    mAdapterHelper.consumeUpdatesInOnePass();
                    mState.mInPreLayout = false;
                }
                mAdapterUpdateDuringMeasure = false;
                resumeRequestLayout(false);
            }

            if (mAdapter != null) {
                mState.mItemCount = mAdapter.getItemCount();
            } else {
                mState.mItemCount = 0;
            }
            eatRequestLayout();
            mLayout.onMeasure(mRecycler, mState, widthSpec, heightSpec);   //在这里调用时mState.mItemCount才会有值，这与mLayout中获取当前item布局的方式有关：View view = recycler.getViewForPosition(0);
            resumeRequestLayout(false);
            mState.mInPreLayout = false; // clear
        }*/
    }
}
