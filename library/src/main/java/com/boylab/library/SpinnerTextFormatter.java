package com.boylab.library;

import android.text.Spannable;

public interface SpinnerTextFormatter<T> {

    Spannable format(T item);
}
