package com.boylab.library;

import android.text.Spannable;

public interface TextFormat<T> {

    Spannable format(T item);
}
