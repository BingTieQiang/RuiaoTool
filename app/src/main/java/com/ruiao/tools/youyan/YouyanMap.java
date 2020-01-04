package com.ruiao.tools.youyan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.LinearLayout;

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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.gson.JsonObject;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;

import com.ruiao.tools.aqi.OneAqiActivity;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.COMMON;

public class YouyanMap extends BaseFragment {
    ArrayList<YouyanGpsBean> list = new ArrayList<>();
    @BindView(R.id.bmapView)
    MapView bmapView;
    private BaiduMap mBaiduMap;
    DistrictSearch districtSearch;
    @Override
    public void getmsg() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_youyan_map;
    }

    @Override
    protected void initViewsAndEvents(View rootView, Bundle savedInstanceState) {
        mBaiduMap = bmapView.getMap();
        districtSearch = DistrictSearch.newInstance();
        districtSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {

            @Override
            public void onGetDistrictResult(DistrictResult districtResult) {

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
        );
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false    bundle.putSerializable("mark", "3");
            @Override
            public boolean onMarkerClick(Marker marker) {
                Marker marker1 = marker;
                Bundle bundle = marker1.getExtraInfo();
//                AqiBean aqi_mark = (AqiBean) bundle.getSerializable("mark");

                Intent intent = new Intent(mContext,YouYanHistryActivity.class);
                intent.putExtra("bundle",bundle);
                getContext().startActivity(intent);
                return false;
            }
        });
        //1定位到城市，框起来
//        districtSearch.searchDistrict(new DistrictSearchOption().cityName("石家庄").districtName("藁城区"));

        //2请求到最新数据，根据站点，列出
        initFromWeb();

    }

    private void initFromWeb() {
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
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("success")) {
                        JSONArray array =  response.getJSONArray("data");
                        JSONObject center =  response.getJSONObject("center");
                        LatLng center_point = new LatLng(center.getDouble("lat"), center.getDouble("long"));
                        MapStatus status = new MapStatus.Builder().target(center_point).zoom(15).build();
                        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newMapStatus(status);
                        mBaiduMap.setMapStatus(statusUpdate);
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

                        putMark(1);

                    } else {
                        ToastHelper.shortToast(getContext(), response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void putMark(int x) {
//        mBaiduMap.clear();
        CoordinateConverter converter  = new CoordinateConverter().from(COMMON);

        for (int i = 0; i < list.size(); i++) {
            LatLng pointx = new LatLng(list.get(i).lat, list.get(i).longt);
            LatLng point = converter.coord(pointx).convert();
//            //构建Marker图标
            BitmapDescriptor bitmap = null;
            bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).num));

            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point);
            ((MarkerOptions) option).icon(bitmap);
//
//            //在地图上添加Marker，并显示
            Marker marker = (Marker) mBaiduMap.addOverlay(option);
            Bundle bundle = new Bundle();
//
            bundle.putSerializable("bean", list.get(i));
            marker.setExtraInfo(bundle);
//
        }
    }


    protected Bitmap getMyBitmap(String pm_val) {
        Bitmap bitmap = BitmapDescriptorFactory.fromResource(R.drawable.aqi_1).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap, 0 ,0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(22f);
        textPaint.setColor(getResources().getColor(R.color.primary_text));
        canvas.drawText(pm_val, 5, 25 ,textPaint);// 设置bitmap上面的文字位置
        return bitmap;
    }






}
