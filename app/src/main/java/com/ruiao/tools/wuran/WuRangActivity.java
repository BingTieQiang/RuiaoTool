package com.ruiao.tools.wuran;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//污染源
public class WuRangActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> dev_adapter;
    ArrayList<String> fac_list = new ArrayList<>();
    ArrayList<ArrayList<String>> dev_list = new ArrayList<>();
    //    @BindView(R.id.arr1context)
//    TextView arr1context;
    private int devtype;
    private boolean ready = false;
    private boolean isDataReady = false;
    ArrayList<ArrayList<DevBean>> biglist = new ArrayList<>();
    ArrayList<FactroyBean> little = new ArrayList<>();
    private Pbdialog dialog; //进度条
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_change_city)
    TextView tvChangeCity;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;
    @BindView(R.id.fac_name)
    TextView facName;
    @BindView(R.id.dev_name)
    TextView devName;
    @BindView(R.id.arr1)
    TextView arr1;
    @BindView(R.id.arrcontext1)
    TextView arrcontext1;
    @BindView(R.id.arr2)
    TextView arr2;
    @BindView(R.id.arr2context)
    TextView arr2context;
    @BindView(R.id.arr3)
    TextView arr3;
    @BindView(R.id.arr3context)
    TextView arr3context;
    @BindView(R.id.arr4)
    TextView arr4;
    @BindView(R.id.arr4context)
    TextView arr4context;
    @BindView(R.id.arr5)
    TextView arr5;
    @BindView(R.id.arr5context)
    TextView arr5context;
    @BindView(R.id.arr6)
    TextView arr6;
    @BindView(R.id.arr6context)
    TextView arr6context;
    @BindView(R.id.arr7)
    TextView arr7;
    @BindView(R.id.arr7context)
    TextView arr7context;

    private Context context;
    OptionsPickerView builder = null;
    private String MonitorID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_wu_rang);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 污染源           1
     * 工地扬尘        2
     * 重点园区监测 3
     * 水质自动站    4
     * VOC监测      5
     * 厂界监测       6
     * 空气自动站    8
     * IC卡总量控制 12
     * 空气微型站    14
     * 油烟在线监测 15
     */
    private void initData() {
        dialog = showdialog(this, "正在加载.......");

        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,fac_list);

        //1，请求厂家，设备，列表
        //2，请求最新数据
        //1开头是气，2开头是水
        RequestParams pa = new RequestParams();
        pa.add("username", (String) SPUtils.get(context, "username", ""));
        pa.add("SID", "1");
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

                            fac_list.add(facname);

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
                            biglist.add(mlist);
                            dev_list.add(temp);
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

    private void initView() {
        StatusBarUtil.darkMode(this);

    }

    /**
     * 初始化工厂，设备  选择器
     */
    private void initPickerView() {
        if (!ready) {
            return;
        }
//        builder = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                String id = biglist.get(options1).get(options2).getId(); //获取设备ID
//                MonitorID = id;
//                facName.setText(little.get(options1).getFacname());
//                devName.setText(biglist.get(options1).get(options2).getDevNane());
//                WuRangActivity.this.MonitorID = MonitorID;
//                isDataReady = true;
//                initNowData();
////                initData(id);
//            }
//        }).setSubmitText("确定").setCancelText("取消").setOutSideCancelable(false).build();
//        builder.setPicker(little, biglist);
//        builder.setTitleText("选择设备");
//        builder.setKeyBackCancelable(false);
//        builder.show();
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
    public void showDev(final int bwhich){
        dev_adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,dev_list.get(bwhich));
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
                WuRangActivity.this.MonitorID = MonitorID;
                isDataReady = true;
                initNowData();
            }
        });
        builder.show();
    }
    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }

    private void initNowData() {
        RequestParams pa = new RequestParams();
        pa.add("MonitorID", MonitorID);
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
                        tvNewDate.setText("最新数据 刷新时间:" + response.getString("MonitorTime")+"(点击切换企业)");

                        if (MonitorID.startsWith("1")) {       //1开头是气 7参数，2开头是水 5参数
                            arr1.setText("烟气流量(mg/m3)");
                            arrcontext1.setText("" + response.getDouble("烟气流量"));
                            arr2.setText("烟尘浓度(mg/m3)");
                            arr2context.setText("" + response.getDouble("烟尘浓度"));
                            arr3.setText("烟尘折算(mg/m3)");
                            arr3context.setText("" + response.getDouble("烟尘折算"));
                            arr4.setText("二氧化硫(mg/m3)");
                            arr4context.setText("" + response.getDouble("二氧化硫"));
                            arr5.setText("二氧化硫折算(mg/m3)");
                            arr5context.setText("" + response.getDouble("二氧化硫折算"));
                            arr6.setText("氮氧化物(mg/m3)");
                            arr6context.setText("" + response.getDouble("氮氧化物"));
                            arr7.setText("氮氧化物折算(mg/m3)");
                            arr7context.setText("" + response.getDouble("氮氧化物折算"));
                            devtype = 1;
                        } else {
                            arr1.setText("流量(L/s)");
                            arrcontext1.setText("" + response.getDouble("流量"));
                            arr2.setText("NH3N(mg/L)");
                            arr2context.setText("" + response.getDouble("NH3N"));
                            arr3.setText("总氮(mg/L)");
                            arr3context.setText("" + response.getDouble("总氮"));
                            arr4.setText("总磷(mg/L)");
                            arr4context.setText("" + response.getDouble("总磷"));
                            arr5.setText("COD(mg/L)");
                            arr5context.setText("" + response.getDouble("COD"));

                            arr6.setText("参数");
//                            arr6context.setText("" + response.getDouble("氮氧化物"));
                            arr7.setText("参数");
//                            arr7context.setText("" + response.getDouble("氮氧化物折算"));
                            devtype = 2;
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

    @OnClick({R.id.ll_select_dev, R.id.histroy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_dev:
                initPickerView();
                break;
            case R.id.histroy:
                if(isDataReady) {
                    Intent intent = new Intent(context,WuRanHistroyActivity.class);
                    intent.putExtra("MonitorID",MonitorID);
                    intent.putExtra("devtype",""+devtype);
                    startActivity(intent);
                }else{
                    ToastHelper.shortToast(context,"请选择企业和设备");
                }
                break;
        }
    }

}
