package com.ruiao.tools.the;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.StatusBarUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.youyan.YouyanBean;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

public class FengzhongHistroyActivity extends AppCompatActivity {
    @BindView(R.id.spinter)
    Spinner spinner;
    int point = 4; //监测点
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
    ArrayList<FenbiaoBean> table_list = new ArrayList<>();
    long time_num;
    String MonitorID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fenbaio_shifen);
        ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        MonitorID = getIntent().getStringExtra("MonitorID");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                point = position+4;
                chose();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowTableTitle(false);
        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1000 * 3600 * 24;
        time_num = zero;

        tvNewDate.setText(format.format(new Date(time_num)));
        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
    }

    private void chose() {
        thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 2);
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
            }
        }).setType(new boolean[]{true, true, true, true, false, false}).setTitleText("请选择时间").build();
        thisTime.show();
    }

    @OnClick({R.id.tv_pre, R.id.tv_after,R.id.tv_newDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pre:
                time_num = time_num - 1000 * 3600 * 2;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
                break;
            case R.id.tv_after:
                time_num = time_num + 1000 * 3600 * 2;
                tvNewDate.setText( format.format(new Date(time_num)));
                initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
                break;
            case R.id.tv_newDate:
                thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 2);
                        tvNewDate.setText( format.format(new Date(time_num)));
                        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 2)));
                    }
                }).setType(new boolean[]{true, true, true, true, false, false}).setTitleText("请选择时间").build();
                thisTime.show();

                break;
        }
    }

    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("DevID", ""+point);
        pa.add("start", start_time);
        pa.add("end", end_time);

//        pa.add("type", "fen");
        HttpUtil.get(URLConstants.Fenbiao_HISTROY, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                    table_list.clear();
                    if (true) {
                        JSONArray array =  response.getJSONArray("data");
                        for(int i = 0; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            FenbiaoBean bean  = new FenbiaoBean();
                            String ss = obj.getString("time");
                            Date date = format1.parse(ss);
                            bean.time = format2.format(date);

                            bean.Ua = obj.getString("A相电压");
                            bean.Ub = obj.getString("B相电压");
                            bean.Uc = obj.getString("C相电压");
                            bean.Ia = obj.getString("A相电流");
                            bean.Ib = obj.getString("B相电流");
                            bean.Ic = obj.getString("C相电流");

                            bean.Dianneng4 = obj.getString("正向有功电能(3/4)");
                            bean.Dianneng3 = obj.getString("正向有功电能(3/3)");
                            bean.Pa = obj.getString("A相有功功率");
                            bean.Pb = obj.getString("B相有功功率");
                            bean.Pc = obj.getString("C相有功功率");
                            bean.Pzong = obj.getString("总功率因数");
                            table_list.add(bean);
                        }
                        table.setData(table_list);
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
