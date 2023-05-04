package com.example.cugerhuo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.tools.MyToast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 范围打卡
 * @Author: 李柏睿
 * @Time: 2023/5/4 19:38
 */
public class LocationClockActivity extends AppCompatActivity implements LocationSource, AMapLocationListener{

    MapView mapView;
    TextView btn_check_in;

    /**地图对象*/
    private AMap aMap;
    private Circle circle;
    /**定位坐标*/
    private LatLng locLatLng = null;
    /**打卡坐标*/
    private LatLng comLatLng = null;
    private float radius = 200;

    /**定位需要的声明*/
    /**定位发起端*/
    private AMapLocationClient mLocationClient = null;
    /**定位参数*/
    private AMapLocationClientOption mLocationOption = null;
    /**定位监听器*/
    private LocationSource.OnLocationChangedListener mListener = null;
    /**标识，用于判断是否只显示一次定位信息和用户重新定位*/
    private boolean isFirstLoc = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_clock);

        mapView = findViewById(R.id.map);
        btn_check_in = findViewById(R.id.btn_check_in);
        // 启动新的线程
        new TimeThread().start();
        // 显示地图，必须要写
        mapView.onCreate(savedInstanceState);
        initView();
        btn_check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locLatLng != null && comLatLng != null) {
                    float distance = AMapUtils.calculateLineDistance(locLatLng, comLatLng);
                    if (distance <= radius) {
                        // 这里模拟把打卡的信息提交到服务器，服务器并且把打卡成功信息返回给客户端
                        MyToast.toast(LocationClockActivity.this,"打卡成功",3);
                       PartUserInfo a= (PartUserInfo) getIntent().getSerializableExtra("chatUser");
                        Intent intent1 = new Intent();
                        setResult(0x11,intent1);
                        intent1.putExtra("location","1");
                        intent1.putExtra("chatUser",a);

                        finish();
                    } else {
                        MyToast.toast(LocationClockActivity.this,"当前位置不打卡范围内，打卡失败",1);
                    }
                } else {
                    MyToast.toast(LocationClockActivity.this,"位置初始化异常，打卡失败",1);

                }
            }
        });
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        amapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);//定位时间
                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        amapLocation.getCountry();//国家信息
                        amapLocation.getProvince();//省信息
                        amapLocation.getCity();//城市信息
                        amapLocation.getDistrict();//城区信息
                        amapLocation.getStreet();//街道信息
                        amapLocation.getStreetNum();//街道门牌号信息
                        amapLocation.getCityCode();//城市编码
                        amapLocation.getAdCode();//地区编码
                        // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                        if (isFirstLoc) {
                            //设置缩放级别
                            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                            //将地图移动到定位点
                            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                            //点击定位按钮 能够将地图的中心移动到定位点
                            mListener.onLocationChanged(amapLocation);
                            //添加图钉
                            aMap.addMarker(getMarkerOptions(amapLocation));
                            //获取定位信息
                            StringBuffer buffer = new StringBuffer();
                            buffer.append(amapLocation.getAddress());
//                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                            Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
//                    L.i("定位地址：" + buffer.toString());
                            // 记录当前定位的坐标
                            locLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                            isFirstLoc = false;
                        }
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);
        initData();
    }

    private void initView() {

        //获取地图对象
        aMap = mapView.getMap();
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否显示地图方向盘
        settings.setCompassEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        //定位的小图标 默认是蓝点，其实就是一张图片
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_point));
//        myLocationStyle.radiusFillColor(R.color.transparent1);
//        myLocationStyle.strokeColor(R.color.transparent1);
//        aMap.setMyLocationStyle(myLocationStyle);

        /**动态申请定位权限*/
        int hasWriteStoragePermission2 = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)&ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteStoragePermission2 == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x20);
        }

    }

    private void initData() {

    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            try {
                mLocationClient = new AMapLocationClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);

            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }

    }


    /** 自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息*/
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_company));
        // 记录打卡坐标
        comLatLng = new LatLng(30.457575,114.617139);
        options.position(comLatLng);
        // 绘制圆圈
        drawCircle(comLatLng);
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getAddress());
        //标题
//        options.title(buffer.toString());
        options.title("公司：酷公司，用钉钉");
        //子标题
        options.snippet("地址：" + amapLocation.getProvince() + amapLocation.getCity() +
                amapLocation.getDistrict() + amapLocation.getStreet());
        //设置多少帧刷新一次图片资源
        options.period(60);
        return options;

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

    /**
     * 绘制圆圈
     *
     * @param latLng
     */
    public void drawCircle(LatLng latLng) {
        if (circle != null) {
            circle = null;
        }
        circle = aMap.addCircle(new CircleOptions()
                .center(latLng).radius(radius)
                .fillColor(R.color.app_color_blue).strokeColor(R.color.app_color_blue)
                .strokeWidth(5));
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  // 消息(一个整型值)
                    mHandler.sendMessage(msg); // 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    // 在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis(); // 获取系统时间
                    CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime); // 时间显示格式
                    btn_check_in.setText(String.format("交易打卡\n%s", sysTimeStr)); // 实时更新时间
                    break;
                default:
                    break;
            }
            return false;
        }
    });
}