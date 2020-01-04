package com.ruiao.tools.ui.fragment.maintab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.about.AboutActivity;
import com.ruiao.tools.alarm.AlarmActivity;
import com.ruiao.tools.dongtaiguankong.TaskBean;
import com.ruiao.tools.dongtaiguankong.TaskDetailActivity;
import com.ruiao.tools.dongtaiguankong.TaskListAdapter;
import com.ruiao.tools.notice.NoticeBean;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AsynHttpTools;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.PackageUtils;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by ruiao on 2018/5/4.搭建
 */

public class NoticeFragment extends BaseFragment {
    private ArrayList<com.ruiao.tools.notice.NoticeBean> mDataList = new ArrayList<>();

    TaskListAdapter adapter1;
    ArrayList<TaskBean> list = new ArrayList<>();
    @BindView(R.id.listview)
    XRecyclerView list_task;
    @BindView(R.id.list_task)
    ListView recyclerview;
    private int Page = 1;
    private int Rows = 7;
    DataAdapter adapter = null;

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        initView();
        upData();
    }

    private void initView2() {

        adapter1 = new TaskListAdapter(getContext(),list);
        recyclerview.setAdapter(adapter1);
        recyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TaskBean bean = (TaskBean) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                intent.putExtra("task",bean);
                startActivity(intent);
            }
        });

    }

    private void upData2() {
        RequestParams pa = new RequestParams();
        //taskname=XXX&Act=XXX
        pa.put("taskname", SPUtils.get(getContext(),"username",""));
        pa.put("Act","0,1");
        HttpUtil.get(URLConstants.TASKLIST,pa,new HttpUtil.SimpJsonHandle(getContext()){
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
                list.clear();
                try {
                    if( response.getBoolean("success")){
                        JSONArray arr = response.getJSONArray("tasklist");
                        JSONObject temp;
                        for(int i = 0;i<arr.length();i++){
                            TaskBean bean = new TaskBean();
                            temp = arr.getJSONObject(i);
                            bean.id =  temp.getString("id");
                            bean.address =  temp.getString("address");
                            bean.people = temp.getString("people");
                            bean.time =  temp.getString("time");
                            bean.status =  temp.getString("status");
                            bean.car =  temp.getString("car");
//                            JSONArray array =  temp.getJSONArray("context");
//                            for(int j=0;j<array.length();j++){
//                                bean.tips.add( array.getJSONObject(j).getString("tip"));
//                            }
                            bean.context = temp.getString("context");
                            list.add(bean);
                        }
                        adapter1.notifyDataSetChanged();
                    }else {
                        ToastHelper.shortToast(getContext(),response.getString("message"));
                        adapter1.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void initData() {

//        ArrayList<NoticeBean>  list = new ArrayList<>();
//        for(int i= 0 ;i<10;i++) {
//            list.add(new NoticeBean("标题"+ i, "消息一"+i));
//        }
//
//        adapter.setDate(list);
//        adapter.notifyDataSetChanged();
    }

    private void initView() {
//        adapter = new DataAdapter(mContext);
//        recyclerview.setAdapter(adapter);

        adapter = new DataAdapter(mContext, mDataList);
        list_task.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        list_task.setLayoutManager(layoutManager);
        //设置分割符号

        //禁止加载更多
        list_task.setLoadingMoreEnabled(true);

        list_task.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Page = 1;
                upData();
            }

            @Override
            public void onLoadMore() {
                upData();
            }
        });

    }

//    //Page 页数  Rows每页几行
    public void upData() {
        RequestParams pa = new RequestParams();
        pa.put("username", SPUtils.get(getContext(), "username", ""));
        pa.put("Page", Page);
        pa.put("Rows", Rows);
        Page++;
        AsynHttpTools.httpGetMethodByParams(URLConstants.WarnData, pa, new JsonHttpResponseHandler("GB2312") {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (Page == 2) {
                    list_task.refreshComplete();
                } else {
                    list_task.loadMoreComplete();
                }


            }

            @Override
            public void onFailure(int code, Header[] heads, Throwable throwable, JSONObject json) {

                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (response.getBoolean("success")) {
                        if (Page == 2) {
                            mDataList.clear();
                        }
                        JSONArray array = response.getJSONArray("报警数据");
                        for (int i = 0; i < array.length(); i++) {
                            NoticeBean bean;
                            JSONObject onj = array.getJSONObject(i);
                            bean = new NoticeBean(onj.getString("污染物名称"), onj.getString("设备名称"), onj.getString("超标时间"));
                            bean.com = onj.getString("所属企业");
                            bean.tvChaobaio = onj.getString("超标数值");
                            bean.tvStand = onj.getString("国家标准");

                            mDataList.add(bean);
                        }

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void getmsg() {
        upData();
    }

    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        private ArrayList<com.ruiao.tools.notice.NoticeBean> mDataList;

        public DataAdapter(Context context, ArrayList<com.ruiao.tools.notice.NoticeBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_message, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText(mDataList.get(position).title + "报警");
            viewHolder.context.setText(mDataList.get(position).msg);
            viewHolder.time.setText(mDataList.get(position).time);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AlarmActivity.class);
                    intent.putExtra("bean", mDataList.get(position));
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView title;
            private TextView context;
            private TextView time;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tv_message_title);
                context = (TextView) itemView.findViewById(R.id.tv_message_context);
                time = (TextView) itemView.findViewById(R.id.tv_message_time);
            }
        }
    }
}
