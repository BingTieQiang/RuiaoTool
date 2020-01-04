package com.ruiao.tools.aqi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.dongtaiguankong.TaskBean;
import com.ruiao.tools.dongtaiguankong.TaskDetailActivity;
import com.ruiao.tools.the.Baojing;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

public class AqiNewData extends BaseFragment {
    ArrayList<AqiBean> list = new ArrayList<>();
    @BindView(R.id.listview)
    XRecyclerView list_task;
    DataAdapter adapter = null;
    @Override
    public void getmsg() {

    }

    @Override
    protected int getContentViewID() {
         return R.layout.fragment_aqinewdata;
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
        pa.add("username", (String) SPUtils.get(getContext(), "username", ""));
        list.clear();
        HttpUtil.get(URLConstants.AQI, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                            AqiBean bean  = new AqiBean();
                            bean.name = obj.getString("name");
                            bean.pm25 = obj.getString("pm25");
                            bean.pm10 = obj.getString("pm10");
                            bean.co = obj.getString("co");
                            bean.no2 = obj.getString("no2");
                            bean.so2 = obj.getString("so2");
                            bean.o3 = obj.getString("o3");
                            bean.fengsu = obj.getString("fengsu");
                            bean.fengxiang = obj.getString("fengxiang");
                            bean.shidu = obj.getString("shidu");
                            bean.press = obj.getString("press");
                            bean.temp = obj.getString("temp");
                            bean.lat = obj.getDouble("lat");
                            bean.longt = obj.getDouble("long");
                            bean.aqi = obj.getString("aqi");
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
        ArrayList<AqiBean>  mDataList;

        public DataAdapter(Context context1, ArrayList<AqiBean> mDataList) {
            mLayoutInflater = LayoutInflater.from(context1);
            this.context = context1;
            this.mDataList = mDataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_aqinewdata, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.aqi.setText(mDataList.get(i).aqi);
            viewHolder.co.setText(mDataList.get(i).co);
            viewHolder.o3.setText(mDataList.get(i).o3);
            viewHolder.no2.setText(mDataList.get(i).no2);
            viewHolder.so2.setText(mDataList.get(i).so2);
            viewHolder.pm25.setText(mDataList.get(i).pm25);
            viewHolder.pm10.setText(mDataList.get(i).pm10);
            viewHolder.fengsu.setText(mDataList.get(i).fengsu);
            viewHolder.fengxiang.setText(mDataList.get(i).fengxiang);
            viewHolder.temp.setText(mDataList.get(i).temp);
            viewHolder.press.setText(mDataList.get(i).press);
            viewHolder.shidu.setText(mDataList.get(i).shidu);
            viewHolder.address.setText(mDataList.get(i).name);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AqiBean bean = mDataList.get(i);
                    Intent intent = new Intent(getContext(), OneAqiActivity.class);
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


            private TextView aqi;
            private TextView co;
            private TextView o3;
            private TextView no2;
            private TextView so2;
            private TextView pm25;
            private TextView pm10;
            private TextView fengsu;
            private TextView fengxiang;
            private TextView temp;
            private TextView press;
            private TextView shidu;
            private TextView address;


            public ViewHolder(View itemView) {
                super(itemView);
                aqi = (TextView) itemView.findViewById(R.id.tv_aqi);
                co = (TextView) itemView.findViewById(R.id.tv_co);
                o3 = (TextView) itemView.findViewById(R.id.tv_o3);
                no2 = (TextView) itemView.findViewById(R.id.tv_no2);
                so2 = (TextView) itemView.findViewById(R.id.tv_so2);
                pm25 = (TextView) itemView.findViewById(R.id.tv_pm25);
                pm10 = (TextView) itemView.findViewById(R.id.tv_pm10);
                fengsu = (TextView) itemView.findViewById(R.id.tv_fengsu);
                fengxiang = (TextView) itemView.findViewById(R.id.tv_fengxiang);
                temp = (TextView) itemView.findViewById(R.id.tv_temp);
                press = (TextView) itemView.findViewById(R.id.tv_press);
                shidu = (TextView) itemView.findViewById(R.id.tv_shidu);
                address = (TextView) itemView.findViewById(R.id.tv_diqu);

            }
        }
    }

}
