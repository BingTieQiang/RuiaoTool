package com.ruiao.tools.fenbiao;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.dongtaiguankong.BaseAppCompatActivity;


import com.ruiao.tools.dongtaiguankong.TaskBean;


public class FenbiaoDetailActivity extends BaseAppCompatActivity {
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

//        bean = (TaskBean) getIntent().getBundleExtra("bundle").getSerializable("bean");
        bean = (TaskBean) getIntent().getSerializableExtra("task");
        initData();

    }

    private void initData() {
        company.setText(bean.address);
        tv_time.setText(bean.time);
        tv_people.setText(bean.people);
        tv_address.setText(bean.address);
        car.setText(bean.context);

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

        return R.layout.activity_fenbiao_task_detail;
    }

    public void go(View view) {

    }
}
