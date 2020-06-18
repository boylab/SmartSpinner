package com.boylab.smartspinner;

import android.text.Spannable;

public interface TextFormat<T> {

    Spannable format(T item);
}
