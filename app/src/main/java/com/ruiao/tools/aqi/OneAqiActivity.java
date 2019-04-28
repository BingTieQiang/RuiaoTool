package com.ruiao.tools.aqi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.KeyRadioGroupV1;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

public class OneAqiActivity extends AppCompatActivity {
    private AqiBean bean;
    @BindView(R.id.krg_main_1)
    KeyRadioGroupV1 group;
    @BindView(R.id.tv_city)
    TextView tvCity;                //地区
    @BindView(R.id.tv_time)
    TextView tvTime;               //时间
    @BindView(R.id.arr_aqi)
    TextView arrAqi;              //
    @BindView(R.id.arr_pm25)
    TextView arrPm25;            //
    @BindView(R.id.arr_pm10)
    TextView arrPm10;
    @BindView(R.id.arr_co)
    TextView arrCo;
    @BindView(R.id.arr_no2)
    TextView arrNo2;
    @BindView(R.id.arr_so2)
    TextView arrSo2;
    @BindView(R.id.arr_o3)
    TextView arrO3;
    @BindView(R.id.arr_fengsu)
    TextView arrFengsu;
    @BindView(R.id.arr_fengxiang)
    TextView arrFengxiang;
    @BindView(R.id.arr_shidu)
    TextView arrShidu;
    @BindView(R.id.arr_press)
    TextView arrPress;
    @BindView(R.id.arr_temp)
    TextView arrTemp;
    @BindView(R.id.Chart)
    BarChart Chart;
    BarChartManager manager;

    ArrayList<ArrayList<BarEntry>> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_aqi);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        bean = (AqiBean) bundle.getSerializable("bean");
        group.setOnCheckedChangeListener(new KeyRadioGroupV1.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(KeyRadioGroupV1 group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_aqi:
                        manager.setData(lists.get(0));
                        break;
                    case R.id.btn_pm10:
                        manager.setData(lists.get(1));
                        break;
                    case R.id.btn_pm25:
                        manager.setData(lists.get(2));
                        break;
                    case R.id.btn_co:
                        manager.setData(lists.get(3));
                        break;
                    case R.id.btn_fengsu:
                        manager.setData(lists.get(4));
                        break;
                    case R.id.btn_no2:
                        manager.setData(lists.get(5));
                        break;
                    case R.id.btn_so2:
                        manager.setData(lists.get(6));
                        break;

                    case R.id.btn_o3:
                        manager.setData(lists.get(7));
                        break;
                    case R.id.btn_press:
                        manager.setData(lists.get(8));
                        break;
                    case R.id.btn_temp:
                        manager.setData(lists.get(9));
                        break;
                    case R.id.btn_shiidu:
                        manager.setData(lists.get(10));
                        break;
                }
            }
        });
        manager = new BarChartManager(Chart);
        manager.initLineChart("十二小时数据");
        initNowData();
        initData();
    }

    private void initNowData() {
        arrAqi.setText(bean.aqi);
        arrPm10.setText(bean.pm10);
        arrPm25.setText(bean.pm25);
        arrCo.setText(bean.co);
        arrFengsu.setText(bean.fengsu);
        arrFengxiang.setText(bean.fengxiang);
        arrNo2.setText(bean.no2);
        arrSo2.setText(bean.so2);
        arrO3.setText(bean.o3);
        arrPress.setText(bean.press);
        arrTemp.setText(bean.temp);
        arrShidu.setText(bean.shidu);
        tvCity.setText(bean.name);
    }

    private void initData() {
        RequestParams pa = new RequestParams();
        pa.add("username", "admin");
        pa.add("MonitorID", bean.MonitorID);
        HttpUtil.get(URLConstants.AQI1, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                    lists.clear();
                    if (response.getBoolean("success")) {

                        JSONArray arr_aqi = response.getJSONArray("aqi");
                        JSONArray arr_pm10 = response.getJSONArray("pm10");
                        JSONArray arr_pm25 = response.getJSONArray("pm25");
                        JSONArray arr_co = response.getJSONArray("co");
                        JSONArray arr_fengsu = response.getJSONArray("fengsu");
                        JSONArray arr_no2 = response.getJSONArray("no2");
                        JSONArray arr_so2 = response.getJSONArray("so2");
                        JSONArray arr_o3 = response.getJSONArray("o3");
                        JSONArray arr_press = response.getJSONArray("press");
                        JSONArray arr_temp = response.getJSONArray("temp");
                        JSONArray arr_shidu = response.getJSONArray("shidu");

                        ArrayList<BarEntry> aqilist = new ArrayList<>();
                        for (int i = 0; i < arr_aqi.length(); i++) {
                            aqilist.add(new BarEntry(i, (float) arr_aqi.getDouble(i)));
                        }
                        lists.add(aqilist);

                        ArrayList<BarEntry> pm10list = new ArrayList<>();
                        for (int i = 0; i < arr_pm10.length(); i++) {
                            pm10list.add(new BarEntry(i, (float) arr_pm10.getDouble(i)));
                        }
                        lists.add(pm10list);

                        ArrayList<BarEntry> pm25list = new ArrayList<>();
                        for (int i = 0; i < arr_pm25.length(); i++) {
                            pm25list.add(new BarEntry(i, (float) arr_pm10.getDouble(i)));
                        }
                        lists.add(pm25list);

                        ArrayList<BarEntry> colist = new ArrayList<>();
                        for (int i = 0; i < arr_co.length(); i++) {
                            colist.add(new BarEntry(i, (float) arr_co.getDouble(i)));
                        }
                        lists.add(colist);

                        ArrayList<BarEntry> fengsulist = new ArrayList<>();
                        for (int i = 0; i < arr_fengsu.length(); i++) {
                            fengsulist.add(new BarEntry(i, (float) arr_fengsu.getDouble(i)));
                        }
                        lists.add(fengsulist);

                        ArrayList<BarEntry> no2list = new ArrayList<>();
                        for (int i = 0; i < arr_no2.length(); i++) {
                            no2list.add(new BarEntry(i, (float) arr_no2.getDouble(i)));
                        }
                        lists.add(no2list);

                        ArrayList<BarEntry> so2list = new ArrayList<>();
                        for (int i = 0; i < arr_so2.length(); i++) {
                            so2list.add(new BarEntry(i, (float) arr_so2.getDouble(i)));
                        }
                        lists.add(so2list);

                        ArrayList<BarEntry> o3list = new ArrayList<>();
                        for (int i = 0; i < arr_o3.length(); i++) {
                            o3list.add(new BarEntry(i, (float) arr_o3.getDouble(i)));
                        }
                        lists.add(o3list);

                        ArrayList<BarEntry> presslist = new ArrayList<>();
                        for (int i = 0; i < arr_press.length(); i++) {
                            presslist.add(new BarEntry(i, (float) arr_press.getDouble(i)));
                        }
                        lists.add(presslist);

                        ArrayList<BarEntry> templist = new ArrayList<>();
                        for (int i = 0; i < arr_temp.length(); i++) {
                            templist.add(new BarEntry(i, (float) arr_temp.getDouble(i)));
                        }
                        lists.add(templist);

                        ArrayList<BarEntry> shidulist = new ArrayList<>();
                        for (int i = 0; i < arr_shidu.length(); i++) {
                            shidulist.add(new BarEntry(i, (float) arr_shidu.getDouble(i)));
                        }
                        lists.add(shidulist);

                        manager.setData(lists.get(0));
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick({R.id.ll_zhandian, R.id.ll_24, R.id.ll_tian, R.id.ll_zhou, R.id.ll_yue})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(),AqiHistroyActivity.class);
        switch (view.getId()) {
            case R.id.ll_zhandian:
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean",bean);
                intent = new Intent(getContext(),ZhandianActivity.class);
                intent.putExtra("bundle",bundle);
                break;
            case R.id.ll_24:
                intent.putExtra("time","24");
                Bundle bundlex = new Bundle();
                bundlex.putSerializable("bean",bean);
                intent.putExtra("bundle",bundlex);
                break;
            case R.id.ll_tian:
                intent.putExtra("time","24");
                break;
            case R.id.ll_zhou:
                intent.putExtra("time","24");
                break;
            case R.id.ll_yue:
                intent.putExtra("time","24");
                break;

        }
        startActivity(intent);
    }
}
