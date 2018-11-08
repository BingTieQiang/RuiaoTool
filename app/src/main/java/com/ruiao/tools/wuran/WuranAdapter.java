package com.ruiao.tools.wuran;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiao.tools.R;

import java.util.ArrayList;

/**
 * Created by ruiao on 2018/5/31.
 */

public class WuranAdapter extends RecyclerView.Adapter{
    private String[] qifen = {"流量(mg/m3)","烟尘(mg/m3)","烟尘折算(mg/m3)","SO2(mg/m3)","SO2折算(mg/m3)","氮氧化物(mg/m3)","氮氧化物折算(mg/m3)"}; //气体分钟 1
    private String[] shuifen = {"流量(L/s)","COD(mg/L)","氨氮(mg/L)","总磷(mg/L)","总氮(mg/L)"};                       //水分钟  2
    private String[] qihour = {"烟气流量平均值(m3)","烟尘平均值(mg/m3)","烟尘折算平均值(mg/m3)","SO2平均(mg/m3)","SO2折算平均值(mg/m3)","氮氧化物平均(mg/m3)","氮氧化物折算平均值(mg/m3)"};  //气体非分钟  11
    private String[] shuihour = {"流量平均值(L/s)","氨氮平均值(mg/L)","COD平均值(mg/L)","总氮平均值(mg/L)","总磷平均值(mg/L)"};  //水非分钟  21
    private boolean selectData = true;
//    private TextView id;
    private TextView time;
    private TextView arr1;
    private TextView arr2;
    private TextView arr3;
    private TextView arr4;
//    private TextView arr5;

    public WuranAdapter(Context context, TextView time, TextView arr1, TextView arr2, TextView arr3, TextView arr4) {
//        this.id = id;
        this.time = time;
        this.arr1 = arr1;
        this.arr2 = arr2;
        this.arr3 = arr3;
        this.arr4 = arr4;
//        this.arr5 = arr5;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    private ArrayList<WuranBean> list = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context context;

    public WuranAdapter(Context context) {
//        this.list = list;
        this.context = context;

    }

    /**
     * 切换数据
     */
    public void switchData(){
        if(list.size() == 0){
            return;
        }
        selectData = !selectData;

        switch (list.get(0).devtype){
            case 1:        //气体分钟
                if(selectData){
                    arr1.setText(qifen[0]);  //流量
                    arr2.setText(qifen[1]);
                    arr3.setText(qifen[2]);
                    arr4.setText(qifen[3]);

                }else {
                    arr1.setText(qifen[0]);
                    arr2.setText(qifen[4]);
                    arr3.setText(qifen[5]);
                    arr4.setText(qifen[6]);

                }

                break;
            case 2:  //水分钟
                if(selectData){
                    arr1.setText(shuifen[0]);
                    arr2.setText(shuifen[1]);
                    arr3.setText(shuifen[2]);
                    arr4.setText(shuifen[3]);

                }else {
                    arr1.setText(shuifen[0]);
                    arr2.setText(shuifen[1]);
                    arr3.setText(shuifen[2]);
                    arr4.setText(shuifen[4]);
                }
                break;
            case 11:  //气体分钟
                if(selectData){
                    arr1.setText(qihour[0]);
                    arr2.setText(qihour[1]);
                    arr3.setText(qihour[2]);
                    arr4.setText(qihour[3]);

                }else {
                    arr1.setText(qihour[0]);
                    arr2.setText(qihour[4]);
                    arr3.setText(qihour[5]);
                    arr4.setText(qihour[6]);

                }
                break;

            case 21:  //水非分钟
                if(selectData){
                    arr1.setText(shuihour[0]);
                    arr2.setText(shuihour[1]);
                    arr3.setText(shuihour[2]);
                    arr4.setText(shuihour[3]);
                }else {
                    arr1.setText(shuihour[3]);
                    arr2.setText(shuihour[4]);
                    arr3.setText(shuihour[5]);
                    arr4.setText(shuihour[6]);
                }
                break;
        }
        notifyDataSetChanged();
    }

    public void setDate( ArrayList<WuranBean> beans  )
    {
        list.clear();
        list.addAll(beans);
        if(beans.size() == 0){
            return;
        }
        Log.d("setDate",list.size()+"");
        switch (beans.get(0).devtype){
            case 1:        //气体分钟
                if(selectData){
                    arr1.setText(qifen[0]);  //流量
                    arr2.setText(qifen[1]);
                    arr3.setText(qifen[2]);
                    arr4.setText(qifen[3]);

                }else {
                    arr1.setText(qifen[0]);
                    arr2.setText(qifen[4]);
                    arr3.setText(qifen[5]);
                    arr4.setText(qifen[6]);

                }

                break;
            case 2:  //水分钟
                if(selectData){
                    arr1.setText(shuifen[0]);
                    arr2.setText(shuifen[1]);
                    arr3.setText(shuifen[2]);
                    arr4.setText(shuifen[3]);

                }else {
                    arr1.setText(shuifen[0]);
                    arr2.setText(shuifen[1]);
                    arr3.setText(shuifen[2]);
                    arr4.setText(shuifen[4]);
                }
                break;
            case 11:  //气体分钟
                if(selectData){
                    arr1.setText(qihour[0]);
                    arr2.setText(qihour[1]);
                    arr3.setText(qihour[2]);
                    arr4.setText(qihour[3]);

                }else {
                    arr1.setText(qihour[0]);
                    arr2.setText(qihour[4]);
                    arr3.setText(qihour[5]);
                    arr4.setText(qihour[6]);

                }
                break;
            case 21:  //水非分钟
                if(selectData){
                    arr1.setText(shuihour[0]);
                    arr2.setText(shuihour[1]);
                    arr3.setText(shuihour[2]);
                    arr4.setText(shuihour[3]);
                }else {
                    arr1.setText(shuihour[3]);
                    arr2.setText(shuihour[4]);
                    arr3.setText(shuihour[5]);
                    arr4.setText(shuihour[6]);
                }
                break;
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_wuran, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if(list.get(position).devtype == 1){   //气，分钟
                if(selectData){
                    viewHolder.time.setText(""+list.get(position).date);
                    viewHolder.arr1.setText(list.get(position).QMyanchenliuliang);
                    viewHolder.arr2.setText(list.get(position).QMyancennongdu);
                    viewHolder.arr3.setText(list.get(position).QMyancenzhesuan);
                    viewHolder.arr4.setText(list.get(position).QMeryanghualiu);
                }else {
                    viewHolder.time.setText(""+list.get(position).date);
                    viewHolder.arr1.setText(list.get(position).QMyanchenliuliang);
                    viewHolder.arr2.setText(list.get(position).QMeryanghualiuzhesuan);
                    viewHolder.arr3.setText(list.get(position).QMdanyang);
                    viewHolder.arr4.setText(list.get(position).QMdanyangzhesuan);
                }


        }else if(list.get(position).devtype == 2){ //水  分钟
                if(selectData){
                    viewHolder.time.setText(""+list.get(position).date);
                    viewHolder.arr1.setText(list.get(position).SMliuliang);
                    viewHolder.arr2.setText(list.get(position).SMcod);
                    viewHolder.arr3.setText(list.get(position).SMnh3n);
                    viewHolder.arr4.setText(list.get(position).SMzonlin);
                }else {
                    viewHolder.time.setText(""+list.get(position).date);
                    viewHolder.arr1.setText(list.get(position).SMliuliang);
                    viewHolder.arr2.setText(list.get(position).SMcod);
                    viewHolder.arr3.setText(list.get(position).SMnh3n);
                    viewHolder.arr4.setText(list.get(position).SMzondan);
                }

        }else if(list.get(position).devtype == 11){  //气体 非分钟

            if(selectData){
                viewHolder.time.setText(""+list.get(position).date);
                viewHolder.arr1.setText(list.get(position).yanCenLiuLiang);
                viewHolder.arr2.setText(list.get(position).yanCenNongDu);
                viewHolder.arr3.setText(list.get(position).yanCenZheSuan);
                viewHolder.arr4.setText(list.get(position).so2);
            }else {
                viewHolder.time.setText(""+list.get(position).date);
                viewHolder.arr1.setText(list.get(position).so2);
                viewHolder.arr2.setText(list.get(position).so2ZheSuan);
                viewHolder.arr3.setText(list.get(position).dan);
                viewHolder.arr4.setText(list.get(position).danZheSuan);
            }

        }else if(list.get(position).devtype == 21){  //水 非分钟
            if(selectData){
                viewHolder.time.setText(""+list.get(position).date);
                viewHolder.arr1.setText(list.get(position).liuLiangPinJun);
                viewHolder.arr2.setText(list.get(position).andanPingjun);
                viewHolder.arr3.setText(list.get(position).CODPinJun);
                viewHolder.arr4.setText(list.get(position).zonlinPingJun);
            }else {
                viewHolder.time.setText(""+list.get(position).date);
                viewHolder.arr1.setText(list.get(position).liuLiangPinJun);
                viewHolder.arr2.setText(list.get(position).andanPingjun);
                viewHolder.arr3.setText(list.get(position).CODPinJun);
                viewHolder.arr4.setText(list.get(position).zondanPingJun);
            }
        }
        if(list.get(position).id%2 == 0){
            viewHolder.back.setBackgroundColor(Color.WHITE);
        }else {
            viewHolder.back.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        private TextView num;
//        private LinearLayout ll;
//        private TextView id;
        private TextView time;
        private TextView arr1;
        private TextView arr2;
        private TextView arr3;
        private TextView arr4;
        private LinearLayout back;
//        private TextView arr5;
        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            arr1 = (TextView) itemView.findViewById(R.id.tv_1);
            arr2 = (TextView) itemView.findViewById(R.id.tv_2);
            arr3 = (TextView) itemView.findViewById(R.id.tv_3);
            arr4 = (TextView) itemView.findViewById(R.id.tv_4);
            back = (LinearLayout) itemView.findViewById(R.id.ll_back);
        }
    }
}
