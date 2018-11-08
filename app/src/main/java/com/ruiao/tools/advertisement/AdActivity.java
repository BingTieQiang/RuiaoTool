package com.ruiao.tools.advertisement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.ruiao.tools.R;

public class AdActivity extends AppCompatActivity {
    private WebView ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ad = (WebView) findViewById(R.id.web_ad);
        ad.loadUrl("file:///android_asset/ff.html");
    }
}
