package com.example.cugerhuo.tools;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 高德定位接口
 * @author 施立豪
 * @time 2023/4/12
 */
public class Location {

    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //位置
    private String location;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {

            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
//                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                    amapLocation.getLatitude();//获取纬度
//                    amapLocation.getLongitude();//获取经度
//                    amapLocation.getAccuracy();//获取精度信息
//                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    amapLocation.getCountry();//国家信息
//                    amapLocation.getProvince();//省信息
//                    amapLocation.getCity();//城市信息
//                    System.out.println(amapLocation.getDistrict());//城区信息
//                    location=amapLocation.getDistrict();
//                    amapLocation.getStreet();//街道信息
//                    amapLocation.getStreetNum();//街道门牌号信息
//                    amapLocation.getCityCode();//城市编码
//                    amapLocation.getAdCode();//地区编码

                    location=amapLocation.getAoiName();//获取当前定位点的AOI信息
//                    amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                    amapLocation.getFloor();//获取当前室内定位的楼层
//                    amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
//可在其中解析amapLocation获取相应内容。
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }

        }
    };
    public String getLocation()
    {
        if(location!=null){
        return location;}
        return "";
    }
    public void initWeb(Context context) throws Exception {
        AMapLocationClient.setApiKey("9f769df4a1a71a366247dc9da243750b");
        AMapLocationClient.updatePrivacyAgree(context,true);
        AMapLocationClient.updatePrivacyShow(context,true,true);
        mLocationClient = new AMapLocationClient(context);//设置定位回调监听

        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption mLocationOption = null;
//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(option);
            mLocationClient.stopLocation();
//            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//           mLocationClient.stopLocation();
//            mLocationClient.startLocation();
        }

//设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();

    }
    public void  init(Context context) throws Exception {
        AMapLocationClient.setApiKey("9f769df4a1a71a366247dc9da243750b");
        AMapLocationClient.updatePrivacyAgree(context,true);
        AMapLocationClient.updatePrivacyShow(context,true,true);
        mLocationClient = new AMapLocationClient(context);//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption mLocationOption = null;
//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(option);
            mLocationClient.stopLocation();
//            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//           mLocationClient.stopLocation();
//            mLocationClient.startLocation();
        }

//设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();

    }
//初始化定位



}
