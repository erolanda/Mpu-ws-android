package com.example.eroland.mpu_ws_android.databinding;

import android.databinding.BindingAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

public final class ChartAdapter {
    private ChartAdapter() {
    }

    @BindingAdapter("data")
    public static void setData(LineChart view, LineData data) {
        if (data == null) {
            view.setData(null);
        } else {
            view.setData(data);
        }
    }

    @BindingAdapter("data")
    public static void setData(BarChart view, BarData data) {
        if (data == null) {
            view.setData(null);
        } else {
            view.setData(data);
        }
    }
}
