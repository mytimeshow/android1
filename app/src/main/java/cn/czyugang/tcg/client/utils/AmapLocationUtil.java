package cn.czyugang.tcg.client.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.utils.rxbus.LocationEvent;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;

/**
 * @author ruiaa
 * @date 2017/12/13
 */

public class AmapLocationUtil {
    public static AMapLocation aMapLocation=null;

    public static String getXY(){
        //LogRui.i("getXY####"+aMapLocation.getLongitude()+","+aMapLocation.getLatitude());
        return aMapLocation.getLongitude()+","+aMapLocation.getLatitude();
    }

    public static void startOnceLocation() {
        //声明AMapLocationClient类对象
        final AMapLocationClient mLocationClient = new AMapLocationClient(MyApplication.getContext());
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        //amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        //amapLocation.getLatitude();//获取纬度
                        //amapLocation.getLongitude();//获取经度
                        //amapLocation.getAccuracy();//获取精度信息
                        //amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        //amapLocation.getCountry();//国家信息
                        //amapLocation.getProvince();//省信息
                        //amapLocation.getCity();//城市信息
                        //amapLocation.getDistrict();//城区信息
                        //amapLocation.getStreet();//街道信息
                        //amapLocation.getStreetNum();//街道门牌号信息
                        //amapLocation.getCityCode();//城市编码
                        //amapLocation.getAdCode();//地区编码
                        //amapLocation.getAoiName();//获取当前定位点的AOI信息
                        //amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                        //amapLocation.getFloor();//获取当前室内定位的楼层
                        aMapLocation=amapLocation;

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogRui.e("onLocationChanged####" + "AmapError   location Error, "
                                + "ErrCode:" + amapLocation.getErrorCode()
                                + ", errInfo:" + amapLocation.getErrorInfo());
                    }
                }
                RxBus.post(new LocationEvent());
                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption  = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：     该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果： 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
}
