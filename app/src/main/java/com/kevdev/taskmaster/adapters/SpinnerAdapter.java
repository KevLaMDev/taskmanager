package com.kevdev.taskmaster.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

public interface SpinnerAdapter extends Adapter {
    View getDropDownView(int var1, View var2, ViewGroup var3);
}
