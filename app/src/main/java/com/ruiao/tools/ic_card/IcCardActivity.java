package com.ruiao.tools.ic_card;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ruiao.tools.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.LinearLayout.HORIZONTAL;

public class IcCardActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ArrayList<IcBean> list;
    private ArrAdapter adapter;
    private RecAdapter recAdapter;
    private TabLayout tab;
    private GridView  gv1;
    private RecyclerView rec;
    private RelativeLayout rl_data_type; // 时间选择框
    private TextView tv_start; //起始时间
    private TextView tv_end;  //结束时间
    private TextView tv_data_type;  //时间类型
    TimePickerView thisTime ;
    SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_card);
        initView();
        initDate();

    }

    private void initView() {
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        context = this;
        gv1 = (GridView) findViewById(R.id.gv_lay1);
        tab = (TabLayout) findViewById(R.id.tabLayout);
        rec = (RecyclerView) findViewById(R.id.rec_chart);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_data_type = (TextView) findViewById(R.id.tv_data_type);
        rl_data_type = (RelativeLayout) findViewById(R.id.rl_data_type);

        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        rl_data_type.setOnClickListener(this);

    }

    private void initDate() {
        list = new ArrayList<>();
        list.add(new IcBean("PM2.5","μg/m³","0.1"));
        list.add(new IcBean("PM2.5","μg/m³","0.1"));
        list.add(new IcBean("PM2.5","μg/m³","0.1"));
        list.add(new IcBean("PM2.5","μg/m³","0.1"));
        adapter = new ArrAdapter(context,list);

        gv1.setAdapter(adapter);

        tab.addTab(tab.newTab().setText("AQI"));
        tab.addTab(tab.newTab().setText("SO2"));
        tab.addTab(tab.newTab().setText("NO2"));
        tab.addTab(tab.newTab().setText("PM10"));
        tab.addTab(tab.newTab().setText("PM2.5"));
        tab.addTab(tab.newTab().setText("CO"));
        tab.addTab(tab.newTab().setText("O3"));
        //设置tab选中事件
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ArrayList<Double> value = new ArrayList<>();
        value.add(10.4);
        value.add(30.4);
        value.add(40.4);
        value.add(50.4);
        value.add(10.4);
        value.add(20.4);
        value.add(10.4);
        value.add(30.4);
        value.add(40.4);
        value.add(50.4);
        value.add(10.4);
        value.add(20.4);
        value.add(10.4);
        value.add(30.4);
        value.add(40.4);
        value.add(50.4);
        value.add(10.4);
        value.add(20.4);

        recAdapter = new RecAdapter(context,value);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,HORIZONTAL,false );

        //设置布局管理器
        rec.setLayoutManager(layoutManager);

        rec.setAdapter(recAdapter);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.tv_start:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Toast.makeText(context,bartDateFormat.format(date),Toast.LENGTH_SHORT).show();
                        tv_start.setTextSize(13);
                        tv_start.setText(bartDateFormat.format(date));
                    }
                }).setType(new boolean[]{true, true, true, true, true, true}).setTitleText("请选择起始时间").build();
                thisTime.show();
                break;
            case R.id.tv_end:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Toast.makeText(context,bartDateFormat.format(date),Toast.LENGTH_SHORT).show();
                        tv_end.setTextSize(13);
                        tv_end.setText(bartDateFormat.format(date));
                    }
                }).setType(new boolean[]{true, true, true, true, true, true}).setTitleText("请选择结束时间").build();
                thisTime.show();
                break;
            case R.id.rl_data_type:


                break;
            default:
                break;
        }
    }
}
