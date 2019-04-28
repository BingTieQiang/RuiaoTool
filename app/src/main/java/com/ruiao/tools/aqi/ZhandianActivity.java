package com.ruiao.tools.aqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhandianActivity extends AppCompatActivity {
    @BindView(R.id.dev_name)
    TextView devName;
    @BindView(R.id.dev_num)
    TextView devNum;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_jingdu)
    TextView tvJingdu;
    @BindView(R.id.tv_weidu)
    TextView tvWeidu;
    private AqiBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhandian);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        bean = (AqiBean) bundle.getSerializable("bean");
        devName.setText(bean.name);
        devNum.setText(bean.MonitorID);
        tvType.setText("微型站");
        tvArea.setText("藁城");
        tvJingdu.setText(""+bean.lat);
        tvWeidu.setText(""+bean.longt);
    }
}
