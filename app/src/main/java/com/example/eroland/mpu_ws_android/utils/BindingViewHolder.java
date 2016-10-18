package com.example.eroland.mpu_ws_android.utils;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BindingViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final V binding;

    public BindingViewHolder(V binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}