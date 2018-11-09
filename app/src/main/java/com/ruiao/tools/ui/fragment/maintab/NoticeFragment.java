package com.ruiao.tools.ui.fragment.maintab;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.notice.NoticeBean;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.AsynHttpTools;
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

    @BindView(R.id.listview)
    XRecyclerView recyclerview;
    private int Page = 0;
    private int Rows = 7;
    DataAdapter adapter = null;
    @Override
    protected int getContentViewID() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        initView();
//        initData();

        upData();
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

        adapter = new DataAdapter(mContext);
        recyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext );
        //设置布局管理器
        recyclerview.setLayoutManager(layoutManager);
        //设置分割符号

        //禁止加载更多
        recyclerview.setLoadingMoreEnabled(false);
        adapter.setDate();

        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Page = 0;
                upData();
            }

            @Override
            public void onLoadMore() {
                upData();
            }
        });

    }
    //Page 页数  Rows每页几行
    public void upData() {

        RequestParams pa = new RequestParams();
        pa.put("username", SPUtils.get(getContext(), "username", ""));
        pa.put("Page", Page);
        pa.put("Rows", Rows);
        Page++;
        AsynHttpTools.httpGetMethodByParams(URLConstants.WarnData, pa, new JsonHttpResponseHandler("GB2312"){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if( Page == 1){
                    recyclerview.refreshComplete();
                }else{
                    recyclerview.loadMoreComplete();
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



                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getmsg() {
        upData();
    }

    private class DataAdapter extends RecyclerView.Adapter {

        private LayoutInflater mLayoutInflater;
        protected ArrayList<com.ruiao.tools.notice.NoticeBean> mDataList = new ArrayList<>();

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mContext = context;
        }
        public void setDate()
        {
            mDataList.clear();

            for ( int i = 0;i<1;i++)
            {
                mDataList.add(new NoticeBean("通知功能暂未开通" ,"尽请期待"));
            }

            notifyDataSetChanged();
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_message, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText(mDataList.get(position).title);
            viewHolder.context.setText(mDataList.get(position).msg);
        }

        @Override
        public int getItemCount() {
            Log.d("123",""+mDataList.size());
            return mDataList.size();

        }



        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView title;
            private TextView context;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tv_message_title);
                context = (TextView) itemView.findViewById(R.id.tv_message_context);
            }
        }
    }
}
