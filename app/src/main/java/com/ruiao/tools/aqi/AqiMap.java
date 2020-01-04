package com.ruiao.tools.aqi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.ui.base.BaseFragment;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.SPUtils;
import com.ruiao.tools.utils.ToastHelper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.BD09LL;
import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.COMMON;

public class AqiMap extends BaseFragment {
    ArrayList<AqiBean> list = new ArrayList<>();
    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.ll_tongjitupian)
    LinearLayout ll_tongjitupian;
    @BindView(R.id.tv_aqi)
    TextView tv_aqi;
    private BaiduMap mBaiduMap;
    DistrictSearch districtSearch;
    @Override
    public void getmsg() {

    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_aqimap;
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

                Intent intent = new Intent(mContext,OneAqiActivity.class);
                intent.putExtra("bundle",bundle);
                getContext().startActivity(intent);
                return false;
            }
        });
        //1定位到城市，框起来
        districtSearch.searchDistrict(new DistrictSearchOption().cityName("石家庄").districtName("新乐市"));

        //2请求到最新数据，根据站点，列出
        initFromWeb();

    }

    private void initFromWeb() {
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
            //构建Marker图标
            BitmapDescriptor bitmap = null;
            switch (x){
                case 1:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).aqi));
                    break;
                case 2:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).pm10));
                    break;
                case 3:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).pm25));
                    break;
                case 4:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).co));
                    break;
                case 5:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).no2));
                    break;
                case 6:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).so2));
                    break;
                case 7:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).o3));
                    break;
                case 8:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).fengsu));
                    break;
                case 9:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).fengxiang));
                    break;
                case 10:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).shidu));
                    break;
                case 11:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).press));
                    break;
                case 12:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap(list.get(i).temp));
                    break;

            }


            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point);
            ((MarkerOptions) option).icon(bitmap);

            //在地图上添加Marker，并显示
            Marker marker = (Marker) mBaiduMap.addOverlay(option);
            Bundle bundle = new Bundle();

            bundle.putSerializable("bean", list.get(i));
            marker.setExtraInfo(bundle);

        }
    }


    protected Bitmap getMyBitmap(String pm_val) {
        int aqi = Integer.parseInt(pm_val);
        tv_aqi.setText("AQI:"+aqi);
        Bitmap bitmap;
        if(aqi<50){
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.gk_1_1).getBitmap();
        }else if(aqi>=50 && aqi<100) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.lixian).getBitmap();
        }else if(aqi>=100 && aqi<200) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.huaide).getBitmap();
        }else if(aqi>=200) {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.tinchan).getBitmap();
        }else {
            bitmap = BitmapDescriptorFactory.fromResource(R.drawable.tinchan).getBitmap();
        }

        bitmap = Bitmap.createBitmap(bitmap, 0 ,0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(22f);
        textPaint.setColor(getResources().getColor(R.color.primary_text));
        canvas.drawText("", 5, 25 ,textPaint);// 设置bitmap上面的文字位置
        return bitmap;
    }

    @OnClick({R.id.pm25, R.id.pm10,R.id.o3,R.id.so2,R.id.no2,R.id.co,R.id.rr_tongjitupian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pm25:
                if (ll_tongjitupian.getVisibility() == View.VISIBLE) {
                    ll_tongjitupian.setVisibility(View.GONE);
                } else if (ll_tongjitupian.getVisibility() == View.GONE) {
                    ll_tongjitupian.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.pm10:
                if (ll_tongjitupian.getVisibility() == View.VISIBLE) {
                    ll_tongjitupian.setVisibility(View.GONE);
                } else if (ll_tongjitupian.getVisibility() == View.GONE) {
                    ll_tongjitupian.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.o3:
                if (ll_tongjitupian.getVisibility() == View.VISIBLE) {
                    ll_tongjitupian.setVisibility(View.GONE);
                } else if (ll_tongjitupian.getVisibility() == View.GONE) {
                    ll_tongjitupian.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.so2:
                if (ll_tongjitupian.getVisibility() == View.VISIBLE) {
                    ll_tongjitupian.setVisibility(View.GONE);
                } else if (ll_tongjitupian.getVisibility() == View.GONE) {
                    ll_tongjitupian.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.co:
                if (ll_tongjitupian.getVisibility() == View.VISIBLE) {
                    ll_tongjitupian.setVisibility(View.GONE);
                } else if (ll_tongjitupian.getVisibility() == View.GONE) {
                    ll_tongjitupian.setVisibility(View.VISIBLE);
                }
                break;
                case R.id.no2:
                if (ll_tongjitupian.getVisibility() == View.VISIBLE) {
                    ll_tongjitupian.setVisibility(View.GONE);
                } else if (ll_tongjitupian.getVisibility() == View.GONE) {
                    ll_tongjitupian.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rr_tongjitupian:
//                if (ll_tongjitupian.getVisibility() == View.VISIBLE) {
//                    ll_tongjitupian.setVisibility(View.GONE);
//                } else if (ll_tongjitupian.getVisibility() == View.GONE) {
//                    ll_tongjitupian.setVisibility(View.VISIBLE);
//                }
                break;

        }
    }



}
