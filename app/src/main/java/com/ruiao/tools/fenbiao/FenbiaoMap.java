package com.ruiao.tools.fenbiao;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.the.PageChangeLinster;
import com.ruiao.tools.the.TheActivity;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FenbiaoMap extends Fragment implements PageChangeLinster {
    ArrayList<String> fac_list = new ArrayList<>();
    ArrayAdapter dev_adapter;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.rr_tongjitupian)
    RelativeLayout rrTongjitupian;   //图片
    @BindView(R.id.rr_tongji)
    LinearLayout rrTongji;
    @BindView(R.id.tv_tongjineirong)
    TextView tvTongjineirong;
    @BindView(R.id.tuli)
    LinearLayout tuli;
    private BaiduMap mBaiduMap;
    public ArrayList<Point> list = new ArrayList<>();
    DistrictSearch districtSearch;
    MapView bmapView;
    Unbinder unbinder;
    private int fac_num = 0;
    private int lixian_num = 0;
    private int baojing_num = 0;
    private int shengchan_num = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fenbiao_map, container, false);
        bmapView = view.findViewById(R.id.bmapView);
        unbinder = ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {
        dev_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, fac_list);
        listview.setAdapter(dev_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Point point = list.get(position);
                LatLng cenpt = new LatLng(point.b_lat, point.b_long);
                //定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(cenpt)
                        .zoom(19)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                //改变地图状态
                mBaiduMap.setMapStatus(mMapStatusUpdate);
                fac_list.clear();
                dev_adapter.notifyDataSetChanged();
//                listview.setVisibility(View.GONE);
            }
        });
        mBaiduMap = bmapView.getMap();

        districtSearch = DistrictSearch.newInstance();
        districtSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {

            @Override
            public void onGetDistrictResult(DistrictResult districtResult) {
                if (true) {
//                    mBaiduMap.clear();
                    //对检索所得行政区划边界数据进行处理
                    if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
                        List<List<LatLng>> polyLines = districtResult.getPolylines();
                        if (polyLines == null) {
                            return;
                        }
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (List<LatLng> polyline : polyLines) {
                            OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                                    .points(polyline).dottedLine(true).color(Color.TRANSPARENT);
                            mBaiduMap.addOverlay(ooPolyline11);
                            OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
                                    .stroke(new Stroke(5, Color.BLUE)).fillColor(0x00FFFFFF);
                            mBaiduMap.addOverlay(ooPolygon);
                            for (LatLng latLng : polyline) {
                                builder.include(latLng);
                            }
                        }
                        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                                .newLatLngBounds(builder.build()));


                    }
                }
            }
        });
        districtSearch.searchDistrict(new DistrictSearchOption().
                cityName("邢台市")
                .districtName("南宫市"));

//        districtSearch.destroy();


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false    bundle.putSerializable("mark", "3");
            @Override
            public boolean onMarkerClick(Marker marker) {
                Marker marker1 = marker;
                OptionsPickerView view = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {


                        Bundle bundle = marker.getExtraInfo();
                        String str_mark = (String) bundle.getSerializable("mark");

                        TheActivity activity = (TheActivity) getActivity();
                        activity.setPager(options1 + 1, str_mark);
                        activity.isSelectMap = true;


                    }
                }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {


                    }
                }).build();

                ArrayList<String> list = new ArrayList<>();
                list.add("单站数据");
                list.add("实时工况");
                list.add("报警数据");

                view.setPicker(list);
                view.setTitleText((String) marker1.getExtraInfo().getSerializable("name"));
                view.show();
                return false;
            }
        });



        RequestParams pa = new RequestParams();
        pa.add("username", (String) SPUtils.get(getContext(), "username1", ""));
        list.clear();
        HttpUtil.get(URLConstants.DITU, pa, new HttpUtil.SimpJsonHandle(getContext()) {
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
                    if (response.getBoolean("success")) {
                        JSONObject center = response.getJSONObject("center");

                        LatLng cenpt = new LatLng(center.getDouble("lat"), center.getDouble("long"));
                        //定义地图状态
                        MapStatus mMapStatus = new MapStatus.Builder()
//                                .target(cenpt)
//                                .zoom(14)
                                .build();
                        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                                .newMapStatus(mMapStatus);
                        //改变地图状态
                        mBaiduMap.setMapStatus(mMapStatusUpdate);

                        fac_num = 0;
                        lixian_num = 0;
                        baojing_num = 0;
                        shengchan_num = 0;

                        JSONArray arr = response.getJSONArray("points");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            fac_num++;
                            if (obj.getBoolean("baojing")) {    //报警数量
                                baojing_num++;
                            }
                            if (obj.getBoolean("lixian")) {    //离线数量
                                lixian_num++;
                            }
                            if (obj.getBoolean("shengchan")) {   //生产数量
                                shengchan_num++;
                            }

                            list.add(new Point(obj.getString("name"), obj.getDouble("long"), obj.getDouble("lat"), obj.getString("DevID")
                                    , obj.getBoolean("lixian"), obj.getBoolean("baojing"), obj.getBoolean("shengchan")));
                        }

                        for (int i = 0; i < list.size(); i++) {
                            LatLng point = new LatLng(list.get(i).b_lat, list.get(i).b_long);
                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.gk_1_1);

//                            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap("1"));
                            BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.huaide);
                            BitmapDescriptor lixian = BitmapDescriptorFactory.fromResource(R.drawable.lixian);
                            BitmapDescriptor tinchan = BitmapDescriptorFactory.fromResource(R.drawable.tinchan);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions().position(point);




                            if (list.get(i).lixian) {
                                ((MarkerOptions) option).icon(lixian);
                            } else {
                                if (list.get(i).baojing) {
                                    ((MarkerOptions) option).icon(bitmap2);
                                } else {
                                    if (list.get(i).shenchan) {
                                        ((MarkerOptions) option).icon(bitmap);
                                    } else if (!list.get(i).shenchan) {
                                        ((MarkerOptions) option).icon(tinchan);
                                    }
                                }
                            }


                            //在地图上添加Marker，并显示
                            Marker marker = (Marker) mBaiduMap.addOverlay(option);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mark", list.get(i).DevID);
                            bundle.putSerializable("name", list.get(i).name);
                            marker.setExtraInfo(bundle);
                            //文字覆盖物位置坐标
                            LatLng llText = new LatLng(list.get(i).b_lat, list.get(i).b_long);

                            //构建TextOptions对象
                            OverlayOptions mTextOptions = new TextOptions()
                                    .text(list.get(i).name) //文字内容
//                                    .bgColor(0xAAFFFF00) //背景色
                                    .bgColor(Color.TRANSPARENT) //背景色
                                    .fontSize(24) //字号
                                    .fontColor(Color.BLACK) //文字颜色
                                    .rotate(-30) //旋转角度
                                    .position(llText);

                            //在地图上显示文字覆盖物
//                            Overlay mText = mBaiduMap.addOverlay(mTextOptions);     //不显示了
                        }


                        tvTongjineirong.setText(
                                "当前企业共有" + fac_num + "家\n" + "报警" + baojing_num + "家\n" + "生产" + shengchan_num + "家\n" + "离线" + lixian_num + "家\n"
                        );
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在Fragment执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        bmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在<span style="font-family: 微软雅黑, 'Microsoft YaHei', sans-serif;">Fragment</span>执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        bmapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在Fragment执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bmapView.onDestroy();
    }

    @Override
    public void setInfo(String id) {

    }


    @OnClick({R.id.ttt, R.id.rr_tongjitupian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ttt:
                if (tuli.getVisibility() == View.VISIBLE) {
                    tuli.setVisibility(View.GONE);
                } else if (tuli.getVisibility() == View.GONE) {
                    tuli.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rr_tongjitupian:
                if (rrTongji.getVisibility() == View.VISIBLE) {
                    rrTongji.setVisibility(View.GONE);
                } else if (rrTongji.getVisibility() == View.GONE) {
                    rrTongji.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    class Point {
        public double b_long;
        public double b_lat;
        public String name;
        public String DevID;
        public boolean lixian;
        public boolean baojing;

        public Point(String name, double b_long, double b_lat, String devID, boolean lixian, boolean baojing, boolean shenchan) {
            this.b_long = b_long;
            this.b_lat = b_lat;
            this.name = name;
            DevID = devID;
            this.lixian = lixian;
            this.baojing = baojing;
            this.shenchan = shenchan;
        }

        public boolean shenchan;

        public Point(String name, double b_long, double b_lat, String devID) {
            this.b_long = b_long;
            this.b_lat = b_lat;
            this.name = name;
            this.DevID = devID;
        }
    }


    protected Bitmap getMyBitmap(String pm_val) {
        Bitmap bitmap = BitmapDescriptorFactory.fromResource(R.drawable.tinchan).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap, 0 ,0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(22f);
        textPaint.setColor(getResources().getColor(R.color.white));
        canvas.drawText(pm_val, 5, 20 ,textPaint);// 设置bitmap上面的文字位置
        return bitmap;
    }


}
