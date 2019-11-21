package com.boylab.library;

enum SpinnerItemGravity {
    START(0),
    END(1),
    CENTER(2);

    private final int id;

    SpinnerItemGravity(int id) {
        this.id = id;
    }

    static SpinnerItemGravity fromId(int id) {
        for (SpinnerItemGravity value : values()) {
            if (value.id == id) return value;
        }
        return CENTER;
    }
}