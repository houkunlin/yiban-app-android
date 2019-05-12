/*
 * Copyright (c) 2017.
 * 网址：http://goour.cn
 * 作者：侯坤林
 * 邮箱：lscode@qq.com
 * 侯坤林 版权所有
 */

package cn.goour.skservice.yiban;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

/**
 * Created by HouKunLin on 2017/7/26.
 */

public class MapChoose extends MapActivity {
    private Config config = Config.getDefaultInstance();
    private MapView mapview;
    private TencentSearch tencentSearch;
    private TencentMap tencentMap;
    private SharedPreferences settings;
    private Marker marker;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_map);
        setTitle("地图选点");
        mapview= findViewById(R.id.mapview);
        mapview.onCreate(bundle);
        Toast.makeText(getApplicationContext(),"单击地图，点击定位图标确认选址",Toast.LENGTH_LONG).show();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        //获取TencentMap实例
        tencentMap = mapview.getMap();
        //设置卫星底图
        tencentMap.setSatelliteEnabled(false);
        //设置实时路况开启
        tencentMap.setTrafficEnabled(false);
        String lat = settings.getString("gpsLat", config.getGpsLat());
        String lng = settings.getString("gpsLng", config.getGpsLng());
        double lat1 = 39.908710;
        double lng1 = 116.397500;
        try {
            lat1 = Double.parseDouble(lat);
        }catch (Exception e){}
        try {
            lng1 = Double.parseDouble(lng);
        }catch (Exception e){}
        LatLng latlng = new LatLng(lat1, lng1);
        //设置地图中心点
        tencentMap.setCenter(latlng);
        //设置缩放级别
        tencentMap.setZoom(17);
        //获取UiSettings实例
        UiSettings uiSettings = mapview.getUiSettings();
        //设置logo到屏幕底部中心
        uiSettings.setLogoPosition(UiSettings.LOGO_POSITION_CENTER_BOTTOM);
        //设置比例尺到屏幕右下角
        uiSettings.setScaleViewPosition(UiSettings.SCALEVIEW_POSITION_RIGHT_BOTTOM);

        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                loadAddressName(latLng);
            }
        });
        tencentSearch = new TencentSearch(this);
        //启用缩放手势(更多的手势控制请参考开发手册)
        uiSettings.setZoomGesturesEnabled(true);

        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                LatLng latLng = arg0.getPosition();
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.GPSSet.class);
                intent.putExtra("address",arg0.getTitle());
                intent.putExtra("lat",latLng.getLatitude()+"");
                intent.putExtra("lng",latLng.getLongitude()+"");
                setResult(RESULT_OK,intent);
                finish();
                return false;
            }
        });
        loadAddressName(latlng);
    }
    private void loadAddressName(final LatLng latLng){
        Geo2AddressParam param = new Geo2AddressParam().location(new Location()
                .lat((float) latLng.getLatitude()).lng((float) latLng.getLongitude()));
        tencentSearch.geo2address(param, new HttpResponseListener() {
            @Override
            public void onSuccess(int i, BaseObject baseObject) {
                Geo2AddressResultObject oj = (Geo2AddressResultObject)baseObject;
                if(oj.result != null){
                    Log.v("demo","address:"+oj.result.address);
                    showLocationMarker(latLng,oj.result.address);
                }else{
                    Toast.makeText(getApplicationContext(),"获取地理位置错误1："+oj,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"获取地理位置错误："+s,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showLocationMarker(LatLng latLng,String address){
        if (marker == null){
            marker = tencentMap.addMarker(new MarkerOptions()
                    .anchor(0.5f,0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker())
                    .draggable(true));
        }
        marker.setPosition(latLng);
        marker.setTitle(address);
        marker.showInfoWindow();
    }

}