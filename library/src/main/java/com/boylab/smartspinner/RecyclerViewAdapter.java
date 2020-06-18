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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SimpleViewHolder> {

    private int selectedIndex;

    private Context mContext;
    private List<String> list = new ArrayList<>();

    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemText;

        public SimpleViewHolder(final View view) {
            super(view);
            itemText = view.findViewById(R.id.text_view_spinner);
        }
    }

    public RecyclerViewAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item01, parent, false);
        SimpleViewHolder viewHolder = new SimpleViewHolder(convertView);
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
