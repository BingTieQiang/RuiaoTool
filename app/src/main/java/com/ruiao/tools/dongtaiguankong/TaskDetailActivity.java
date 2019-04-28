package com.ruiao.tools.dongtaiguankong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ruiao.tools.R;


public class TaskDetailActivity extends BaseAppCompatActivity {
    private TaskBean bean;
    private TextView tv_renwuhao;
    private TextView tv_time;
    private TextView tv_people;
    private TextView tv_address;
    private TextView tv_context;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (TaskBean) getIntent().getSerializableExtra("task");
        initData();
    }

    private void initData() {
        tv_renwuhao.setText(bean.people);
        tv_time.setText(bean.time);
        tv_people.setText(bean.address);
        tv_address.setText(bean.status);
        car.setText(bean.context);
//        String tip = new String();
//        for(int i =0;i<bean.tips.size();i++){
//            tip+=bean.tips.get(i)+"\n";
//        }
//        tv_context.setText(tip);
//
//        String status = bean.status;
//        if(status.equals("未接受")){
//            tv_1.setTextColor(Color.RED);
//        }else if(status.equals("进行中")){
//            tv_2.setTextColor(Color.RED);
//        }
//        car.setText(bean.car);
    }

    @Override
    protected void initView() {
        tv_renwuhao = findViewById(R.id.tv_renwuhao);
        tv_time = findViewById(R.id.tv_time);
        tv_people = findViewById(R.id.tv_people);
        tv_address = findViewById(R.id.tv_address);
        tv_context = findViewById(R.id.tv_context);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        car = findViewById(R.id.tv_car);
    }

    @Override
    public String SetTitle() {
        return "详细";
    }

    @Override
    protected int getLayoutId() {

        return R.layout.activity_task_detail;
    }

    public void go(View view) {
        if(bean.status.equals("未接受")){
            finish();
            Intent intent = new Intent(context, StartTaskActivity.class);
            intent.putExtra("task",bean);
            startActivity(intent);
        }else if(bean.status.equals("进行中")){
            finish();
            Intent intent = new Intent(context, EndTaskActivity.class);
            intent.putExtra("task",bean);
            startActivity(intent);
        }
    }
}
