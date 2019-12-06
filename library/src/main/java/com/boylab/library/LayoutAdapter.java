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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {

    private Context mContext;
    private final SpinnerItemGravity itemGravity;

    private int textColor;
    private int backgroundSelector;

    int selectedIndex;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public SimpleViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.text_view_spinner);
        }
    }

    public LayoutAdapter(Context context, int textColor, int backgroundSelector, SpinnerItemGravity itemGravity) {
        this.mContext = context;
        this.backgroundSelector = backgroundSelector;
        this.textColor = textColor;
        this.itemGravity = itemGravity;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item, parent, false);
        return new SimpleViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // TODO: 2019/12/5 图片设置失败
            holder.title.setBackground(ContextCompat.getDrawable(mContext, backgroundSelector));
        }
        holder.title.setText(getItemText(position));
        holder.title.setTextColor(textColor);

        setTextHorizontalAlignment(holder.title);
    }


    public abstract String getItemText(int position);

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public abstract String getItemInDataset(int position);

    private void setTextHorizontalAlignment(TextView textView) {
        switch (itemGravity) {
            case START:
                textView.setGravity(Gravity.START);
                break;
            case END:
                textView.setGravity(Gravity.END);
                break;
            case CENTER:
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
        }
    }
}
