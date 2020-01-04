package com.ruiao.tools.aqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.danyanghuawu.DanYanghuawuBean;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

//分钟数据
public class AqiFenActivity extends AppCompatActivity {
    Date data = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH 时");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("dd日HH时mm分");
    TimePickerView thisTime;
    //氮氧化物，按照天查询
    @BindView(R.id.table)
    SmartTable table;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;       //当前时间
    ArrayList<TableBean> beanlist = new ArrayList<>();
    long time_num;
    AqiBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aqi_fen);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        bean = (AqiBean) bundle.getSerializable("bean");
        tvNewDate.setText(format.format(data));
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowTableTitle(false);
        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 1) ;
        time_num = zero;
        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
    }

    @OnClick({R.id.tv_pre, R.id.tv_after,R.id.tv_newDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                time_num = time_num - 1000 * 3600 * 1;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                break;
            case R.id.tv_after:
                time_num = time_num + 1000 * 3600 * 1;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                break;
            case R.id.tv_newDate:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 1);
                        tvNewDate.setText( format.format(new Date(time_num)));
                        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                    }
                }).setType(new boolean[]{true, true, true, true, false, false}).setTitleText("请选择时间").build();
                thisTime.show();

                break;
        }
    }

    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("Start", start_time);
        pa.add("ActType", "1");
        pa.add("End", end_time);
        pa.add("username", (String) SPUtils.get(this, "username", ""));
        pa.add("Rows", "60");
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
                    beanlist.clear();
                    if (response.getBoolean("success")) {
//                        JSONArray arr_aqi = response.getJSONArray("aqi");
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
                        JSONArray time = response.getJSONArray("time");
                        JSONArray arr_fengxiang = response.getJSONArray("fengxiang");
                        for(int i = 0 ;i < time.length();i++){
                            TableBean bean = new TableBean();
                            String ss = time.getString(i);
                            Date date = format1.parse(ss);
                            bean.time = format2.format(date);
                            bean.aqi = "-";
                            bean.pm25 = ""+arr_pm25.getDouble(i);
                            bean.pm10 = ""+arr_pm10.getDouble(i);
                            bean.co = ""+arr_co.getDouble(i);
                            bean.fengsu =""+ arr_fengsu.getDouble(i);
                            bean.fengxiang = ""+arr_fengxiang.getDouble(i);
                            bean.no2 = ""+arr_no2.getDouble(i);
                            bean.so2 = ""+arr_so2.getString(i);
                            bean.o3 = ""+arr_o3.getDouble(i);
                            bean.qiya =""+ arr_press.getDouble(i);
                            bean.wendu =""+arr_temp.getDouble(i);
                            bean.shidu =""+ arr_shidu.getDouble(i);

                            beanlist.add(bean);
                        }
                        table.setData(beanlist);

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
