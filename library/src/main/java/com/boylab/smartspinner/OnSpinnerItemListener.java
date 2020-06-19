package com.boylab.smartspinner;

import android.view.View;
import android.widget.AdapterView;

public interface OnSpinnerItemListener {

    void onItemClick(SmartSpinner parent, View view, int position, long id);
}
