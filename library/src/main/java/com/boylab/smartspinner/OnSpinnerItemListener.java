package com.boylab.smartspinner;

import android.view.View;
import android.widget.AdapterView;

public interface OnSpinnerItemListener extends AdapterView.OnItemClickListener {

    void onItemClick(SmartSpinner2 parent, View view, int position, long id);
}
