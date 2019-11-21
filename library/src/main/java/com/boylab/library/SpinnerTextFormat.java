package com.boylab.library;

import android.text.Spannable;
import android.text.SpannableString;

public class SpinnerTextFormat implements TextFormat<String> {

    @Override
    public Spannable format(String item) {
        return new SpannableString(item);
    }
}
