package com.ruiao.tools.wuran;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.ruiao.tools.R;
import com.ruiao.tools.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;

public class WuRanChartActivity extends AppCompatActivity {
    private String[] qifen = {"流量","烟尘","烟尘折算","SO2","SO2折算","氮氧化物","氮氧化物折算"}; //气体分钟 1
    private String[] shuifen = {"流量","COD","氨氮","总磷","总氮"};                       //水分钟  2
    private String[] qihour = {"烟气流量平均值","烟尘平均值","烟尘折算平均值","SO2平均","SO2折算","氮氧化物平均","氮氧化物折算平均值"};  //气体非分钟  11
    private String[] shuihour = {"流量平均值","COD平均值","氨氮平均值","总磷平均值","总氮平均值"};  //水非分钟  21
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_change_city)
    TextView tvChangeCity;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    Context context;
    ArrayList<WuranBean> list;
    WuRanChartManager manager;
    @BindView(R.id.tag_group)
    TagGroup tagGroup;
    private int timeType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wu_ran_chart);
        ButterKnife.bind(this);
        this.context = this;
        list = (ArrayList<WuranBean>) getIntent().getSerializableExtra("list");
        timeType = getIntent().getIntExtra("timeType",0);
        initView();
        initData();
        manager.setWhich(0,timeType);

    }

    private void initData() {
        manager.setData(list,timeType);

        switch (list.get(0).devtype){
            case 1:
                tagGroup.setTags(qifen);
                break;
            case 11:
                tagGroup.setTags(qihour);
                break;
            case 2:
                tagGroup.setTags(shuifen);
                break;
            case 21:
                tagGroup.setTags(shuihour);
                break;
        }
    }

    private void initView() {
        StatusBarUtil.darkMode(this);
        manager = new WuRanChartManager(lineChart);
        manager.initChart();
//        String[] tags  = tagGroup.getTags();
//        final ArrayList<String> arrayList = (ArrayList<String>) Arrays.asList(tags);
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                String[] tags =  tagGroup.getTags();
//                ArrayList<String> arrayList = (ArrayList<String>) Arrays.asList(tags);
//                int pos =  arrayList.indexOf(tag);
                int pos = 0;
                for(int i = 0 ;i< tags.length; i++){
                    if(tags[i].equals(tag)){
                        pos = i;
                        break;
                    }
                }

                manager.setWhich(pos,timeType);
            }
        });


    }
}
