package com.ruiao.tools.gongdiyangceng;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.aqi.AqiMap;
import com.ruiao.tools.aqi.AqiNewData;
import com.ruiao.tools.ic_card2.DevBean;
import com.ruiao.tools.ic_card2.FactroyBean;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AsynHttpTools;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.Pbdialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GongdiYangchengActivity extends AppCompatActivity {
    Context context;
    private String MonitorID = null;
    ArrayList<String> fac_list = new ArrayList<>();
    ArrayList<ArrayList<String>> dev_list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> dev_adapter;
    private boolean isDataReady = false;
    private boolean ready = false;
    ArrayList<FactroyBean> little = new ArrayList<>();
    Unbinder unbinder;
    Date nowTime = new Date();
    DateFormat bf = new SimpleDateFormat("MM月dd日HH时mm分");
    Date hourTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);  //小时数据

    @BindView(R.id.tv_newDate)
    TextView facName;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_1a)
    TextView tv_1a;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_2a)
    TextView tv_2a;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.tv_3a)
    TextView tv_3a;
    @BindView(R.id.tv_4)
    TextView tv_4;
    @BindView(R.id.tv_4a)
    TextView tv_4a;
    @BindView(R.id.tv_5)
    TextView tv_5;
    @BindView(R.id.tv_5a)
    TextView tv_5a;
    @BindView(R.id.tv_6)
    TextView tv_6;
    @BindView(R.id.tv_6a)
    TextView tv_6a;
    @BindView(R.id.tv_7)
    TextView tv_7;
    @BindView(R.id.tv_7a)
    TextView tv_7a;
    private Pbdialog dialog; //进度条

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<ArrayList<DevBean>> biglist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gongdi_lishi);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        context = this;
        initView();
    }
    private void initView() {
        initData();
    }

    private void initData() {
        initFacDev();
    }




    @OnClick({R.id.ll_shifenzhong, R.id.ll_xiaoshi, R.id.ll_zhou,R.id.ll_select_dev})
    public void onViewClicked(View view) {
        if(!isDataReady){
            return;
        }
        switch (view.getId()) {
            case R.id.ll_shifenzhong:
                Intent intent1 = new Intent(context,ShifenActivity.class);
                intent1.putExtra("MonitorID",MonitorID);
                startActivity(intent1);
                break;
            case R.id.ll_xiaoshi:
                Intent intent2 = new Intent(context,ShiActivity.class);
                intent2.putExtra("MonitorID",MonitorID);
                startActivity(intent2);
                break;
            case R.id.ll_zhou:
                Intent intent3 = new Intent(context,DayActivity.class);
                intent3.putExtra("MonitorID",MonitorID);
                startActivity(intent3);
                break;
            case R.id.ll_select_dev:
                initFacDev();
                break;
        }
    }
    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }
    private void initFacDev() {
        dialog = showdialog(context, "正在加载.......");
        RequestParams pa = new RequestParams();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, fac_list);
        pa.add("username", (String) SPUtils.get(context, "username", ""));
//        AsynHttpTools.httpGetMethodByParams(URLConstants.GONGDI_FAC, pa, new JsonHttpResponseHandler("GB2312") {
        AsynHttpTools.httpGetMethodByParams(URLConstants.GONGDI_FAC, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int code, Header[] heads, Throwable throwable, JSONObject json) {

                throwable.printStackTrace();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                fac_list.clear();
                try {
                    Boolean status = response.getBoolean("success");
                    if (status) {
                        JSONArray arr = response.getJSONArray("factory");
                        JSONObject fac = null;
                        JSONArray devs = null;
                        JSONObject dev = null;
                        for (int i = 0; i < arr.length(); i++) {
                            fac = arr.getJSONObject(i);
                            String facname = fac.getString("name"); //工厂名字

                            fac_list.add("" + i + " " + facname);

                            little.add(new FactroyBean(facname));
                            devs = fac.getJSONArray("device");

                            ArrayList<String> temp = new ArrayList<>();
                            ArrayList<DevBean> mlist = new ArrayList<>();
                            for (int j = 0; j < devs.length(); j++) {
                                dev = devs.getJSONObject(j);
                                String devNane = dev.getString("name");
                                String devId = dev.getString("id");

                                mlist.add(new DevBean(devNane, devId));

                                temp.add(devNane);
                            }
                            dev_list.add(temp);
                            biglist.add(mlist);
                        }
                        ready = true;
                        initPickerView();

                    } else {
                        ToastHelper.shortToast(context, response.getString("message"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void initPickerView() {
        if (!ready) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_lonch);
        builder.setTitle("选择企业：");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this, "你点击了第"+which+"个item", 1000).show();;

                showDev(which);
            }
        });
        builder.show();
    }
    public void showDev(final int bwhich) {
        dev_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dev_list.get(bwhich));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_lonch);
        builder.setTitle("选择设备：");
        builder.setAdapter(dev_adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = biglist.get(bwhich).get(which).getId(); //获取设备ID
                MonitorID = id;
//                facName.setText(little.get(bwhich).getFacname());
                facName.setText(little.get(bwhich).getFacname()+biglist.get(bwhich).get(which).getDevNane());
//                MonitorID = MonitorID;
                isDataReady = true;
                initData(id);
            }
        });
        builder.show();
    }
    private void initData(String MonitorID) {
        dialog = showdialog(context, "正在加载.......");
        RequestParams pa = new RequestParams();
        pa.add("start", format.format(new Date(nowTime.getTime() -  8 * 60 * 1000)));
        pa.add("end", format.format(nowTime));
        pa.add("type", "fen");
        pa.add("MonitorID", MonitorID);
        pa.add("username", (String) SPUtils.get(context, "username", ""));
        AsynHttpTools.httpGetMethodByParams(URLConstants.GONGDI_Histry, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int code, Header[] heads, Throwable throwable, JSONObject json) {

                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    ArrayList<GongdiNowBean> beans = new ArrayList<>();

                    Boolean status = response.getBoolean("success");
                    if (status) {
                        JSONArray arry = response.getJSONArray("data");
                        for(int i = 0 ;i<arry.length() ;i++){
//                            GongdiNowBean bean =  new GongdiNowBean();
//                            JSONObject obj = arry.getJSONObject(i);
//                            bean.time  = obj.getString("time");
//                            bean.pm10 = obj.getString("pm10");
//                            beans.add(bean);

                            JSONObject obj = arry.getJSONObject(0);
                            GongdiNowBean bean  = new GongdiNowBean();
                            bean.name = obj.getString("name");
                            bean.pm10 = obj.getString("pm10"); tv_1.setText("PM10"); tv_1a.setText(bean.pm10);
                            bean.pm25 = obj.getString("pm25");tv_2.setText("PM2.5"); tv_2a.setText(bean.pm25);
                            bean.zaosheng = obj.getString("sound");tv_3.setText("噪声"); tv_3a.setText(bean.zaosheng);
                            bean.wendu = obj.getString("temp");tv_4.setText("温度"); tv_4a.setText(bean.wendu);
                            bean.shidu = obj.getString("shidu");tv_5.setText("湿度"); tv_5a.setText(bean.shidu);
                            bean.fengsu = obj.getString("fengsu");tv_6.setText("风速"); tv_6a.setText(bean.fengsu);
                            bean.fengxiang = obj.getString("fengxiang");tv_7.setText("风向"); tv_7a.setText(bean.fengxiang);
                            bean.qiye = obj.getString("qiye");
                            String ss = obj.getString("time");
//                            Date date = format1.parse(ss);
//                            bean.time = format2.format(date);
//                            table_list.add(bean);
                        }

                    } else {
                        ToastHelper.shortToast(context, response.getString("msg"));
                    }
                } catch (JSONException e) {
                    ToastHelper.shortToast(context, "数据解析错误");
                    //JSON数据格式错误
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
