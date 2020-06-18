package com.boylab.smartspinner;

import android.view.Gravity;

enum SpinnerItemGravity {
    START(0, Gravity.LEFT | Gravity.CENTER_VERTICAL),
    END(1, Gravity.END | Gravity.CENTER_VERTICAL),
    CENTER(2, Gravity.CENTER);

    private final int id;
    private int gravity;

    SpinnerItemGravity(int id, int gravity) {
        this.id = id;
        this.gravity = gravity;
    }

    public int getId() {
        return id;
    }

    public int getGravity() {
        return gravity;
    }

    static SpinnerItemGravity fromId(int id) {
        for (SpinnerItemGravity value : values()) {
            if (value.id == id) return value;
        }
        return CENTER;
    }
}