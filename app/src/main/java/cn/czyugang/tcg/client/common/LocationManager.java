package cn.czyugang.tcg.client.common;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by wuzihong on 2017/10/30.
 * 全局位置管理
 */

public class LocationManager implements AMapLocationListener {
    public static final int LOCATION_STATUS_START = 0;
    public static final int LOCATION_STATUS_SUCCEED = 1;
    public static final int LOCATION_STATUS_FAILURE = 2;
    private static LocationManager mInstance;
    private AMapLocationClient mLocationClient;
    private AMapLocation mLocation;
    private int mLocationStatus = LOCATION_STATUS_START;

    private LocationManager(Context context) {
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(this);
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        locationOption.setOnceLocation(true);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(locationOption);
        startLocation();
    }

    /**
     * 初始化，创建实例
     *
     * @return
     */
    public static LocationManager init(Context context) {
        mInstance = new LocationManager(context);
        return mInstance;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static LocationManager getInstance() {
        return mInstance;
    }

    /**
     * 启动定位
     */
    public void startLocation() {
        mLocationClient.startLocation();
        mLocationStatus = LOCATION_STATUS_START;
    }

    /**
     * 获取定位信息
     *
     * @return
     */
    public AMapLocation getLocation() {
        return mLocation;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mLocationStatus = LOCATION_STATUS_SUCCEED;
            mLocation = aMapLocation;
        } else {
            mLocationStatus = LOCATION_STATUS_FAILURE;
        }
    }
}
