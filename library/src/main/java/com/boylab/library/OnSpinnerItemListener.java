package com.boylab.library;

import android.view.View;
import android.widget.AdapterView;

public interface OnSpinnerItemListener extends AdapterView.OnItemClickListener {

    void onItemClick(SmartSpinner parent, View view, int position, long id);
}
