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

package com.boylab.smartspinner;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SmartViewAdapter extends RecyclerView.Adapter<SmartViewAdapter.SimpleViewHolder> {

    private Context mContext;
    private SpinnerItemAttrs itemAttrs;
    private List<String> list = new ArrayList<>();

    private int selectedIndex;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemText;

        public SimpleViewHolder(final View view, SpinnerItemAttrs itemAttrs) {
            super(view);
            itemText = view.findViewById(R.id.text_view_spinner);

            itemText.setWidth(itemAttrs.getItemWidth());
            itemText.setHeight(itemAttrs.getItemHeight());
            itemText.setTextColor(itemAttrs.getTextColor());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemText.setBackground(ContextCompat.getDrawable(view.getContext(), itemAttrs.getItemDrawableResId()));
            }
            itemText.setGravity(itemAttrs.getItemGravity().getGravity());
            itemText.setPadding(itemAttrs.getItemPaddingLeft(), itemAttrs.getItemPaddingTop(), itemAttrs.getItemPaddingRight(), itemAttrs.getItemPaddingBottom());

        }
    }

    public SmartViewAdapter(Context mContext, SpinnerItemAttrs itemAttrs, List<String> list) {
        this.mContext = mContext;
        this.itemAttrs = itemAttrs;
        this.list = list;
        Log.i(">>>boylab>>", ">>>SmartViewAdapter: "+itemAttrs.toString());
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item, parent, false);
        SimpleViewHolder viewHolder = new SimpleViewHolder(convertView, itemAttrs);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        holder.itemText.setText(list.get(position));
        holder.itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectedListener != null){
                    onItemSelectedListener.onItemSelected(null, v, position, v.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getSelectedValue() {
        if (selectedIndex >= 0 && selectedIndex <= getItemCount()) {
            return list.get(selectedIndex);
        }
        return list.get(0);

    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
