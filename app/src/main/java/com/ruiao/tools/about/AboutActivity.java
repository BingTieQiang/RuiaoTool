package com.ruiao.tools.about;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiao.tools.R;
import com.ruiao.tools.utils.PackageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.webview)
    WebView webView;
    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        final String appVersion = PackageUtils.getVersionName(this);

        webView.getSettings().setJavaScriptEnabled(true); // 开启javascript支持
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.addJavascriptInterface(this, "changeVersionJs");
        webView.loadUrl("file:///android_asset/about.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:changeVersion('" + appVersion + "')");
            }
        });
    }

}
