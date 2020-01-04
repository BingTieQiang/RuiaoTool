package com.ruiao.tools.fenbiao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ruiao.tools.R;
import com.ruiao.tools.utils.StatusBarUtil;

public class FenbiaoYichangyiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.darkMode(this);
        setContentView(R.layout.activity_yichangyice_changxin);
    }
}
