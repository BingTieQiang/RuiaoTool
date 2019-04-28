package com.ruiao.tools.the;

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

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.gongkuang.ShishigongkuangBean;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.widget.Pbdialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ruiao on 2019/3/1.
 */

public class ShishiGongkuang1 extends Fragment implements PageChangeLinster {
    @BindView(R.id.tv_qiye)
    TextView tvQiye;  //企业
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
    @BindView(R.id.tv_quyu)
    TextView tvArea;
    @BindView(R.id.tv_leibie)
    TextView tvLeibie;
    @BindView(R.id.yujingzhuangtai)
    TextView YujingZhuangtai;
    @BindView(R.id.tv_com)
    TextView tvCom;
    @BindView(R.id.ll_serch)
    LinearLayout llSerch;
    @BindView(R.id.tv_sousuotiaojian)
    TextView tvSousuotiaojian;
    @BindView(R.id.tv_leixing)
    TextView tvLeixing;
    @BindView(R.id.img_yichang)
    ImageView yichang;
    @BindView(R.id.ll_yujing)
    LinearLayout LLyichang;
    private Pbdialog dialog; //进度条
    ArrayList<QiYeBean> qiYeBeans = new ArrayList<>();

    private MyTimeTask task;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER:
                    //在此执行定时操作
//
//                    TheActivity activity = (TheActivity) getActivity();
//                    if (activity.isSelectMap) {   //选了地图 那么就 有限制了，
                    upData();
//                    } else {
//                        upData2();
//                    }

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_shishigongkuang, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        listView.setNestedScrollingEnabled(false);
        list = new ArrayList<>();
        little_list = new ArrayList<>();
//        listview.setAdapter(adapter);
//        listview.setGroupIndicator(null);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//            }
//        });
//        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//
//                return false;
//            }
//        });
        adapter = new DataAdapter(getContext(), little_list);
        listView.setAdapter(adapter);
        qiYeBeans.clear();
        qiYeBeans.add(new QiYeBean("昌兴五金丝网制品厂", "3", "东里庄镇", "金属制品业"));
        qiYeBeans.add(new QiYeBean("石家庄宝康生物工程有限公司", "9", "工业路循环经济园区", "化学原料和化学制品业"));
        qiYeBeans.add(new QiYeBean("晋州市中兴装饰用布有限公司", "10", "工业路循环经济园区", "印染业"));
        qiYeBeans.add(new QiYeBean("晋州市盖鑫医药中间体有限公司", "11", "工业路循环经济园区", "化学原料和化学制品业"));
        setTimer();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        stopTimer();

    }

    private void setTimer() {
        task = new MyTimeTask(60000, new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(TIMER);
                //或者发广播，启动服务都是可以的
            }
        });
        task.start();
    }

    private void stopTimer() {
        task.stop();
    }

    public void upData() {


        RequestParams pa = new RequestParams();
        TheActivity activity = (TheActivity) getActivity();

        if (!activity.id.isEmpty()) {
            pa.put("DevID", activity.id);
        }

//        URLConstants.SHISHI

        HttpUtil.get(URLConstants.SHISHI, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                TheActivity activity = (TheActivity) getActivity();
                try {
                    if (response.getBoolean("success")) {
                        JSONArray arr = response.getJSONArray("list");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            ShishigongkuangBean bean = new ShishigongkuangBean();
                            bean.qiye = obj.getString("企业名称");
                            bean.shijian = obj.getString("时间");
                            bean.wangluo = obj.getString("网络状态");
                            bean.shengchan = obj.getString("生产状态");
                            bean.shengchansheshi = obj.getString("生产设施状态");
                            bean.zhilisheshi = obj.getString("治理设施状态");
                            bean.yujing = obj.getString("预警等级:");
                            bean.yingjicuoshi = obj.getString("应急措施:");
                            bean.xingzhengquyu = obj.getString("行政区域:");
                            bean.hangye = obj.getString("行业类别:");
                            YujingZhuangtai.setText("当前预警等级：" + bean.yujing);

                            JSONArray array = obj.getJSONArray("设备");
                            bean.lists = new ArrayList<>();
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject shebei = array.getJSONObject(j);
                                bean.lists.add(new ShishigongkuangBean.canshu(shebei.getString("name"), shebei.getString("状态"),shebei.getString("今日运行")));
                            }

                            if (activity.id.equals("3")) {
                                if ("昌兴五金丝网制品厂".equals(obj.getString("企业名称"))) {
                                    list.add(bean);
                                }
                            } else if (activity.id.equals("9")) {
                                if ("石家庄宝康生物工程有限公司".equals(obj.getString("企业名称"))) {
                                    list.add(bean);
                                }
                            } else if (activity.id.equals("10")) {
                                if ("晋州市中兴装饰用布有限公司".equals(obj.getString("企业名称"))) {
                                    list.add(bean);
                                }
                            } else if (activity.id.equals("11")) {
                                if ("晋州市盖鑫医药中间体有限公司".equals(obj.getString("企业名称"))) {
                                    list.add(bean);
                                }
                            }

                        }
//                        adapter.notifyDataSetChanged();
                        setAdapter();
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
        if(bean.yingjicuoshi.equals("正常")){
            tvYujing.setText(bean.yingjicuoshi);
            tvYujing.setTextColor(getResources().getColor(R.color.primary_text));
            yichang.setImageResource(R.drawable.zhengchang);
            LLyichang.setVisibility(View.GONE);
        }else {
            yichang.setImageResource(R.drawable.yichang);
            tvYujing.setText(bean.yingjicuoshi);
            LLyichang.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    public void setInfo(String id) {

    }

    @OnClick({R.id.ll_quyu, R.id.ll_leibie, R.id.ll_com, R.id.txt_setting,R.id.rr_yichangyice, R.id.rr_gongyiliucheng,R.id.rr_gongyipanding,R.id.rr_baojingjilu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rr_baojingjilu:
                TheActivity activitys = (TheActivity) getActivity();
                activitys.changeFragment(3);
                break;
            case R.id.rr_gongyipanding:
                startActivity(new Intent(getContext(),ZhuangtaiPaidingActivity.class));
                break;
            case R.id.ll_quyu:
                quyu();
                break;
            case R.id.ll_leibie:
                leixing();
                break;
            case R.id.ll_com:
                qiye();
                break;
            case R.id.txt_setting:
                if (llSerch.getVisibility() == View.GONE) {
                    llSerch.setVisibility(View.VISIBLE);

                } else if (llSerch.getVisibility() == View.VISIBLE) {
                    llSerch.setVisibility(View.GONE);
                    submit();
                }
                break;
            case R.id.rr_yichangyice:
                TheActivity activity = (TheActivity) getActivity();
                Intent intent1chang1ce = new Intent(getContext(), YichangyiceActivity.class);
                intent1chang1ce.putExtra("Devid",activity.id);
                startActivity(intent1chang1ce);
                break;
            case R.id.rr_gongyiliucheng:  //流程图
                TheActivity activity1 = (TheActivity) getActivity();
                Intent intent1chang1ce1 = new Intent(getContext(), LiuchengActivity.class);
                intent1chang1ce1.putExtra("Devid",activity1.id);
                startActivity(intent1chang1ce1);
                break;
        }
    }

    ArrayList<String> qiye_list = new ArrayList<>();
    ArrayList<String> id_list = new ArrayList<>();

    private void qiye() {
        id_list.clear();
        qiye_list.clear();
        for (int i = 0; i < qiYeBeans.size(); i++) {
            QiYeBean bean = qiYeBeans.get(i);
            if (tvArea.getText().equals("全部") && tvLeibie.getText().equals("全部")) {
                qiye_list.add(bean.name);
                id_list.add(bean.devid);
            } else if (!tvArea.getText().equals("全部") && tvLeibie.getText().equals("全部")) {
                if (bean.quyu.equals(tvArea.getText())) {
                    qiye_list.add(bean.name);
                    id_list.add(bean.devid);
                }
            } else if (tvArea.getText().equals("全部") && !tvLeibie.getText().equals("全部")) {
                if (bean.leixing.equals(tvLeibie.getText())) {
                    qiye_list.add(bean.name);
                    id_list.add(bean.devid);
                }
            } else if (!tvArea.getText().equals("全部") && !tvLeibie.getText().equals("全部")) {
                if (bean.quyu.equals(tvArea.getText()) && bean.leixing.equals(tvLeibie.getText())) {
                    qiye_list.add(bean.name);
                    id_list.add(bean.devid);
                }
            }


        }
        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                tvCom.setText(qiye_list.get(options1));
                TheActivity activity = (TheActivity) getActivity();
                activity.id = id_list.get(options1);

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(qiye_list);
        view.show();
    }

    private void quyu() {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        list.add("东里庄镇");
        list.add("工业路循环经济园区");
        list.add("晋州镇");
        list.add("小樵镇");
        list.add("马于镇");
        list.add("桃园镇");
        list.add("总十庄镇");
        list.add("东卓宿镇");
        list.add("槐树镇");
        list.add("营里镇");
        list.add("周家庄乡");
        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvSousuotiaojian.setText("区域：" + list.get(options1));
                tvArea.setText(list.get(options1));
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(list);
        view.show();
    }

    private void leixing() {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        list.add("印染业");
        list.add("金属制品业");
        list.add("非金属矿物制品业");
        list.add("化学原料和化学制品业");
        list.add("橡胶和塑料制品业");
        list.add("电力，热力生产和工业业");
        OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvLeixing.setText("类别：" + list.get(options1));
                tvLeibie.setText(list.get(options1));

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {


            }
        }).build();


        view.setPicker(list);
        view.show();
    }

    private void submit() {
        TheActivity activity = (TheActivity) getActivity();
        activity.isSelectMap = true;
        upData();
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
        upData();

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
            int minute = Integer.parseInt(mDataList.get(i).zhuangtai)  % 60;
//            viewHolder.address.setText(mDataList.get(i).people);
            viewHolder.name.setText(mDataList.get(i).name);
            viewHolder.dianliu.setText(mDataList.get(i).value);
            viewHolder.shijian.setText(hours+"小时"+minute+"分");
//            viewHolder.miaoshu.setText(mDataList.get(i).context);


//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    TaskBean bean = (TaskBean) mDataList.get(i);
//                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
//                    intent.putExtra("task",bean);
//                    startActivity(intent);
//                }
//            });
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

//                ico = (ImageView) itemView.findViewById(R.id.ico);
//                type = (TextView) itemView.findViewById(R.id.type);
//                time = (TextView) itemView.findViewById(R.id.time);
                name = (TextView) itemView.findViewById(R.id.tv_shebei_name);
                dianliu = (TextView) itemView.findViewById(R.id.tv_shebei_value);
                shijian = (TextView) itemView.findViewById(R.id.tv_shebei_shijian);
//                type = (TextView) itemView.findViewById(R.id.type);
            }
        }
    }

}
