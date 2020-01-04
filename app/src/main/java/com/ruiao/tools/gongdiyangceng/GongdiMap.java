package com.ruiao.tools.gongdiyangceng;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
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
import com.ruiao.tools.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GongdiMap extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.bmapView)
    MapView bmapView;
    private BaiduMap mBaiduMap;
    DistrictSearch districtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gongdi_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
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
        districtSearch.searchDistrict(new DistrictSearchOption().cityName("邢台市").districtName("柏乡县"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
