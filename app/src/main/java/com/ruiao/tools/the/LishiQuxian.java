package com.ruiao.tools.the;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;

import com.ruiao.tools.ui.EchartOptionUtil;
import com.ruiao.tools.ui.EchartView;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.Pbdialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2019/3/1.
 */

public class LishiQuxian extends Fragment implements PageChangeLinster{
    private Pbdialog dialog; //进度条
    @BindView(R.id.tv_gongsiming)
    TextView tvGongsiming;
    @BindView(R.id.tv_time_result)
    TextView tvTimeResult;
    @BindView(R.id.basi_list)
    WrapContentListView basiList;
    DanzhanAdapter adapter = null;
    @BindView(R.id.echartview)
    EchartView echartview;
    @BindView(R.id.echartview2)
    EchartView echartview2;
    boolean hasFinish = false;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
    private SimpleDateFormat formatx = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat formatres = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected TimePickerView thisTime;


    //    ArrayList<TaskListAdapter.Dongtaibin> list = new ArrayList<>();
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_qiye)
    TextView tvQiye;
    @BindView(R.id.txt_setting)
    TextView txtSetting;
    @BindView(R.id.ll_serch)
    LinearLayout llSerch;
    Unbinder unbinder;
    private String start = "";
    private String end = "";
    private String qiye = "3";

    private ArrayList<String> fac_list = new ArrayList<>();
    private ArrayList<String> dev_list = new ArrayList<>();
    private ArrayList<String> dev_id = new ArrayList<>();
    ArrayList<GongkuangBean> beans = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_dongta1, container, false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new DanzhanAdapter(getContext(), beans);
        basiList.setAdapter(adapter);
        echartview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                refreshLineChart();
                hasFinish = true;
            }
        });
        dialog = showdialog(getContext(), "正在加载.......");
        return view;
    }

    private void refreshLineChart() {
        Date d_end = new Date();
        Date d_start = new Date(d_end.getTime() - 15 * 60 * 1000);
        upData(formatres.format(d_start), formatres.format(d_end));
        tvTimeResult.setText(formatres.format(d_start) + "->" + formatres.format(d_end));
    }

    @Override
    public void onResume() {
        super.onResume();
//        upData("2019-03-09 01:50:04","2019-03-10 00:50:07");


    }
    /*
     if(activity.id .equals( "3")){
                                if("昌兴五金丝网制品厂".equals(obj.getString("企业名称"))){
                                    list.add(bean);
                                }
                            }else if(activity.id .equals( "9")){
                                if("石家庄宝康生物工程有限公司".equals(obj.getString("企业名称"))){
                                    list.add(bean);
                                }
                            }else if(activity.id .equals( "10")){
                                if("晋州市中兴装饰用布有限公司".equals(obj.getString("企业名称"))){
                                    list.add(bean);
                                }
                            }
     */

    public void upData(String star, String end)  {

        RequestParams pa = new RequestParams();
        pa.add("username", (String) SPUtils.get(getContext(), "username1", ""));
        pa.add("start", star);
        pa.add("end", end);
        pa.put("Page", 1);
        pa.put("Rows", 60);
        TheActivity activity = (TheActivity)getActivity() ;
        pa.add("DevID", activity.id);

        if( activity.id .equals("3")){
            tvGongsiming.setText("昌兴五金丝网制品厂");
        }else if(activity.id .equals("9")){
            tvGongsiming.setText("石家庄宝康生物工程有限公司");
        }else if(activity.id .equals("10")){
            tvGongsiming.setText("晋州市中兴装饰用布有限公司");
        }else if(activity.id .equals("11")){
            tvGongsiming.setText("晋州市盖鑫医药中间体有限公司");
        }

        try {
            Date startData  = formatres.parse(star);
            Date endData = formatres.parse(end);
            if(  (endData.getTime()-startData.getTime()) > (60*60*1000)){
                ToastHelper.shortToast(getContext(),"时间间隔过大，只能显示60个数据");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        pa.add("type", "");
        HttpUtil.get(URLConstants.DANZHAN, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                beans.clear();
                try {
                    if (response.getBoolean("success")) {

                        String[] names = null;
                        JSONArray time = response.getJSONArray("time");
                        Object[] time_arr = new Object[time.length()];
                        for (int i = 0; i < time.length(); i++) {
                            time_arr[i] = time.getString(i);
                        }

                        JSONArray list = response.getJSONArray("list_shengchan");
                        names = new String[list.length()];
                        for (int i = 0; i < list.length(); i++) {
                            names[i]  = list.getJSONObject(i).getString("name");

                        }

                        ArrayList data_list = new ArrayList();
                        JSONArray lists = response.getJSONArray("list_shengchan");
                        for (int i = 0; i < lists.length(); i++) {
                            JSONArray data = lists.getJSONObject(i).getJSONArray("data");
                                    Object[] data1 = new Object[data.length()];
                                   for(int y = 0 ; y<data.length(); y++){
                                       data1[y] =  data.getString(y);
                                   }
                            data_list.add(data1);

                        }

                        echartview.refreshEchartsWithOption(EchartOptionUtil.getLineChartOptions(names,time_arr,data_list));  //线的名字，X坐标 ，线
//                        adapter.notifyDataSetChanged();
                        if(response.has("list_zhili")){
                            echartview2.setVisibility(View.VISIBLE);
                            setZhiLi(response);
                        }else {
                            echartview2.setVisibility(View.GONE);
                        }

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setZhiLi(JSONObject response) {
        try {
            String[] names = null;
            JSONArray time = response.getJSONArray("time");
            Object[] time_arr = new Object[time.length()];
            for (int i = 0; i < time.length(); i++) {
                time_arr[i] = time.getString(i);
            }

            JSONArray list = response.getJSONArray("list_zhili");
            names = new String[list.length()];
            for (int i = 0; i < list.length(); i++) {
                names[i] = list.getJSONObject(i).getString("name");

            }

            ArrayList data_list = new ArrayList();
            JSONArray lists = response.getJSONArray("list_zhili");
            for (int i = 0; i < lists.length(); i++) {
                JSONArray data = lists.getJSONObject(i).getJSONArray("data");
                Object[] data1 = new Object[data.length()];
                for (int y = 0; y < data.length(); y++) {
                    data1[y] = data.getString(y);
                }
                data_list.add(data1);

            }

            echartview2.refreshEchartsWithOption(EchartOptionUtil.getLineChartOptions(names, time_arr, data_list));  //线的名字，X坐标 ，线
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.ll_qiye, R.id.ll_time1, R.id.ll_time2, R.id.txt_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_time1:
                initTimePicker(new TimerLinster() {
                    @Override
                    public void timeresult(Date date, View v) {
                        tvTime1.setText(format.format(date));
                        start = formatres.format(date);
                    }
                });

                break;
            case R.id.ll_time2:
                initTimePicker(new TimerLinster() {
                    @Override
                    public void timeresult(Date date, View v) {
                        tvTime2.setText(format.format(date));
                        end = formatres.format(date);
                    }
                });
                break;
            case R.id.ll_qiye:
                initQiYe();
                break;
            case R.id.txt_setting:
                if (llSerch.getVisibility() == View.GONE) {
                    llSerch.setVisibility(View.VISIBLE);

                } else if (llSerch.getVisibility() == View.VISIBLE) {
                    llSerch.setVisibility(View.GONE);
                    tvTimeResult.setText(start + " -> " + end);
                    submit();
                }
                break;
        }
    }

    private void submit() {
        if ((start.isEmpty()) || (end.isEmpty()) || (qiye.isEmpty())) {
            ToastHelper.shortToast(getContext(), "请选择开始时间，结束时间，企业");
            return;
        } else {
            upData(start, end);
        }
    }

    private void initQiYe() {
        ArrayList<String> list = new ArrayList<>();
        list.add("昌兴五金丝网制品厂");
        list.add("石家庄宝康生物工程有限公司");
        list.add("晋州市中兴装饰用布有限公司");
        list.add("晋州市盖鑫医药中间体有限公司");
        TheActivity activity = (TheActivity)getActivity() ;
        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                switch (options1){


                    case 0:
                        activity.id = "3";
                        break;
                    case 1:
                        activity.id = "9";
                        break;
                    case 2:
                        activity.id = "10";
                        break;
                        case 3:
                        activity.id = "11";
                        break;

                }
                tvQiye.setText(list.get(options1));
                tvGongsiming.setText(list.get(options1));
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(list);
        view.show();
    }



    public void showDev2(final int bwhich, String ss) {
        ArrayAdapter dev_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dev_list);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.ic_lonch);
        builder.setTitle("选择设备：");
        builder.setAdapter(dev_adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                qiye = dev_id.get(which);
                tvQiye.setText(ss + dev_list.get(bwhich));
                tvGongsiming.setText(ss);
            }
        });
        builder.show();
    }

    private void initTimePicker(final TimerLinster linster) {
        thisTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                linster.timeresult(date, v);
            }
        }).setType(new boolean[]{true, true, true, true, true, false}).setTitleText("请选择时间").build();
        thisTime.show();
    }

    @Override
    public void setInfo(String id) {
        qiye = id;
        if(hasFinish){
            Date d_end = new Date();
            Date d_start = new Date(d_end.getTime() - 15 * 60 * 1000);
            upData(formatres.format(d_start), formatres.format(d_end));
            tvTimeResult.setText(formatres.format(d_start) + "->" + formatres.format(d_end));
        }

    }

    public interface TimerLinster {
        public void timeresult(Date date, View v);
    }
    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }
}
