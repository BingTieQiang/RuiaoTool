package com.ruiao.tools.youyan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

public class YouyanNewData extends BaseFragment {
    DateFormat bf = new SimpleDateFormat("MM月dd日HH时mm分");
    ArrayList<YouyanGpsBean> list = new ArrayList<>();
    @BindView(R.id.listview)
    XRecyclerView list_task;
    DataAdapter adapter = null;
    @Override
    public void getmsg() {

    }

    @Override
    protected int getContentViewID() {
         return R.layout.fragment_map_newdata;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        adapter = new DataAdapter(getContext(), list);
        list_task.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        //设置布局管理器
        list_task.setLayoutManager(layoutManager);
        //设置分割符号

        //禁止加载更多
        list_task.setLoadingMoreEnabled(false);
        list_task.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                upData();
            }

            @Override
            public void onLoadMore() {
                upData();
            }
        });
        list_task.refresh();
    }

    private void upData() {
        RequestParams pa = new RequestParams();
        pa.add("username", "15801299706");
        list.clear();
        HttpUtil.get(URLConstants.YOUYAN, pa, new HttpUtil.SimpJsonHandle(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                list_task.refreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("success")) {
                        JSONArray array =  response.getJSONArray("data");
                        for(int i = 0; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            YouyanGpsBean bean  = new YouyanGpsBean();
                            bean.name = obj.getString("name");
                            bean.num = obj.getString("num");
                            bean.fengji = obj.getString("fengji");
                            bean.jinghuaqi = obj.getString("jinghuaqi");
                            bean.lat = obj.getDouble("lat");
                            bean.longt = obj.getDouble("long");
                            bean.MonitorID = obj.getString("MonitorID");
                            list.add(bean);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class DataAdapter extends RecyclerView.Adapter {
        private Context context;
        private LayoutInflater mLayoutInflater;
        ArrayList<YouyanGpsBean>  mDataList;

        public DataAdapter(Context context1, ArrayList<YouyanGpsBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context1);
            this.context = context1;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_youyan_newdata, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.time.setText(bf.format(new Date()));
            viewHolder.nongdu.setText(mDataList.get(i).num);
            viewHolder.fengji.setText(mDataList.get(i).fengji);
            viewHolder.jinghuaqi.setText(mDataList.get(i).jinghuaqi);
            viewHolder.diqu.setText(mDataList.get(i).name);

//
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YouyanGpsBean bean = mDataList.get(i);
                    Intent intent = new Intent(getContext(), YouYanHistryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",bean);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {

            return mDataList.size();

        }


        private class ViewHolder extends RecyclerView.ViewHolder {


            private TextView time;
            private TextView nongdu;
            private TextView fengji;
            private TextView jinghuaqi;
            private TextView diqu;



            public ViewHolder(View itemView) {
                super(itemView);
                time = (TextView) itemView.findViewById(R.id.tv_time);
                nongdu = (TextView) itemView.findViewById(R.id.tv_nongdu);
                fengji = (TextView) itemView.findViewById(R.id.tv_fengji);
                jinghuaqi = (TextView) itemView.findViewById(R.id.tv_jinghuaqi);
                diqu = (TextView) itemView.findViewById(R.id.tv_diqu);

            }
        }
    }

}
