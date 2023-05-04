package com.example.cugerhuo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alicom.tools.networking.Response;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.example.cugerhuo.activity.post.PostBuyActivity;

/**
 * 聊天中地理位置点开查看
 * @Author: 李柏睿
 * @Time: 2023/5/3 19:19
 */
public class LocationDetailActivity extends AppCompatActivity implements LocationSource, View.OnClickListener, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener{
    MapView mMapView = null;
    /**初始化地图控制器对象*/
    AMap aMap;
    /**定位需要的数据*/
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    /**定位蓝点*/
    MyLocationStyle myLocationStyle;
    /**定义一个UiSettings对象*/
    private UiSettings mUiSettings;

    private TextView tvLocation, tvLocationHeader;
    private View rvHeadView;
    private ImageView ivLocation;

    private TextView cancelLocation;
    private Button sendLocation;
    LatLng latLng;

    /**
     * 聊天对象
     */
    private PartUserInfo chatUser=new PartUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        /**
         * 从上个界面获取聊天对象信息
         */
        Intent intent =getIntent();
        chatUser= (PartUserInfo) intent.getSerializableExtra("chatUser");
        /**动态申请定位权限*/
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)&ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x20);
        }
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        tvLocation = findViewById(R.id.tvLocation);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState),创建地图
        mMapView.onCreate(savedInstanceState);
        ivLocation = findViewById(R.id.iv_location);
        cancelLocation = findViewById(R.id.cancel_location);
        cancelLocation.setOnClickListener(this);
        sendLocation = findViewById(R.id.send_position);
        sendLocation.setOnClickListener(this);
        if (aMap == null) {
            aMap = mMapView.getMap();

        }
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);

        //蓝点初始化
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.showMyLocation(true);

        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0)
                    {
                        String location = amapLocation.getPoiName();//获取当前定位点的AOI信息
                        Log.e("定位地点：", location);
                        tvLocation.setText(location);
                        mlocationClient.stopLocation();

                    }
                    else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        AMapLocationClient.setApiKey("9f769df4a1a71a366247dc9da243750b");
        AMapLocationClient.updatePrivacyAgree(LocationDetailActivity.this,true);
        AMapLocationClient.updatePrivacyShow(LocationDetailActivity.this,true,true);
        //设置定位回调监听
        mlocationClient.setLocationListener(mLocationListener);

        //地图移动监听
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                L.cc(cameraPosition.toString());
//                setMarker(cameraPosition.target);
                animTranslate();
                try {
                    getGeocodeSearch(cameraPosition.target);
                    latLng = cameraPosition.target;
                } catch (AMapException e) {
                    e.printStackTrace();
                }
            }
        });

        /**实例化UiSettings类对象*/
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);

    }

    /**
     * 点击事件函数
     * @Author: 李柏睿
     * @Time: 2023/4/22 20:12
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            /**
             * 取消
             * @Author: 李柏睿
             * @Time: 2023/5/4
             */
            case R.id.cancel_location:
                finish();
                break;
            /**
             * 发送地理信息
             * @Author: 李柏睿
             * @Time: 2023/5/4
             */
            case R.id.send_position:
                int isSendLocation = 1;
                Intent intent=new Intent(LocationDetailActivity.this, ChatActivity.class);
                intent.putExtra("isSendLocation",isSendLocation);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);
                intent.putExtra("poiName",tvLocation.getText());
                intent.putExtra("chatUser",chatUser);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i != 1000) {return;}
        tvLocation.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //--定位监听---------

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            try {
                mlocationClient = new AMapLocationClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);

            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    //定位回调  在回调方法中调用“mListener.onLocationChanged(amapLocation);”可以在地图上显示系统小蓝点。
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                // 显示系统小蓝点
                mListener.onLocationChanged(aMapLocation);

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("定位AmapErr", errText);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    private AnimatorSet animatorSet;
    public void animTranslate(){
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(ivLocation, "scaleX", 1, 0.5f, 1).setDuration(300)
                    , ObjectAnimator.ofFloat(ivLocation, "scaleY", 1, 0.5f, 1).setDuration(300));
        }animatorSet.start();
    }


    private GeocodeSearch geocoderSearch;

    //逆地理编码获取当前位置信息
    private void getGeocodeSearch(LatLng targe) throws AMapException {
        if (geocoderSearch == null) {geocoderSearch = new GeocodeSearch(this);}
        geocoderSearch.setOnGeocodeSearchListener((GeocodeSearch.OnGeocodeSearchListener) this);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(targe.latitude, targe.longitude), 1000, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }


}