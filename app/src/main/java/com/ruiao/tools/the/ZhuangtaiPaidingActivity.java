package com.ruiao.tools.the;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ruiao.tools.R;
import com.ruiao.tools.utils.StatusBarUtil;

public class ZhuangtaiPaidingActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.darkMode(this);
        setContentView(R.layout.activity_panding_zhongxin);
        /*
        String devid = getIntent().getStringExtra("Devid");
        if(devid.equals("3")){
            setContentView(R.layout.activity_panding_zhongxin);
        }else if(devid.equals("10")){
            setContentView(R.layout.activity_panding_zhongxin);
        }else if(devid.equals("9")){
            setContentView(R.layout.activity_panding_zhongxin);
        }else if(devid.equals("11")){
            setContentView(R.layout.activity_panding_zhongxin);
        }
*/
    }
}
