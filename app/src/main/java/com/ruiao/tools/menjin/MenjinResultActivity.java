package com.ruiao.tools.menjin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.Pbdialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenjinResultActivity extends AppCompatActivity {
    private Pbdialog dialog; //进度条
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                initStatue();
            }

        }
    };
    public String mn;
    Context context;
    @BindView(R.id.dev_name)
    TextView devName;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_bianhao)
    TextView tvBianhao;
    @BindView(R.id.tv_menjinzhuangtai)
    TextView tvMenjinzhuangtai;
    @BindView(R.id.tv_kaiqizhuangtai)
    TextView tvKaiqizhuangtai;
    @BindView(R.id.img_menjing)
    ImageView imgMenjing;
    @BindView(R.id.open)
    ImageView open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mn = getIntent().getStringExtra("mn");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menjin_result);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        context = this;
        initStatue();
    }

    private void initStatue() {
        RequestParams pa = new RequestParams();
        pa.put("username", SPUtils.get(context, "username", ""));
        pa.add("MN", mn);
        HttpUtil.get(URLConstants.MENJIN, pa, new HttpUtil.SimpJsonHandle(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        JSONObject info = response.getJSONObject("data");
                        devName.setText(info.getString("name"));
                        tvCompany.setText(info.getString("company"));
                        tvBianhao.setText(info.getString("number"));
                        tvMenjinzhuangtai.setText(info.getString("statue"));
                        String s1 = info.getString("statue");
                        tvKaiqizhuangtai.setText(info.getString("open"));  //开启状态
                        if(s1.equals("未在线")){
                            imgMenjing.setImageResource(R.drawable.suoweizaixian);
                        }


                        String ss = tvKaiqizhuangtai.getText().toString();
                        if(ss.equals("打开")){
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            imgMenjing.setImageResource(R.drawable.menjinkai);
                        }else if(ss.equals("关闭")){
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            imgMenjing.setImageResource(R.drawable.menjinguan);
                        }else if(ss.equals("打开 长时间未关闭")){
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            imgMenjing.setImageResource(R.drawable.menjinkai);
                        }else if(ss.equals("操作异常")){
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            imgMenjing.setImageResource(R.drawable.suoweizaixian);
                        }
                    } else {
                        ToastHelper.shortToast(context, response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    @OnClick(R.id.open)
    public void onViewClicked() {
        dialog = showdialog(this, "正在开锁.......");
        String ss = tvKaiqizhuangtai.getText().toString();
        if(!ss.equals("关闭")){
            ToastHelper.shortToast(context,"当前无法开锁");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            return;
        }


        RequestParams pa = new RequestParams();
        pa.put("username", SPUtils.get(context, "username", ""));
        pa.add("MN", mn);
        HttpUtil.get(URLConstants.MENJIN_OPEN, pa, new HttpUtil.SimpJsonHandle(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {

                    } else {
                        ToastHelper.shortToast(context, response.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
//        count = 0;
        Message msg =  new Message();
        msg.what = 1;
        handler.sendMessageDelayed(msg,4500);


    }
    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();

        return pbdialog;
    }

    @Override
    protected void onPause() {
        super.onPause();


    }


}
