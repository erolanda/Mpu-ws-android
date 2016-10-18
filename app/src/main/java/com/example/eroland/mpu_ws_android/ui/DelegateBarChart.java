package com.example.eroland.mpu_ws_android.ui;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.eroland.mpu_ws_android.Constants;
import com.example.eroland.mpu_ws_android.R;
import com.example.eroland.mpu_ws_android.databinding.DelegateBarChartBinding;
import com.example.eroland.mpu_ws_android.utils.BindingViewHolder;
import com.example.eroland.mpu_ws_android.utils.ViewDelegate;
import com.github.mikephil.charting.data.BarData;

public class DelegateBarChart extends ViewDelegate<BindingViewHolder<DelegateBarChartBinding>> {
    @Override
    public int getItemViewType() {
        return Constants.BAR_CHART;
    }

    @Override
    public BindingViewHolder<DelegateBarChartBinding> onCreateViewHolder(ViewGroup viewGroup) {
        DelegateBarChartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.delegate_bar_chart, viewGroup, false);

        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<DelegateBarChartBinding> viewHolder,
                                 Object item, int position) {
        viewHolder.binding.setModel((BarData) item);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public boolean canHandleItemTypeAtPosition(Object item, int position) {
        return item instanceof BarData;
    }
}
