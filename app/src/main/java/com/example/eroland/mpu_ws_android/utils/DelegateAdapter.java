package com.example.eroland.mpu_ws_android.utils;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.eroland.mpu_ws_android.R;

public abstract class DelegateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SparseArray<ViewDelegate<? extends RecyclerView.ViewHolder>> delegates = new SparseArray<>();

    public <T extends RecyclerView.ViewHolder> void registerDelegate(ViewDelegate<T> delegate) {
        int viewType = delegate.getItemViewType();

        delegates.put(viewType, delegate);
    }

    private ViewDelegate<? extends RecyclerView.ViewHolder> getDelegateByViewType(int viewType) {
        return delegates.get(viewType);
    }

    private ViewDelegate<? extends RecyclerView.ViewHolder> getDelegateByViewHolder(RecyclerView.ViewHolder viewHolder) {
        return (ViewDelegate<? extends RecyclerView.ViewHolder>) viewHolder.itemView.getTag(R.id.delegate);
    }

    private ViewDelegate<? extends RecyclerView.ViewHolder> getDelegateByPosition(int position) {
        Object item = getItem(position);

        for (int i = 0, size = delegates.size(); i < size; i++) {
            ViewDelegate<? extends RecyclerView.ViewHolder> delegate = delegates.valueAt(i);

            if (delegate.canHandleItemTypeAtPosition(item, position)) {
                return delegate;
            }
        }

        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewDelegate<? extends RecyclerView.ViewHolder> delegate = getDelegateByViewType(viewType);
        RecyclerView.ViewHolder viewHolder = delegate.onCreateViewHolder(viewGroup);
        viewHolder.itemView.setTag(R.id.delegate, delegate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewDelegate delegate = getDelegateByViewHolder(viewHolder);
        //noinspection unchecked
        delegate.onBindViewHolder(viewHolder, getItem(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return getDelegateByPosition(position).getItemViewType();
    }

    @Override
    public long getItemId(int position) {
        return getDelegateByPosition(position).getItemId(getItem(position), position);
    }

    public abstract Object getItem(int position);
}

