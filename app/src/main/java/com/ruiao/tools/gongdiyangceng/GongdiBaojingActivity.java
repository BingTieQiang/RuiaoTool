package com.ruiao.tools.gongdiyangceng;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GongdiBaojingActivity extends AppCompatActivity {
    GongdiBaojingBean bean;
    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_company)
    TextView tv_company;
    @BindView(R.id.dev_address)
    TextView dev_address;
    @BindView(R.id.tv_wuranwu)
    TextView tv_wuranwu;
    @BindView(R.id.tv_chaobaio)
    TextView tv_chaobaio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongdi_baojing);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        bean = (GongdiBaojingBean) bundle.getSerializable("bean");
        tv_time.setText(bean.time);
        tv_company.setText(bean.company);
        dev_address.setText(bean.point);
        tv_wuranwu.setText(bean.type);
        tv_chaobaio.setText(bean.context);

    }

}
