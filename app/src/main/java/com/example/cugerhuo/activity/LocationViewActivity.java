package com.example.cugerhuo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.cugerhuo.R;

/**
 * 查看点具体信息
 * @Author: 李柏睿
 * @Time: 2023/5/4 15:25
 */
public class LocationViewActivity extends AppCompatActivity implements LocationSource{

    MapView mapView;

    private AMap aMap;
    private UiSettings uiSettings;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;
    private LocationSource.OnLocationChangedListener locationChangedListener;
    private String poiName;
    double lat;
    double lng;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);
        tvLocation = findViewById(R.id.tvLocation);
        Intent intent =getIntent();
        if(intent.getSerializableExtra("latlng")!=null){
            poiName = (String)intent.getSerializableExtra("poi");
            String latlng = (String)intent.getSerializableExtra("latlng");
            String[] location = latlng.split(",");
            lat = Double.parseDouble(location[1]);
            lng = Double.parseDouble(location[0]);
            tvLocation.setText(poiName);
        }else{

        }

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        try {
            initMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMap() throws Exception {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(false);

        aMapLocationClient = new AMapLocationClient(this);
        aMapLocationClient.setLocationListener(aMapLocation -> {
            Log.d("map","myLocation:lat="+aMapLocation.getLatitude()+",lon="+aMapLocation.getLongitude()+",zoomLevel="+aMap.getMaxZoomLevel());

            if (locationChangedListener == null || aMapLocation == null) {
                return;
            }

            if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                locationChangedListener.onLocationChanged(aMapLocation);
            }
        });

        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setInterval(5000);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        //显示定位标记
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
//        Bitmap iconBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_speak);
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(iconBitmap));
        //透明定位精度圆圈
        myLocationStyle.strokeWidth(0);
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        //定位间隔
        myLocationStyle.interval(5000);
        //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        aMap.setMyLocationStyle(myLocationStyle);
        aMapLocationClient.startLocation();

        //位置
        LatLng latLng = new LatLng(lat,lng);

        //地图view中心点和缩放级别设置
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel()-3));
        //添加marker
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title("标题").snippet("今天考完试了很开心");
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_target));
        aMap.addMarker(markerOption);

        //添加圆形面范围标识
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.strokeWidth(0);
        circleOptions.fillColor(getResources().getColor(R.color.app_color_transparent));
        circleOptions.radius(50);
        aMap.addCircle(circleOptions);

    }


    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        locationChangedListener = null;
        if (aMapLocationClient != null) {
            aMapLocationClient.stopLocation();
            aMapLocationClient.onDestroy();
        }
        aMapLocationClient = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (aMapLocationClient != null) {
            aMapLocationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

}