package com.ruiao.tools.ui.adapter.lv.base;


import com.ruiao.tools.ui.adapter.lv.ViewHolder;

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
}
