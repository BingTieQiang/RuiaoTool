package com.ruiao.tools.ic_card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiao.tools.R;

import java.util.ArrayList;

/**
 * Created by ruiao on 2018/5/7.
 */

public class RecAdapter extends RecyclerView.Adapter{
    private LayoutInflater mLayoutInflater;
    protected Context mContext;
    private ArrayList<Double> list;
    public RecAdapter(Context mContext, ArrayList<Double> list) {
        this.mContext = mContext;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_line, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.num.setText(""+list.get(position));
        ViewGroup.LayoutParams layoutParams = viewHolder.ll.getLayoutParams();
        layoutParams.height = list.get(position).intValue();
        viewHolder.ll.setLayoutParams(layoutParams);


    }

    /**
     * 设置数据，自动更新
     *  输入double数组，
     */
    public void setData(ArrayList<Double> list)
    {
        list.clear();
        list.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView num;
        private LinearLayout ll;
        private TextView data;
        public ViewHolder(View itemView) {
            super(itemView);

            num = (TextView) itemView.findViewById(R.id.tv_num);
            data = (TextView) itemView.findViewById(R.id.tv_data);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_line);
        }
    }
}
