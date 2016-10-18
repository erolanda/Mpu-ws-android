package com.example.eroland.mpu_ws_android.ui;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.eroland.mpu_ws_android.Constants;
import com.example.eroland.mpu_ws_android.R;
import com.example.eroland.mpu_ws_android.databinding.DelegateLineChartBinding;
import com.example.eroland.mpu_ws_android.utils.BindingViewHolder;
import com.example.eroland.mpu_ws_android.utils.ViewDelegate;
import com.github.mikephil.charting.data.LineData;

public class DelegateLineChart extends ViewDelegate<BindingViewHolder<DelegateLineChartBinding>> {
    @Override
    public int getItemViewType() {
        return Constants.LINE_CHART;
    }

    @Override
    public BindingViewHolder<DelegateLineChartBinding> onCreateViewHolder(ViewGroup viewGroup) {
        DelegateLineChartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.delegate_line_chart, viewGroup, false);

        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<DelegateLineChartBinding> viewHolder,
                                 Object item, int position) {
        viewHolder.binding.setModel((LineData) item);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public boolean canHandleItemTypeAtPosition(Object item, int position) {
        return item instanceof LineData;
    }
}
