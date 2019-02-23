package com.ruiao.tools.voc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VocActivity extends AppCompatActivity {

    @BindView(R.id.fac_name)
    TextView facName;
    @BindView(R.id.dev_name)
    TextView devName;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;
    @BindView(R.id.voc1)
    TextView voc1;
    @BindView(R.id.voc2)
    TextView voc2;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    private String MonitorID = null;
    Date nowTime = new Date();
    ArrayList<String> fac_list = new ArrayList<>();
    ArrayList<ArrayList<String>> dev_list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> dev_adapter;
    Date hourTime = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);  //小时数据
    private boolean isDataReady = false;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean ready = false;
    ArrayList<ArrayList<DevBean>> biglist = new ArrayList<>();
    ArrayList<FactroyBean> little = new ArrayList<>();
    Context context;

    private Pbdialog dialog; //进度条
    private LineChartManager lineChartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_voc);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        lineChartManager = new LineChartManager(lineChart);
        lineChartManager.initChart();
        initFacDev();
    }

    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }

    private void initFacDev() {
        dialog = showdialog(this, "正在加载.......");
        RequestParams pa = new RequestParams();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fac_list);
        pa.add("username", (String) SPUtils.get(this, "username", ""));
        pa.add("SID", "5");
        AsynHttpTools.httpGetMethodByParams(URLConstants.FAC, pa, new JsonHttpResponseHandler("GB2312") {
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
                finish();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        dev_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dev_list.get(bwhich));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_lonch);
        builder.setTitle("选择设备：");
        builder.setAdapter(dev_adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = biglist.get(bwhich).get(which).getId(); //获取设备ID
                MonitorID = id;
                facName.setText(little.get(bwhich).getFacname());
                devName.setText(biglist.get(bwhich).get(which).getDevNane());
//                MonitorID = MonitorID;
                isDataReady = true;
                initNowData(id);
                initData(id);
            }
        });
        builder.show();
    }

    private void initNowData(String MonitorID) {
        RequestParams pa = new RequestParams();
//        pa.add("start", format.format(new Date(hourTime.getTime() - 2 * 60 * 1000)));
//        pa.add("end", format.format(hourTime));
//        pa.add("type", "fen");
        pa.add("MonitorID", MonitorID);
//        pa.add("username", (String)SPUtils.get(ICActivity.this,"username",""));
        AsynHttpTools.httpGetMethodByParams(URLConstants.NEW_IC, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int code, Header[] heads, Throwable throwable, JSONObject json) {

                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        tvNewDate.setText("最新数据：刷新时间:" + response.getString("MonitorTime") + "(点击切换企业)");
                        String base = (String) SPUtils.get(context, "BASE", "");
                        if (base.startsWith("http://222.222.220.218")) {   //"晋州"
                            voc1.setText("" + response.getDouble("VOC1"));
                            voc2.setText("" + response.getString("VOC2"));
                        }


                    } else {
                        ToastHelper.shortToast(context, response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    private void initData(String MonitorID) {
        dialog = showdialog(this, "正在加载.......");
        RequestParams pa = new RequestParams();
        pa.add("start", format.format(new Date(hourTime.getTime() - 12 * 60 * 60 * 1000)));
        pa.add("end", format.format(hourTime));
        pa.add("type", "xiaoshi");
        pa.add("MonitorID", MonitorID);
        pa.add("username", (String) SPUtils.get(context, "username", ""));
        AsynHttpTools.httpGetMethodByParams(URLConstants.IC, pa, new JsonHttpResponseHandler("GB2312") {
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
                int address = 0; //1为晋州
                // list 按照时间排列
                ArrayList<VocBean> beans = new ArrayList<>();

                try {
                    String base = (String) SPUtils.get(context, "BASE", "");
                    if (base.startsWith("http://222.222.220.218")) {   //"晋州"
                        address = 1;
                    }

                    lineChartManager.setAddress(address);
                    Boolean status = response.getBoolean("success");
                    if (status) {
                        beans.clear();
                        VocBean bean = new VocBean();

                        JSONArray voclist1 = response.getJSONArray("VOC1平均值");
                        JSONArray voclist2 = response.getJSONArray("VOC2平均值");

                        JSONObject vocbean1 = null;
                        JSONObject vocbean2 = null;
                        for(int i = 0; i < voclist1.length(); i++){
                            vocbean1 = voclist1.getJSONObject(i);
                            vocbean2 = voclist2.getJSONObject(i);
                            bean.date = vocbean1.getString("id");
                            bean.voc1 = vocbean1.getString("value");
                            bean.voc2 = vocbean2.getString("value");
                            beans.add(bean);
                        }
                        lineChartManager.setData(beans);
                    } else {
                        ToastHelper.shortToast(context, response.getString("message"));
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

    @OnClick({R.id.ll_select_dev, R.id.histroy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_dev:
                initPickerView();
                break;
            case R.id.histroy:
                Intent intent = new Intent(context,VocHistroyActivity.class);
                intent.putExtra("MonitorID",MonitorID);
                startActivity(intent);
                break;
        }
    }
}
