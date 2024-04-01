package com.example.frontend;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

class InputFilterMinMax implements InputFilter {
    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public void InputFilter(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);

    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        try {
            String newVal = spanned.toString().substring(0, i2) + charSequence.toString() + spanned.toString().substring(i3);

            int input = Integer.parseInt(newVal);
            if (isInRange(min, max, input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            Log.e("InputFilter: NumberFormatError", "Input number is wrong");
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}