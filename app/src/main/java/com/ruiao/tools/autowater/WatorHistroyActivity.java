package com.ruiao.tools.autowater;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AsynHttpTools;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.voc.VocBean;
import com.ruiao.tools.widget.Pbdialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

public class WatorHistroyActivity extends Activity {
    DataAdapter adapter = null;
    @BindView(R.id.tag_group)
    TagGroup tagGroup;
    private ArrayList<WatorBean> mDataList = new ArrayList<>();
    @BindView(R.id.listview)
    XRecyclerView listview;

    TimePickerView thisTime;
    @BindView(R.id.tv_newDate)
    TextView tvNewDate;
    private int Page = 1;
    private int Rows = 7;
    private Context context;
    private Pbdialog dialog; //进度条
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String type = "xiaoshi";
    Date nowTime = new Date();
    Date hourTimex = new Date(nowTime.getTime() - 2 * 60 * 60 * 1000);  //小时数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wator_histroy);
        context = this;
        ButterKnife.bind(this);
        adapter = new DataAdapter(context, mDataList);
        initView();
        tagGroup.setTags(new String[]{"分钟数据", "小时数据", "日数据"});
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                switch (tag) {
                    case "分钟数据":
                        type = "fen";

                        break;

                    case "小时数据":
                        type = "xiaoshi";

                        break;
                    case "日数据":
                        type = "tian";

                        break;

                    default:

                        break;

                }
            }
        });
        initData(format.format(hourTimex), format.format(new Date(hourTimex.getTime() - 24 * 60 * 60 * 1000)), type);
    }

    @OnClick(R.id.ll_select_time)
    public void onViewClicked() {
        thisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

                if (type.equals("fen")) {
                    if ((date.getTime()) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                        ToastHelper.shortToast(context, "此时间段无数据");
                        return;
                    }
                    initData(format.format(date), format.format(new Date(date.getTime() - 1 * 60 * 60 * 1000)), "fen");
                    tvNewDate.setText( format.format(new Date(date.getTime() - 1 * 60 * 60 * 1000))+"->"+format.format(date));
                } else if (type.equals("xiaoshi")) {
                    if ((date.getTime()) > (nowTime.getTime() - 2 * 60 * 60 * 1000)) {
                        ToastHelper.shortToast(context, "此时间段无数据");
                        return;
                    }

                    initData(format.format(date), format.format(new Date(date.getTime() - 24 * 60 * 60 * 1000)), "xiaoshi");
                    String str = format.format(new Date(date.getTime() - 24 * 60 * 60 * 1000))+"->"+format.format(date);
                    tvNewDate.setText(str);
                } else if (type.equals("tian")) {
                    if ((date.getTime()) > (date.getTime() - 2 * 60 * 60 * 1000)) {
                        ToastHelper.shortToast(context, "此时间段无数据");
                        return;
                    }
                    initData(format.format(date), format.format(new Date(date.getTime() - 7 * 24 * 60 * 60 * 1000)), "tian");
                    tvNewDate.setText(format.format(new Date(date.getTime() - 24 * 60 * 60 * 1000))+"->"+format.format(date));
                }


            }
        }).setType(new boolean[]{true, true, true, true, true, true}).setTitleText("请选择时间").build();
        thisTime.show();
    }


    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        private ArrayList<WatorBean> mDataList;

        public DataAdapter(Context context, ArrayList<WatorBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_vochistroy, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText("" + mDataList.get(position).cod + "");
            viewHolder.context.setText(mDataList.get(position).date);
            viewHolder.time.setText("" + mDataList.get(position).dan);

        }

        @Override
        public int getItemCount() {
            Log.d("123", "" + mDataList.size());
            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView title;
            private TextView context;
            private TextView time;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tv_message_title);  //VOC1
                context = (TextView) itemView.findViewById(R.id.tv_message_context);   //时间
                time = (TextView) itemView.findViewById(R.id.tv_message_time);   //VOC2
            }
        }
    }

    public Pbdialog showdialog(Context context, String msg) {
        Pbdialog pbdialog = new Pbdialog(context);
        pbdialog.setMessage(msg);
        pbdialog.show();
        return pbdialog;
    }

    private void initData(String end, String start, final String typex) {
        String MonitorID = getIntent().getStringExtra("MonitorID");
        RequestParams pa = new RequestParams();
        pa.add("start", start);
        pa.add("end", end);
        pa.add("type", typex);
        pa.add("MonitorID", MonitorID);

        dialog = showdialog(this, "正在加载.......");

        AsynHttpTools.httpGetMethodByParams(URLConstants.IC, pa, new JsonHttpResponseHandler("GB2312") {
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
                int address = 0; //1为晋州
                // list 按照时间排列
                ArrayList<WatorBean> beans = new ArrayList<>();

                try {
                    String base = (String) SPUtils.get(context, "BASE", "");
                    if (base.startsWith("http://222.222.220.218")) {   //"晋州"
                        address = 1;
                    }


                    Boolean status = response.getBoolean("success");
                    if (status) {
                        beans.clear();
                        WatorBean bean = new WatorBean();

                        JSONArray voclist1 = null;
                        JSONArray voclist2 = null;

                        if(type.equals("fen")){
                            voclist1 = response.getJSONArray("COD");
                            voclist2 = response.getJSONArray("NH3N");
                        }else {
                            voclist1 = response.getJSONArray("COD平均值");
                            voclist2 = response.getJSONArray("氨氮平均值");
                        }


                        JSONObject vocbean1 = null;
                        JSONObject vocbean2 = null;
                        beans.add(new WatorBean("时间","COD","氨氮"));
                        for (int i = 0; i < voclist1.length(); i++) {
                            vocbean1 = voclist1.getJSONObject(i);
                            vocbean2 = voclist2.getJSONObject(i);
                            bean.date = vocbean1.getString("id");

                            bean.cod = vocbean1.getString("value");
                            bean.dan = vocbean2.getString("value");
                            beans.add(bean);
                        }

                        mDataList.addAll(beans);
                        listview.notify();

                    } else {
                        ToastHelper.shortToast(context, response.getString("message"));
                    }
                } catch (JSONException e) {
                    ToastHelper.shortToast(context, "数据解析错误");
                    //JSON数据格式错误
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void initView() {
//        adapter = new DataAdapter(mContext);
//        recyclerview.setAdapter(adapter);

        adapter = new DataAdapter(context, mDataList);
        listview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //设置布局管理器
        listview.setLayoutManager(layoutManager);
        //设置分割符号

        //禁止加载更多
        listview.setLoadingMoreEnabled(false);
        listview.setPullRefreshEnabled(false);
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {


            }

            @Override
            public void onLoadMore() {

            }
        });

    }


}