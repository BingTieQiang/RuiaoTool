package com.ruiao.tools.ui.activity.functionfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiao.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ruiao on 2018/4/18.
 */

public class WordReportAdapter extends BaseAdapter {
    public WordReportAdapter(Context context) {
        this.context = context;
    }

    private Context context;
    private String[] item_Text = {"VOC", "空气站","污染源","工地扬尘","水质自动站","厂区检测","油烟在线","设备监控","IC卡总量站"};
    private int[] item_img = {R.mipmap.vocs, R.mipmap.kongqizhan,R.mipmap.wuranyuan,
                                R.mipmap.yangchen, R.mipmap.shuizhi,R.mipmap.changqu,
                                    R.mipmap.jiankong,R.mipmap.famen,
            R.mipmap.shuaka

    };
    @Override
    public int getCount() {
        return item_img.length;
    }

    @Override
    public Object getItem(int position) {
        return item_Text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.workreport_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.workImg.setImageResource(item_img[position]);
        viewHolder.workText.setText(item_Text[position]);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.work_img)
        ImageView workImg;
        @BindView(R.id.work_text)
        TextView workText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
