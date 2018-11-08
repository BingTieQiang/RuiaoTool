package com.ruiao.tools.ui.adapter.lv;

import android.content.Context;

import com.ruiao.tools.ui.adapter.lv.base.ItemViewDelegate;

import java.util.List;

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {


    public CommonAdapter(Context context, List<T> mDatas, final int layoutId) {
        super(context, mDatas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

}
