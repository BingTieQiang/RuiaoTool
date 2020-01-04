package com.ruiao.tools.fenbiao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.view.TimePickerView;
import com.example.pickerviewlibrary.picker.TeaPickerView;
import com.example.pickerviewlibrary.picker.entity.PickerData;
import com.example.pickerviewlibrary.picker.listener.OnPickerClickListener;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.gongkuang.ShishigongkuangBean;
import com.ruiao.tools.the.PageChangeLinster;
import com.ruiao.tools.the.QiYeBean;
import com.ruiao.tools.the.YichangyiceActivity;
import com.ruiao.tools.the.ZhuangtaiPaidingActivity;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.Pbdialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2019/3/1.
 */

public class FragmentGongkuang extends Fragment implements PageChangeLinster {
    String id; //公司ID
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long time_num;
    private String partitionId = "";
      //
    @BindView(R.id.tv_qiye)
    TextView tvQiye;  //企业
    @BindView(R.id.txt_setting)
    TextView txt_setting;
    @BindView(R.id.tv_yujing)
    TextView tvYujing;  //预警
    @BindView(R.id.tv_time)
    TextView tvTime;  //时间
    @BindView(R.id.tv_wanlguo)
    TextView tvWangluo;  //网络
    @BindView(R.id.tv_address)
    TextView tvQuyu;  //区域
    @BindView(R.id.tv_leibies)
    TextView tvLeibies;  //行业类别
    @BindView(R.id.tv_shengchanshesh)
    TextView tvShengchan;  //生产设施
    @BindView(R.id.tv_zhili)
    TextView tvZhili;  //治理设施
    @BindView(R.id.list_view)
    RecyclerView listView;
    DataAdapter adapter;
    private boolean isNew = true;
    private static final int TIMER = 999;
    TimePickerView thisTime;
    JSONObject result;
    @BindView(R.id.yujingzhuangtai)
    TextView YujingZhuangtai;  //预警等级
    PickerData pickerData = new PickerData();
    List<String> first = new ArrayList<>();
    Map<String, List<String>> mSecondDatas = new HashMap<>();
    @BindView(R.id.tv_sousuotiaojian)
    TextView tvSousuotiaojian;  //选择的企业名称
    @BindView(R.id.img_yichang)
    ImageView yichang;   //企业预警 图片
    @BindView(R.id.ll_yujing)
    LinearLayout LLyichang;
    private Pbdialog dialog; //进度条
    ArrayList<QiYeBean> qiYeBeans = new ArrayList<>();
    TeaPickerView teaPickerView;
    private MyTimeTask task;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER:
                    //在此执行定时操作
//
                    upData();

//

                    break;
                default:
                    break;
            }
        }
    };

    ArrayList<ShishigongkuangBean> list;


    ArrayList<ShishigongkuangBean.canshu> little_list;
    Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gongkuang, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        listView.setNestedScrollingEnabled(false);
        list = new ArrayList<>();
        little_list = new ArrayList<>();

        adapter = new DataAdapter(getContext(), little_list);
        listView.setAdapter(adapter);
        show();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        upData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        stopTimer();

    }


    private void stopTimer() {
//        task.stop();
    }

    public void upData() {


        RequestParams pa = new RequestParams();
        HttpUtil.get(URLConstants.Fenbiao_Yujing, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onFinish() {
                super.onFinish();
//                list_task.refreshComplete();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                list.clear();
//                adapter.notifyDataSetChanged();

                try {
                    if (response.getBoolean("success")) {
                        String yujing = response.getString("yujing");
                        YujingZhuangtai.setText("当前预警等级:" + yujing);
                        yichang.setImageResource(R.drawable.zhengchang);

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
//                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void setAdapter() {
        little_list.clear();
        ShishigongkuangBean bean = list.get(0);
        tvQiye.setText(bean.qiye);

        tvTime.setText(bean.shijian);
        tvWangluo.setText(bean.wangluo);
        tvQuyu.setText(bean.xingzhengquyu);
        tvLeibies.setText(bean.hangye);
        tvShengchan.setText(bean.shengchan);
        tvZhili.setText(bean.zhilisheshi);
        little_list.addAll(bean.lists);
        if (bean.yingjicuoshi.equals("正常")) {
            tvYujing.setText(bean.yingjicuoshi);
            tvYujing.setTextColor(getResources().getColor(R.color.primary_text));
            yichang.setImageResource(R.drawable.zhengchang);
            LLyichang.setVisibility(View.GONE);
        } else {
            yichang.setImageResource(R.drawable.yichang);
            tvYujing.setText(bean.yingjicuoshi);
            LLyichang.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    public void setInfo(String id) {

    }

    @OnClick({R.id.rr_yichangyice, R.id.rr_gongyiliucheng, R.id.rr_gongyipanding, R.id.rr_baojingjilu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rr_yichangyice:
                if(!partitionId.isEmpty()){
                    Intent intent1chang1ce = new Intent(getContext(), FenbiaoYichangyiceActivity.class);
                    intent1chang1ce.putExtra("partitionId",partitionId);
                    startActivity(intent1chang1ce);
                }

                break;
            case R.id.rr_gongyiliucheng:

                break;
            case R.id.rr_gongyipanding:
                startActivity(new Intent(getContext(), ZhuangtaiPaidingActivity.class));
                break;
            case R.id.rr_baojingjilu:

                break;
        }
    }

    public class MyTimeTask {
        private Timer timer;
        private TimerTask task;
        private long time;

        public MyTimeTask(long time, TimerTask task) {
            this.task = task;
            this.time = time;
            if (timer == null) {
                timer = new Timer();
            }
        }

        public void start() {
            timer.schedule(task, 0, time);//每隔time时间段就执行一次
        }

        public void stop() {
            if (timer != null) {
                timer.cancel();
                if (task != null) {
                    task.cancel();  //将原任务从队列中移除
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        upData();

    }


    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        private ArrayList<ShishigongkuangBean.canshu> mDataList;

        public DataAdapter(Context context1, ArrayList<ShishigongkuangBean.canshu> mDataList) {
            mLayoutInflater = LayoutInflater.from(context1);
            this.context = context1;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.dongtaiguankong3, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            ViewHolder viewHolder = (ViewHolder) holder;
            int hours = (int) Math.floor(Integer.parseInt(mDataList.get(i).zhuangtai) / 60);
            int minute = Integer.parseInt(mDataList.get(i).zhuangtai) % 60;
//            viewHolder.address.setText(mDataList.get(i).people);
            viewHolder.name.setText(mDataList.get(i).name);
            viewHolder.dianliu.setText(mDataList.get(i).value);
            viewHolder.shijian.setText(hours + "小时" + minute + "分");

        }

        @Override
        public int getItemCount() {
            Log.d("size", "" + mDataList.size());
            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {


            private TextView dianliu;
            private TextView name;
            private TextView shijian;
            private TextView miaoshu;


            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_shebei_name);
                dianliu = (TextView) itemView.findViewById(R.id.tv_shebei_value);
                shijian = (TextView) itemView.findViewById(R.id.tv_shebei_shijian);

            }
        }
    }

    private void show() {
        RequestParams pa = new RequestParams();
        pa.put("username", (String) SPUtils.get(getContext(), "username", ""));
        HttpUtil.get(URLConstants.Fenbiao_Company, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                result = response;
                try {
                    JSONArray array = response.getJSONArray("factory");
                    first.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String comname = obj.getString("name");
                        first.add(comname);
                        JSONArray array2 = obj.getJSONArray("device");
                        ArrayList<String> list = new ArrayList<>();
                        list.clear();
                        for (int y = 0; y < array2.length(); y++) {
                            JSONObject obj2 = array2.getJSONObject(y);
                            String deviceName = obj2.getString("name");
                            list.add(deviceName);

                            ArrayList<String> lists = new ArrayList<>();
                            JSONArray parts = obj2.getJSONArray("device"); //分区对象
                            lists.clear();
                            for (int z = 0; z < parts.length(); z++) {
                                JSONObject part = parts.getJSONObject(z);
                                lists.add(deviceName + ":" + part.getString("name"));

                                mSecondDatas.put(comname, lists);
                            }
//                            mThirdDatas.put(deviceName,lists);

                        }
//                        mSecondDatas.put(comname,list);
                    }

                    pickerData.setFirstDatas(first);
                    pickerData.setSecondDatas(mSecondDatas);
//                    pickerData.setThirdDatas(mThirdDatas);
                    pickerData.setInitSelectText("请选择");
                    teaPickerView = new TeaPickerView(getActivity(), pickerData);
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
                                for (int i = 0; i < arr2.length(); i++) {
                                    JSONObject obj4 = arr2.getJSONObject(i);
                                    String devicenam = obj4.getString("name");
                                    if (devicenam.equals(secondtext)) {
                                        JSONArray arrend = obj4.getJSONArray("device");
                                        for (int y = 0; y < arrend.length(); y++) {
                                            JSONObject obj5 = arrend.getJSONObject(y);
                                            String endname = obj5.getString("name");
                                            if (endname.equals(thirdtext)) {
                                                partitionId = obj5.getString("id");
                                            }

                                        }
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            teaPickerView.dismiss();
//                            tv_gongsiming.setText(firsttext+" "+secondtextx);
//                            initChart(  format1.format(new Date(time_num)) , format1.format(new Date(time_num + 1000 * 3600 * 1)));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
