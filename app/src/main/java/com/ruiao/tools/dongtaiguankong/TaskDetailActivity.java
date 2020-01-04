package com.ruiao.tools.dongtaiguankong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.ruiao.tools.dongtaiguankong.DongtaiBaojing.TaskBean;
import com.ruiao.tools.R;


public class TaskDetailActivity extends BaseAppCompatActivity {
    private TaskBean bean;
    private TextView company;
    private TextView tv_time;
    private TextView tv_people;
    private TextView tv_address;
    private TextView tv_context;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView car;
    private ImageView img1,img2,img3;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bean = (TaskBean) getIntent().getBundleExtra("bundle").getSerializable("bean");
        initData();

    }

    private void initData() {
        company.setText(bean.company);
        tv_time.setText(bean.time);
        tv_people.setText(bean.people);
        tv_address.setText(bean.company);
        car.setText(bean.context);
        if(bean.type.equals("未接受")){
            tv_1.setTextColor(Color.GREEN);
            img1.setImageResource(R.drawable.yuan2);
        }else if(bean.type.equals("进行中")){
            tv_1.setTextColor(Color.GREEN);
            tv_2.setTextColor(Color.GREEN);
            img1.setImageResource(R.drawable.yuan2);
            img2.setImageResource(R.drawable.yuan2);
        }else if(bean.type.equals("已完成")){
            tv_1.setTextColor(Color.GREEN);
            tv_2.setTextColor(Color.GREEN);
            tv_3.setTextColor(Color.GREEN);
            img1.setImageResource(R.drawable.yuan2);
            img2.setImageResource(R.drawable.yuan2);
            img3.setImageResource(R.drawable.yuan2);
        }
    }

    @Override
    protected void initView() {
        company = findViewById(R.id.tv_renwuhao);
        tv_time = findViewById(R.id.tv_time);
        tv_people = findViewById(R.id.tv_people);
        tv_address = findViewById(R.id.tv_address);
        tv_context = findViewById(R.id.tv_context);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        img1 = findViewById(R.id.imageView2);
        img2 = findViewById(R.id.imageView3);
        img3 = findViewById(R.id.imageView4);
        car = findViewById(R.id.context);
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
        if(bean.type.equals("未接受")){
            finish();
            Intent intent = new Intent(context, StartTaskActivity.class);
            intent.putExtra("task",bean);
            startActivity(intent);
        }else if(bean.type.equals("进行中")){
            finish();
            Intent intent = new Intent(context, EndTaskActivity.class);
            intent.putExtra("task",bean);
            startActivity(intent);
        }else if(bean.type.equals("已完成")){


        }
    }
}
