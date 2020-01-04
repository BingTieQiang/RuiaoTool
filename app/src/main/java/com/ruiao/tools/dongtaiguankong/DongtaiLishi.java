package com.ruiao.tools.dongtaiguankong;

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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.dongtaiguankong.histroy.DongtaiFenActivity;
import com.ruiao.tools.dongtaiguankong.histroy.DongtaiTianActivity;
import com.ruiao.tools.dongtaiguankong.histroy.DongtaiZhouActivity;
import com.ruiao.tools.gongdiyangceng.DayActivity;
import com.ruiao.tools.gongdiyangceng.GongdiNowBean;
import com.ruiao.tools.gongdiyangceng.LineChartManager;
import com.ruiao.tools.gongdiyangceng.ShiActivity;
import com.ruiao.tools.gongdiyangceng.ShifenActivity;
import com.ruiao.tools.ic_card2.DevBean;
import com.ruiao.tools.ic_card2.FactroyBean;
import com.ruiao.tools.ui.GuankongEchartView;
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

public class DongtaiLishi extends Fragment {
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
    @BindView(R.id.tv_5)
    TextView tv_5;
    @BindView(R.id.tv_6)
    TextView tv_6;
    @BindView(R.id.tv_7)
    TextView tv_7;
    @BindView(R.id.tv_8)
    TextView tv_8;
    @BindView(R.id.tv_9)
    TextView tv_9;
    @BindView(R.id.tv_10)
    TextView tv_10;
    @BindView(R.id.tv_4a)
    TextView tv_4a;
    @BindView(R.id.tv_5a)
    TextView tv_5a;
    @BindView(R.id.tv_6a)
    TextView tv_6a;
    @BindView(R.id.tv_7a)
    TextView tv_7a;
    @BindView(R.id.tv_8a)
    TextView tv_8a;
    @BindView(R.id.tv_9a)
    TextView tv_9a;
    @BindView(R.id.tv_10a)
    TextView tv_10a;


    private Pbdialog dialog; //进度条

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
                Intent intent1 = new Intent(getContext(), DongtaiFenActivity.class);
                intent1.putExtra("MonitorID",MonitorID);
                startActivity(intent1);
                break;
            case R.id.ll_xiaoshi:
                Intent intent2 = new Intent(getContext(),DongtaiTianActivity.class);
                intent2.putExtra("MonitorID",MonitorID);
                startActivity(intent2);
                break;
            case R.id.ll_zhou:
                Intent intent3 = new Intent(getContext(), DongtaiZhouActivity.class);
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
        AsynHttpTools.httpGetMethodByParams(URLConstants.Dongtai_FAC, pa, new JsonHttpResponseHandler("GB2312") {
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
        pa.add("start", format.format(new Date(nowTime.getTime() - 5 * 60 * 1000)));
        pa.add("end", format.format(nowTime));
        pa.add("ActType", "1");
        pa.add("MonitorID", MonitorID);
        pa.add("username", (String) SPUtils.get(getContext(), "username", ""));
        AsynHttpTools.httpGetMethodByParams(URLConstants.Dongtai_Histry, pa, new JsonHttpResponseHandler("GB2312") {
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


                    Boolean status = response.getBoolean("success");
                    if (status) {
                        if(response.has("废气")){

                            tv_1.setText("废气");  tv_1a.setText(response.getJSONArray("废气").getString(0));
                            tv_2.setText("烟尘");  tv_2a.setText(response.getJSONArray("烟尘").getString(0));
                            tv_3.setText("烟尘折算");  tv_3a.setText(response.getJSONArray("烟尘折算").getString(0));
                            tv_4.setText("二氧化硫");  tv_4a.setText(response.getJSONArray("二氧化硫").getString(0));
                            tv_5.setText("二氧化硫折算");  tv_5a.setText(response.getJSONArray("二氧化硫折算").getString(0));
                            tv_6.setText("氮氧化物");  tv_6a.setText(response.getJSONArray("氮氧化物").getString(0));
                            tv_7.setText("氮氧化物折算");  tv_7a.setText(response.getJSONArray("氮氧化物折算").getString(0));
                            tv_8.setText("氧气含量");  tv_8a.setText(response.getJSONArray("氧气含量").getString(0));
                            tv_9.setText("烟气流速");  tv_9a.setText(response.getJSONArray("烟气流速").getString(0));
                            tv_10.setText("烟气温度");  tv_10a.setText(response.getJSONArray("烟气温度").getString(0));
//                            tv_1.setText("烟气压力");  tv_1a.setText(response.getJSONArray("烟气压力").getString(0));

                        }else {
                            tv_1.setText("污水");  tv_1a.setText(response.getJSONArray("污水").getString(0));
                            tv_2.setText("化学需氧量");  tv_2a.setText(response.getJSONArray("化学需氧量").getString(0));
                            tv_3.setText("氨氮");  tv_3a.setText(response.getJSONArray("氨氮").getString(0));
                            tv_4.setText("总氮");  tv_4a.setText(response.getJSONArray("总氮").getString(0));
                            tv_5.setText("pH");  tv_5a.setText(response.getJSONArray("pH").getString(0));

                        }


                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
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
