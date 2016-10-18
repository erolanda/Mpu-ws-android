package com.example.eroland.mpu_ws_android.utils;

import android.support.annotation.NonNull;

import java.util.List;

public class ListDelegateAdapter extends DelegateAdapter {
    private List<?> list;

    public ListDelegateAdapter(@NonNull List<?> list) {
        this.list = list;
    }

    public ListDelegateAdapter() {
    }

    public void setData(List<?> list) {
        this.list = list;

        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
