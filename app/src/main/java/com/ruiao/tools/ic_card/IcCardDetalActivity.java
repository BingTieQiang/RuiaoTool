package com.ruiao.tools.ic_card;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

/**
 * IC卡排污详细
 */
public class IcCardDetalActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private TabLayout tab;
    private RelativeLayout rl_data_type; // 时间选择框
    private TextView tv_start; //起始时间
    private TextView tv_end;  //结束时间
    private TextView tv_data_type;  //时间类型

    private RecyclerView rec;  //时间类型
    TimePickerView thisTime ;
    SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    RecAdapter recAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_card_detal);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        context = this;

        rec = (RecyclerView) findViewById(R.id.rec_chart);
        tab = (TabLayout) findViewById(R.id.tabLayout);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_data_type = (TextView) findViewById(R.id.tv_data_type);
        rl_data_type = (RelativeLayout) findViewById(R.id.rl_data_type2);

        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        rl_data_type.setOnClickListener(this);

        tab.addTab(tab.newTab().setText("COD"));
        tab.addTab(tab.newTab().setText("COD排放量"));
        tab.addTab(tab.newTab().setText("NH₃N"));
        tab.addTab(tab.newTab().setText("NH₃N排放量"));
        tab.addTab(tab.newTab().setText("阀门控制方式"));
        tab.addTab(tab.newTab().setText("阀门状态"));

        ArrayList<Double> value = new ArrayList<>();
        value.add(10.4);
        value.add(30.4);
        value.add(40.4);
        value.add(50.4);
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
            case R.id.rl_data_type2:
                final String items[]={"分钟数据","小时数据","日数据"};
                AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
                builder.setTitle("提示"); //设置标题
                //builder.setMessage("是否确认退出?"); //设置内容
                builder.setIcon(R.mipmap.ic_lonch);//设置图标，图片id即可
                //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                builder.setItems(items,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tv_data_type.setText(items[which]);

                    }
                });

                builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }
    }


}
