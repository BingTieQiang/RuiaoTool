package com.ruiao.tools.advertisement;

import android.os.Bundle;
import android.webkit.WebView;

import com.ruiao.tools.R;
import com.ruiao.tools.ui.base.BaseActivity;
import com.ruiao.tools.utils.StatusBarUtil;

import butterknife.BindView;

public class AdActivity extends BaseActivity {
    @BindView(R.id.web_ad)
    WebView webAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this);

        webAd.loadUrl("file:///android_asset/ff.html");
    }
}
