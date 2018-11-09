package com.ruiao.tools.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ruiao.tools.R;
import com.ruiao.tools.notice.NoticeBean;
import com.ruiao.tools.ui.base.BaseActivity;
import com.ruiao.tools.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends BaseActivity {
    @BindView(R.id.dev_name)
    TextView devName;
    @BindView(R.id.dev_gongsi)
    TextView com;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_wuranwu)
    TextView tvWuranwu;
    @BindView(R.id.tv_chaobaio)
    TextView tvChaobaio;
    @BindView(R.id.tv_stand)
    TextView tvStand;
    private NoticeBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_alarm;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        bean = (NoticeBean) getIntent().getSerializableExtra("bean");
        StatusBarUtil.darkMode(this);
        com.setText(bean.com);
        tvTime.setText(bean.time);
        tvWuranwu.setText(bean.title);
        tvChaobaio.setText(bean.tvChaobaio);
        tvStand.setText(bean.tvStand);
        devName.setText(bean.msg);


    }
}
