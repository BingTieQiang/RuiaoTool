package com.ruiao.tools.ic_card2;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ruiao.tools.R;
import com.ruiao.tools.notice.NoticeBean;

import java.util.ArrayList;

/**
 * Created by ruiao on 2018/5/10.
 */

public class IcAdapter1 extends RecyclerView.Adapter {
    private int arr = 0;//默认是0 显示cod cod流量 nh3n nh3n总量   1显示总磷 总磷流量  总氮 总氮流量

    private LayoutInflater mLayoutInflater;
    protected ArrayList<IcBean1> mDataList = new ArrayList<>();

    public IcAdapter1(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

    }
    public void notifyChange(int arr){
        this.arr = arr;
        notifyDataSetChanged();
    }
    public void setDate( ArrayList<IcBean1> beans  )
    {
        mDataList.clear();
        mDataList.addAll(beans);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_ic_data, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
            if(position%2==0){
                viewHolder.ll.setBackgroundColor(Color.WHITE);
            }else {
                viewHolder.ll.setBackgroundColor(Color.GRAY);
            }
            viewHolder.tv1.setText(""+mDataList.get(position).nom);
            viewHolder.tv2.setText(mDataList.get(position).data);


            if(0 == arr){
                viewHolder.tv3.setText(mDataList.get(position).cod);
                viewHolder.tv4.setText(mDataList.get(position).cods);
                viewHolder.tv5.setText(mDataList.get(position).nh3n);
                viewHolder.tv6.setText(mDataList.get(position).nh3ns);
                viewHolder.total.setText(mDataList.get(position).paishuiliang);

            }else{
                viewHolder.tv3.setText(mDataList.get(position).lin);
                viewHolder.tv4.setText(mDataList.get(position).linpaifan);
                viewHolder.tv5.setText(mDataList.get(position).dan);
                viewHolder.tv6.setText(mDataList.get(position).danpaifang);
                viewHolder.total.setText(mDataList.get(position).total);

            }



    }

    @Override
    public int getItemCount() {
        return mDataList.size();

    }



    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;
        private TextView total;
        private LinearLayout ll;

//        private FoldingCell foldingCell;
        public ViewHolder(View itemView) {
            super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv_ic_1);
                tv2 = (TextView) itemView.findViewById(R.id.tv_ic_2);
                tv3 = (TextView) itemView.findViewById(R.id.tv_ic_3);
                tv4 = (TextView) itemView.findViewById(R.id.tv_ic_4);
                tv5 = (TextView) itemView.findViewById(R.id.tv_ic_5);
                tv6 = (TextView) itemView.findViewById(R.id.tv_ic_6);
            total = (TextView) itemView.findViewById(R.id.tv_total);
                ll = (LinearLayout) itemView.findViewById(R.id.ll_bac);
//                context = (TextView) itemView.findViewById(R.id.tv_message_context);
//            foldingCell = (FoldingCell) itemView.findViewById(R.id.folding_cell);
        }
    }


}
