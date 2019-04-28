package com.ruiao.tools.gongkuang;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.the.YichangyiceActivity;

import java.util.ArrayList;

/**
 * Created by ruiao on 2018/9/25.
 */

public class ShishigongkuangListAdapter extends BaseExpandableListAdapter {
    private Context mcontext;

    public ShishigongkuangListAdapter(Context mcontext, ArrayList<ShishigongkuangBean> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    private ArrayList<ShishigongkuangBean> list = null;
    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).lists.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).lists.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dongtaiguankong1,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.time = (TextView)convertView.findViewById(R.id.tv_time);
            groupViewHolder.wangluo = (TextView)convertView.findViewById(R.id.tv_wanlguo);
            groupViewHolder.yujing = (TextView)convertView.findViewById(R.id.tv_yujing);
            groupViewHolder.quyu = (TextView)convertView.findViewById(R.id.tv_address);
            groupViewHolder.leibie = (TextView)convertView.findViewById(R.id.tv_leibie);
            groupViewHolder.shengchan = (TextView)convertView.findViewById(R.id.tv_shengchanshesh);
            groupViewHolder.zhili = (TextView)convertView.findViewById(R.id.tv_zhili);
            groupViewHolder.qiye = (TextView)convertView.findViewById(R.id.tv_qiye);
            groupViewHolder.rr = convertView.findViewById(R.id.rr_);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.time.setText(list.get(groupPosition).shijian);
        groupViewHolder.wangluo.setText(list.get(groupPosition).wangluo);
        groupViewHolder.yujing.setText(list.get(groupPosition).yingjicuoshi);
        groupViewHolder.quyu.setText(list.get(groupPosition).xingzhengquyu);
        groupViewHolder.leibie.setText(list.get(groupPosition).hangye);
        groupViewHolder.shengchan.setText(list.get(groupPosition).shengchan);
        groupViewHolder.zhili.setText(list.get(groupPosition).zhilisheshi);
        groupViewHolder.qiye.setText(list.get(groupPosition).qiye);
        groupViewHolder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, YichangyiceActivity.class);
                intent.putExtra("Devid",list.get(groupPosition).qiye);
                mcontext.startActivity(intent);
            }
        });
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dongtaiguankong2,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv_shebei_name = (TextView)convertView.findViewById(R.id.tv_shebei_name);
            childViewHolder.tv_shebei_value = (TextView)convertView.findViewById(R.id.tv_shebei_value);
            childViewHolder.ll = (LinearLayout) convertView.findViewById(R.id.ll_list);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tv_shebei_name.setText(list.get(groupPosition).lists.get(childPosition).name);
        if(list.get(groupPosition).lists.get(childPosition).value.equals("电流值")){
            childViewHolder.tv_shebei_value.setText(list.get(groupPosition).lists.get(childPosition).value);
        }else {
            childViewHolder.tv_shebei_value.setText(list.get(groupPosition).lists.get(childPosition).value+"A");
        }

        if(list.get(groupPosition).lists.get(childPosition).name.equals("设备名称")){
            childViewHolder.ll.setBackgroundColor(Color.GRAY);
        }else {
            childViewHolder.ll.setBackgroundColor(Color.WHITE);
        }
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder{
        TextView time;
        TextView qiye;
        TextView wangluo; //网络
        TextView yujing;   //预警
        TextView quyu;   //区域
        TextView leibie;   //类别
        TextView shengchan;   //shengchan
        TextView zhili;   //zhili
        LinearLayout rr;   //zhili
    }
    class ChildViewHolder{
        TextView tv_shebei_name;
        TextView tv_shebei_value;
        LinearLayout ll;
    }
}
