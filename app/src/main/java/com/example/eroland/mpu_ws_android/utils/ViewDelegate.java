package com.example.eroland.mpu_ws_android.utils;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class ViewDelegate<V extends RecyclerView.ViewHolder> {
    public int getItemViewType() {
        return 0;
    }

    public long getItemId(Object item, int position) {
        return -1L;
    }

    public abstract V onCreateViewHolder(ViewGroup viewGroup);

    public abstract void onBindViewHolder(V viewHolder, Object item, int position);

    public abstract boolean canHandleItemTypeAtPosition(Object item, int position);
}