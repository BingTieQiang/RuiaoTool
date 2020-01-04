package com.ruiao.tools.dongtaiguankong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

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
import com.bin.david.form.core.SmartTable;
import com.loopj.android.http.RequestParams;
import com.ruiao.tools.R;
import com.ruiao.tools.aqi.OneAqiActivity;
import com.ruiao.tools.http.https.HttpsUtils;
import com.ruiao.tools.url.URLConstants;
import com.ruiao.tools.utils.HttpUtil;
import com.ruiao.tools.utils.ToastHelper;
import com.ruiao.tools.youyan.YouyanGpsBean;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.COMMON;

public class DongtaiMap extends Fragment {
    ArrayList<YouyanGpsBean> list = new ArrayList<>();
    Unbinder unbinder;
//    @BindView(R.id.bmapView)
//    MapView bmapView;

    @BindView(R.id.webview)
    WebView webview;
    private BaiduMap mBaiduMap;
    DistrictSearch districtSearch;
    ArrayList<Ditubean> arrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gongdi_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("gb2312") ;
        webview.getSettings().setTextZoom(150);
        webview.loadUrl("http://110.249.145.94:11111/wryzxjc/RealMapAPP.asp");
//        mBaiduMap = bmapView.getMap();
//        districtSearch = DistrictSearch.newInstance();
//        districtSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {
//
//         @Override
//           public void onGetDistrictResult(DistrictResult districtResult) {
//
//             //对检索所得行政区划边界数据进行处理
//             if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
//                 List<List<LatLng>> polyLines = districtResult.getPolylines();
//                 if (polyLines == null) {
//                     return;
//                 }
//                 LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                 for (List<LatLng> polyline : polyLines) {
//                     OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
//                             .points(polyline).dottedLine(true).color(Color.TRANSPARENT);
//                     mBaiduMap.addOverlay(ooPolyline11);
//                     OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
//                             .stroke(new Stroke(5, Color.BLUE)).fillColor(0x00FFFFFF);
//                     mBaiduMap.addOverlay(ooPolygon);
//                     for (LatLng latLng : polyline) {
//                         builder.include(latLng);
//                     }
//                 }
//                 mBaiduMap.setMapStatus(MapStatusUpdateFactory
//                         .newLatLngBounds(builder.build()));
//
//
//             }
//            }
//        }
//        );

//        districtSearch.searchDistrict(new DistrictSearchOption().cityName("石家庄市").districtName("裕华区"));
//
//        for(int i = 0;i<4;i++) {
//            Ditubean ditubean = new Ditubean();
//
//            arrayList.add(ditubean);
//        }
//        table.setData(arrayList);
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
//                        JSONObject center =  response.getJSONObject("center");
//                        LatLng center_point = new LatLng(center.getDouble("lat"), center.getDouble("long"));
//                        MapStatus status = new MapStatus.Builder().target(center_point).zoom(15).build();
//                        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newMapStatus(status);
//                        mBaiduMap.setMapStatus(statusUpdate);
                        for(int i = 0; i < array.length() ; i++){
                            JSONObject obj = array.getJSONObject(i);
                            YouyanGpsBean bean  = new YouyanGpsBean();
                            bean.name = obj.getString("name");
                            bean.lat = obj.getDouble("lat");
                            bean.longt = obj.getDouble("long");

                            list.add(bean);
                        }

                        putMark();

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

    }

    private void putMark() {
//        mBaiduMap.clear();
        CoordinateConverter converter  = new CoordinateConverter().from(COMMON);

        for (int i = 0; i < list.size(); i++) {
            LatLng pointx = new LatLng(list.get(i).lat, list.get(i).longt);
            LatLng point = converter.coord(pointx).convert();
//            //构建Marker图标
            BitmapDescriptor bitmap = null;
            bitmap = BitmapDescriptorFactory.fromBitmap(getMyBitmap());

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


    protected Bitmap getMyBitmap() {
        Bitmap bitmap = BitmapDescriptorFactory.fromResource(R.drawable.aqi_1).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap, 0 ,0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(22f);
        textPaint.setColor(getResources().getColor(R.color.primary_text));
        return bitmap;
    }
}
