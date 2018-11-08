package com.ruiao.tools.ic_card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruiao.tools.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ruiao on 2018/5/7.
 */

public class ArrAdapter extends BaseAdapter {
    private Context context;

    public ArrAdapter(Context context, ArrayList<IcBean> list) {
        this.context = context;
        this.list = list;
    }

    private ArrayList<IcBean> list = null;
    @Override
    public int getCount() {
        return list.size() ;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder1 viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_ic_card, null);
            viewHolder = new ViewHolder1(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder1) convertView.getTag();
        }

        viewHolder.name.setText(list.get(position).name);
        viewHolder.num.setText(list.get(position).num);
        viewHolder.unit.setText(list.get(position).unit);
        return convertView;
    }
    class ViewHolder1 {
        @BindView(R.id.tv_ic_name)
        TextView name;
        @BindView(R.id.tv_ic_num)
        TextView num;
        @BindView(R.id.tv_ic_unit)
        TextView unit;

        ViewHolder1(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
