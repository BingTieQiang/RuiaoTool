package com.ruiao.tools.the;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YichangyiceActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.darkMode(this);
        String devid = getIntent().getStringExtra("Devid");
        if(devid.equals("3")){
            setContentView(R.layout.activity_yichangyice_changxin);
        }else if(devid.equals("10")){
            setContentView(R.layout.activity_yichangyice_zhongxin);
        }else if(devid.equals("9")){
            setContentView(R.layout.activity_yichangyice_baokang);
        }else if(devid.equals("11")){
            setContentView(R.layout.activity_yichangyice_gaixin);
        }

    }
}
