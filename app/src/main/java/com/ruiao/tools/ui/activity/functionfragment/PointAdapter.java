package com.ruiao.tools.ui.activity.functionfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruiao.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ruiao on 2018/4/18.
 */

public class PointAdapter extends BaseAdapter {
    public PointAdapter(Context context , String[] arr , int[] chartColors ) {
        this.context = context;
        this.arr = arr;
        this.chartColors = chartColors;
    }
    private String[] arr;
    private Context context;

    private int[] chartColors ;
    @Override
    public int getCount() {

        return arr.length ;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.arr_point_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.workImg.setBackgroundColor(chartColors[position]);
        viewHolder.workText.setText(arr[position]);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.work_img)
        View workImg;
        @BindView(R.id.work_text)
        TextView workText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
