package com.ruiao.tools.fenbiao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.example.pickerviewlibrary.picker.TeaPickerView;
import com.example.pickerviewlibrary.picker.entity.PickerData;
import com.example.pickerviewlibrary.picker.listener.OnPickerClickListener;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.the.PageChangeLinster;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2019/3/1.
 */

public class FengbiaoLishi extends Fragment implements PageChangeLinster {
    List<String> first = new ArrayList<>();
    Map<String, List<String>> mSecondDatas= new HashMap<>();
    Map<String, List<String>> mThirdDatas= new HashMap<>();

    PickerData pickerData=new PickerData();
    Unbinder unbinder;
    @BindView(R.id.table)
    SmartTable table;
    Date data = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH 时");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("HH时mm分");
    TimePickerView thisTime;
    JSONObject result;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;
    @BindView(R.id.txt_setting)
    TextView txt_setting;
    @BindView(R.id.tv_gongsiming)
    TextView tv_gongsiming;       //
    ArrayList<FenbiaoBean> beanlist = new ArrayList<>();
    long time_num;
    FenbiaoBean bean;
    TeaPickerView teaPickerView;
    private String partitionId = "",deviceId = "";
    private int delFlag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lishi, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvNewDate.setText(format.format(data));
        table.getConfig().setShowYSequence(false);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowTableTitle(false);
        long zero =  System.currentTimeMillis()- ( System.currentTimeMillis()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 1) ;
        time_num = zero;

        show();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setInfo(String id) {

    }

    private void initChart(String start_time,String end_time) {
        RequestParams pa = new RequestParams();
        pa.add("Start", start_time);
        pa.add("MonitorID", partitionId);
        pa.add("End", end_time);
        pa.add("username", (String) SPUtils.get(getContext(), "username", ""));
//        pa.add("Rows", "60");
//        pa.add("MonitorID", bean.MonitorID);
        HttpUtil.get(URLConstants.Fenbiao_Histroy, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                        JSONArray arr = response.getJSONArray("data");

                        for(int i = 0 ;i < arr.length();i++){
                            JSONObject obj = arr.getJSONObject(i);
                            FenbiaoBean bean = new FenbiaoBean();
//                            bean.time = obj.getString("time");
                            String timeStr = obj.getString("time");
                            Date date = format1.parse(timeStr);
                            bean.time = format2.format(date);
                            bean.av = obj.getString("A相电压");
                            bean.bv = obj.getString("B相电压");
                            bean.cv = obj.getString("C相电压");
                            bean.ai = obj.getString("A相电流");
                            bean.bi = obj.getString("B相电流");
                            bean.ci = obj.getString("C相电流");
                            bean.dianneg = obj.getString("正向有功电能");
                            bean.aw = obj.getString("A相有功功率");
                            bean.bw = obj.getString("B相有功功率");
                            bean.cw = obj.getString("C相有功功率");
                            bean.gonglv = obj.getString("总功率因数");
                            beanlist.add(bean);
                        }
                        table.setData(beanlist);
//
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void show() {
        RequestParams pa = new RequestParams();
        pa.put("username",(String) SPUtils.get(getContext(), "username", ""));
        HttpUtil.get(URLConstants.Fenbiao_Company,pa,new HttpUtil.SimpJsonHandle(getContext()){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                result = response;
                try {
                    JSONArray array = response.getJSONArray("factory");
                    first.clear();
                    for(int i = 0 ;i < array.length() ; i++){
                        JSONObject obj = array.getJSONObject(i);
                        String comname = obj.getString("name");
                        first.add(comname);
                        JSONArray array2 = obj.getJSONArray("device");
                        ArrayList<String> list = new ArrayList<>();
                        list.clear();
                        for(int y = 0 ;y < array2.length() ; y++){
                            JSONObject obj2 = array2.getJSONObject(y);
                            String deviceName = obj2.getString("name");
                            list.add(deviceName);

                            ArrayList<String> lists = new ArrayList<>();
                            JSONArray parts = obj2.getJSONArray("device"); //分区对象
                            lists.clear();
                            for(int z = 0;z< parts.length() ; z++){
                                JSONObject part = parts.getJSONObject(z);
                                lists.add(deviceName+":"+part.getString("name"));

                                mSecondDatas.put(comname,lists);
                            }
//                            mThirdDatas.put(deviceName,lists);

                        }
//                        mSecondDatas.put(comname,list);
                    }

                    pickerData.setFirstDatas(first);
                    pickerData.setSecondDatas(mSecondDatas);
//                    pickerData.setThirdDatas(mThirdDatas);
                    pickerData.setInitSelectText("请选择");
                    teaPickerView =new TeaPickerView(getActivity(),pickerData);
                    teaPickerView.setScreenH(1)
                            .setDiscolourHook(true)
                            .setRadius(25)
                            .setContentLine(true)
                            .setRadius(25)
                            .build();
                    teaPickerView.show(txt_setting);
                    teaPickerView.setOnPickerClickListener(new OnPickerClickListener() {
                        @Override
                        public void OnPickerClick(PickerData pickerData) {
                            String firsttext = pickerData.getFirstText();
                            String secondtext = pickerData.getSecondText();
                            String secondtextx = pickerData.getSecondText();
                            String thirdtext;



                            int index1 = first.indexOf(firsttext);
                            ArrayList<String> list2 = (ArrayList<String>) mSecondDatas.get(firsttext);
                            int index2 = list2.indexOf(secondtext);
                            String[] strin1 = secondtext.split(":");
                            secondtext = strin1[0];
                            thirdtext = strin1[1];
                            try {
                                JSONArray arr = result.getJSONArray("factory");

                                JSONObject obj = arr.getJSONObject(index1);  //设备区
                                JSONArray arr2 = obj.getJSONArray("device"); //设备列表
                                for(int i = 0 ;i<arr2.length();i++){
                                    JSONObject obj4 = arr2.getJSONObject(i);
                                    String devicenam = obj4.getString("name");
                                    if(devicenam.equals(secondtext)){
                                        JSONArray arrend = obj4.getJSONArray("device");
                                        for(int y = 0;y<arrend.length();y++){
                                            JSONObject obj5 = arrend.getJSONObject(y);
                                            String endname = obj5.getString("name");
                                            if(endname.equals(thirdtext)){
                                                partitionId = obj5.getString("id");
                                            }

                                        }
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            teaPickerView.dismiss();
                            tv_gongsiming.setText(firsttext+" "+secondtextx);
                            initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @OnClick({R.id.tv_pre, R.id.tv_after,R.id.tv_newDate,R.id.txt_setting})
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
                thisTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time_num = date.getTime() - ( date.getTime()+ TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 1);
                        tvNewDate.setText( format.format(new Date(time_num)));
                        initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                    }
                }).setType(new boolean[]{true, true, true, true, false, false}).setTitleText("请选择时间").build();
                thisTime.show();
                break;
            case R.id.txt_setting:
                show();
                break;
        }
    }
}
