package com.boylab.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
public class SmartSpinnerAdapter extends LayoutAdapter {

    private List<String> items;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public SimpleViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }

    public SmartSpinnerAdapter(Context mContext, List<String> items, int itemWidth, int itemHeight, int textColor, int itemDrawableRes,
                               SpinnerItemGravity itemGravity, int itemPaddingLeft, int itemPaddingTop, int itemPaddingRight, int itemPaddingBottom) {
        super(mContext, itemWidth, itemHeight,textColor, itemDrawableRes, itemGravity, itemPaddingLeft, itemPaddingTop, itemPaddingRight, itemPaddingBottom);
        this.items = items;
    }

    @Override
    public String getItemText(int position) {
        return items.get(position);
    }

    @Override
    public String getItemInDataset(int position) {
        if (!items.isEmpty()){
            return items.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    /*@Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public T getItemInDataset(int position) {
        if (!items.isEmpty()){
            return items.get(position);
        }
        return null;
    }*/
}