package com.ruiao.tools.gongdiyangceng;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.JsonArray;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.ic_card2.DevBean;
import com.ruiao.tools.ic_card2.FactroyBean;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AsynHttpTools;
import com.ruiao.tools.utils.SPUtils;
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

public class GongdiLishi extends Fragment {
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
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.tv_newDate)
    TextView facName;
    private Pbdialog dialog; //进度条
    private LineChartManager lineChartManager;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ArrayList<ArrayList<DevBean>> biglist = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_guankong_lishi, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        lineChartManager = new LineChartManager(lineChart);
        lineChartManager.initChart();
        initData();
    }

    private void initData() {
        initFacDev();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @OnClick({R.id.ll_shifenzhong, R.id.ll_xiaoshi, R.id.ll_zhou,R.id.ll_select_dev})
    public void onViewClicked(View view) {
        if(!isDataReady){
            return;
        }
        switch (view.getId()) {
            case R.id.ll_shifenzhong:
                Intent intent1 = new Intent(getContext(),ShifenActivity.class);
                intent1.putExtra("MonitorID",MonitorID);
                startActivity(intent1);
                break;
            case R.id.ll_xiaoshi:
                Intent intent2 = new Intent(getContext(),ShiActivity.class);
                intent2.putExtra("MonitorID",MonitorID);
                startActivity(intent2);
                break;
            case R.id.ll_zhou:
                Intent intent3 = new Intent(getContext(),DayActivity.class);
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
        dialog = showdialog(getContext(), "正在加载.......");
        RequestParams pa = new RequestParams();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, fac_list);
        pa.add("username", (String) SPUtils.get(getContext(), "username", ""));
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
                        ToastHelper.shortToast(getContext(), response.getString("message"));

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        dev_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dev_list.get(bwhich));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        dialog = showdialog(getContext(), "正在加载.......");
        RequestParams pa = new RequestParams();
        pa.add("start", format.format(new Date(hourTime.getTime() - 24 * 60 * 60 * 1000)));
        pa.add("end", format.format(hourTime));
        pa.add("type", "xiaoshi");
        pa.add("MonitorID", MonitorID);
        pa.add("username", (String) SPUtils.get(getContext(), "username", ""));
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
                            GongdiNowBean bean =  new GongdiNowBean();
                            JSONObject obj = arry.getJSONObject(i);
                            bean.time  = obj.getString("time");
                            bean.pm10 = obj.getString("pm10");
                            beans.add(bean);
                        }
                        lineChartManager.setData(beans);
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("msg"));
                    }
                } catch (JSONException e) {
                    ToastHelper.shortToast(getContext(), "数据解析错误");
                    //JSON数据格式错误
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
