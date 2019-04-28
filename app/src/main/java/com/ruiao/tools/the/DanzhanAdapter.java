package com.ruiao.tools.the;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiao.tools.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ruiao on 2018/9/25.
 */

public class DanzhanAdapter extends BaseAdapter {


    ArrayList<GongkuangBean> list;
    Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

    public DanzhanAdapter(Context context, ArrayList<GongkuangBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public GongkuangBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.size();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHold viewHold;
        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dongtaiguankongx, null);
            viewHold = new ViewHold();

//            viewHold.dianliang = (TextView) convertView.findViewById(R.id.tv_dianliang);
            viewHold.qiye = (TextView) convertView.findViewById(R.id.tv_qiye);
            viewHold.yujing = (TextView) convertView.findViewById(R.id.tv_yujing);
            viewHold.time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHold.wangluo = (TextView) convertView.findViewById(R.id.tv_wangluo);
            viewHold.shengchan = (TextView) convertView.findViewById(R.id.tv_shengchan);
            viewHold.shengchansheshi = (TextView) convertView.findViewById(R.id.tv_shengchansheshi);
            viewHold.zhili = (TextView) convertView.findViewById(R.id.tv_zhili);

            viewHold.a1 = (TextView) convertView.findViewById(R.id.tv_1);
            viewHold.a2 = (TextView) convertView.findViewById(R.id.tv_2);
            viewHold.a3 = (TextView) convertView.findViewById(R.id.tv_3);
            viewHold.a4 = (TextView) convertView.findViewById(R.id.tv_4);
            viewHold.a5 = (TextView) convertView.findViewById(R.id.tv_5);
            viewHold.qilu = (TextView) convertView.findViewById(R.id.tv_qilu);
            viewHold.dianlu = (TextView) convertView.findViewById(R.id.tv_dianlu);
            viewHold.lasi = (TextView) convertView.findViewById(R.id.tv_lasi);
            viewHold.rr = convertView.findViewById(R.id.rr_);
            viewHold.the = convertView.findViewById(R.id.theresult);
            viewHold.shuoming = convertView.findViewById(R.id.shuoming);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }


        viewHold.a1.setText(list.get(i).yihao+"A");
        viewHold.a2.setText(list.get(i).erhao+"A");
        viewHold.a3.setText(list.get(i).sanhao+"A");
        viewHold.a4.setText(list.get(i).sihao+"A");
        viewHold.a5.setText(list.get(i).wuhao+"A");

        viewHold.qilu.setText(list.get(i).qilu+"A");
        viewHold.dianlu.setText(list.get(i).dianlu+"A");
        viewHold.lasi.setText(list.get(i).lasizong+"A");


        viewHold.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, YichangyiceActivity.class));
            }
        });
        return convertView;
    }

    class ViewHold {
        private ImageView img_wanlguo;
        private ImageView img_shengchan;
        private ImageView img_shengchansheshi;
        private ImageView img_zhili;

        private TextView qiye;
        private TextView wangluo;
        private TextView shengchan;
        private TextView shengchansheshi;
        private TextView zhili;
        private TextView time;
        private TextView yujing;
        private TextView shuoming;
        private TextView the;

        private RelativeLayout rr;
        private TextView a1,a2,a3,a4,a5,dianlu,qilu,lasi;


    }
}
