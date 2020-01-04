package com.ruiao.tools.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.abel533.echarts.json.GsonOption;

public class GuankongEchartView extends WebView {
    private static final String TAG = GuankongEchartView.class.getSimpleName();

    public GuankongEchartView(Context context) {
        this(context, null);
    }

    public GuankongEchartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuankongEchartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        loadUrl("file:///android_asset/guankongcharts.html");
    }

    /**刷新图表
     * java调用js的loadEcharts方法刷新echart
     * 不能在第一时间就用此方法来显示图表，因为第一时间html的标签还未加载完成，不能获取到标签值
     * @param option
     */
    public void refreshEchartsWithOption(GsonOption option) {
        if (option == null) {
            return;
        }
        String optionString = option.toString();
        String call = "javascript:loadEcharts('" + optionString + "')";
        loadUrl(call);
    }



    public void loadStr(String string) {
        if (string == null) {
            return;
        }
        String call = "javascript:loadEcharts('" + string + "')";
        loadUrl(call);

    }
    public void setDatex(String string) {
        if (string == null) {
            return;
        }
        String call = "javascript:setDate('" + string + "')";
        loadUrl(call);
    }
}
