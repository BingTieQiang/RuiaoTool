package com.ruiao.tools.dongtaiguankong;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.ruiao.tools.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ruiao on 2018/9/25.
 */

public class TaskListAdapter extends BaseAdapter {
    ArrayList<TaskBean> list;
    Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
    public TaskListAdapter( Context context,ArrayList<TaskBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TaskBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHold viewHold ;
        if (convertView == null) {

            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tasklist, null);

            viewHold = new ViewHold();
//            viewHold.ico = (ImageView) convertView.findViewById(R.id.ico);
//            viewHold.type = (TextView) convertView.findViewById(R.id.type);
            viewHold.time = (TextView) convertView.findViewById(R.id.time);
            viewHold.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.type.setText("正在进行");
        try {
            Date date=sdf.parse(list.get(i).time);
            viewHold.time.setText(sdf2.format(date));
        } catch (ParseException e) {

            e.printStackTrace();
        }

        viewHold.type.setText(list.get(i).status);
        viewHold.address.setText(list.get(i).address);
        if(list.get(i).status.equals("未接受")){
            viewHold.ico.setImageResource(R.drawable.renwu1);
        }else {
            viewHold.ico.setImageResource(R.drawable.renwu2);
        }
        return convertView;
    }
    class ViewHold {
        private ImageView ico;
        private TextView type;
        private TextView time;
        private TextView address;
    }
}
