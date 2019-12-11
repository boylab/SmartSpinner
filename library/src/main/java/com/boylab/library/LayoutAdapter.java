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

package com.boylab.library;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {

    private Context mContext;

    private int itemWidth;
    private int itemHeight;
    private int textColor;
    private int itemDrawableRes;
    private final SpinnerItemGravity itemGravity;

    private int itemPaddingTop, itemPaddingLeft, itemPaddingBottom, itemPaddingRight;


    private boolean itemChecked;

    private int selectedIndex;
    private static AdapterView.OnItemSelectedListener onItemSelectedListener;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemText;

        public SimpleViewHolder(final View view) {
            super(view);
            itemText = view.findViewById(R.id.text_view_spinner);
        }
    }

    public LayoutAdapter(Context mContext, int itemWidth, int itemHeight, int textColor, int itemDrawableRes, SpinnerItemGravity itemGravity,
                         int itemPaddingLeft, int itemPaddingTop, int itemPaddingRight, int itemPaddingBottom) {
        this.mContext = mContext;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.textColor = textColor;
        this.itemDrawableRes = itemDrawableRes;
        this.itemGravity = itemGravity;
        this.itemPaddingTop = itemPaddingTop;
        this.itemPaddingLeft = itemPaddingLeft;
        this.itemPaddingBottom = itemPaddingBottom;
        this.itemPaddingRight = itemPaddingRight;
    }

    /*public LayoutAdapter(Context context, int textColor, int itemDrawableRes, SpinnerItemGravity itemGravity) {
        this.mContext = context;
        this.itemDrawableRes = itemDrawableRes;
        this.textColor = textColor;
        this.itemGravity = itemGravity;
    }*/

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item, parent, false);
        SimpleViewHolder viewHolder = new SimpleViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {

        holder.itemText.setWidth(itemWidth);
        holder.itemText.setHeight(itemHeight);
        holder.itemText.setTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.itemText.setBackground(ContextCompat.getDrawable(mContext, itemDrawableRes));
        }
        holder.itemText.setGravity(itemGravity.getGravity());
        holder.itemText.setPadding(itemPaddingLeft, itemPaddingTop, itemPaddingRight, itemPaddingBottom);

        holder.itemText.setText(getItemText(position));
        holder.itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedIndex(position);
                if (onItemSelectedListener != null){
                    onItemSelectedListener.onItemSelected(null, holder.itemText, position, v.getId());
                }
            }
        });

    }

    protected abstract String getItemText(int position);

    protected void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    protected abstract String getItemInDataset(int position);

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
