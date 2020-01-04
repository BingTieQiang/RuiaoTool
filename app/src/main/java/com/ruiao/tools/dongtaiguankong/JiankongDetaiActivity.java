package com.ruiao.tools.dongtaiguankong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.dongtaiguankong.JanKong.TaskBean;

import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;


public class JiankongDetaiActivity extends BaseAppCompatActivity  {
    int i = 0;
    ArrayList<String> arrayList = new ArrayList<>();
    Date data = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日HH时");
    TimePickerView thisTime;
    private WebView web_img;
    private TaskBean bean;
    private TextView type,company,address;
    private ImageView img1;
    private ArrayList<Uri> imgList;
    private Button btn;
    long time_num;
    private EditText log;
    private boolean hasphoto = false;
    String MonitorID;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;       //当前时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1000 * 3600 * 24;
        time_num = zero;
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    @Override
    protected void initView() {
        web_img = findViewById(R.id.web_img);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        bean = (TaskBean) bundle.getSerializable("bean");
        type = findViewById(R.id.type);
        company = findViewById(R.id.qiye);
        address = findViewById(R.id.miaoshu);
        type.setText(bean.type);
        company.setText(bean.company);
        address.setText(bean.zhandian);
        MonitorID = bean.MonitorID;
        web_img.getSettings().setJavaScriptEnabled(true);
        web_img.getSettings().setSupportZoom(true);
        web_img.getSettings().setBuiltInZoomControls(true);
        web_img.getSettings().setUseWideViewPort(true);
        web_img.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_img.getSettings().setLoadWithOverviewMode(true);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
    }

    @Override
    public String SetTitle() {
        return "智能抓拍";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jianjongjetal;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OPEN_SET_REQUEST_CODE) {

            if (hasAllPermissionsGranted(grantResults)) {

//                Log.i(TAG, "用户允许打开权限");

            } else {

//                Log.i(TAG, "用户拒绝打开权限");

                showPermissionDialog("请去设置页面打开权限", OPEN_SET_REQUEST_CODE);
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_SET_REQUEST_CODE) {
            if (lacksPermissions(PERMISSION)) {

//                Log.i(TAG, "缺少权限去申请");

                ActivityCompat.requestPermissions(this, PERMISSION, OPEN_SET_REQUEST_CODE);
            }
        }
    }
    @OnClick({R.id.tv_pre, R.id.tv_after,R.id.tv_newDate,R.id.xiangqian, R.id.xianghou})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                time_num = time_num - 1000 * 3600 * 24;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 24)));
                break;
            case R.id.tv_after:
                time_num = time_num + 1000 * 3600 * 24;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 24)));
                break;
            case R.id.tv_newDate:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1000 * 3600 * 24;
                        tvNewDate.setText( format.format(date));
                        initChart( format1.format(new Date(time_num + 1000 * 3600 * 24)),format1.format(new Date(time_num +2 * 1000 * 3600 * 24))  );
                    }
                }).setType(new boolean[]{true, true, true, false, false, false}).setTitleText("请选择时间").build();
                thisTime.show();

                break;
            case R.id.xiangqian:
                if( i>0 && i <arrayList.size()){
                    web_img.loadUrl(arrayList.get(--i));
                    Log.d("id",arrayList.get(i));
                }
                break;
            case R.id.xianghou:
                if( i < (arrayList.size() - 1)){
                    web_img.loadUrl(arrayList.get(++i));
                    Log.d("id",arrayList.get(i));
                }
                break;
        }
    }
    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("Start", start_time);
        pa.add("End", end_time);
        pa.add("username", (String) SPUtils.get(this, "username", ""));
        pa.add("Rows", "60");
        pa.add("MonitorID", MonitorID);
        HttpUtil.get(URLConstants.Zhaopian, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    if (response.getBoolean("success")) {
                        arrayList.clear();
                           JSONArray array =  response.getJSONArray("data");
                           for(int i = 0;i<array.length();i++){
                               arrayList.add( array.getString(i) );
                           }
                           if(arrayList.size()!=0){
                               web_img.loadUrl(arrayList.get(0));
                           }

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void init() {
        RequestParams pa = new RequestParams();
        pa.add("username", (String) SPUtils.get(this, "username", ""));
        pa.add("MonitorID", MonitorID);
        HttpUtil.get(URLConstants.Zhaopian, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    if (response.getBoolean("success")) {
                        arrayList.clear();
                        JSONArray array =  response.getJSONArray("data");
                        for(int i = 0;i<array.length();i++){
                            arrayList.add( array.getString(i) );
                        }
                        if(arrayList.size()!=0){
                            web_img.loadUrl(arrayList.get(0));
                        }
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
