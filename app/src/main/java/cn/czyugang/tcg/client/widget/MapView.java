package cn.czyugang.tcg.client.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import cn.czyugang.tcg.client.R;

/**
 * Created by wuzihong on 2017/10/30.
 */

public class MapView extends com.amap.api.maps.MapView {
    public AMap mAMap;

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
        init();
    }

    public void addMarker(LatLng location, int resId, float anchorX, float anchorY) {
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), resId);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        mAMap.addMarker(new MarkerOptions().position(location).icon(des).anchor(anchorX, anchorY));
    }

    private void init() {
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        locationStyle.radiusFillColor(Color.TRANSPARENT);
        locationStyle.strokeWidth(0);
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_blue_point)));
        mAMap = getMap();
        mAMap.setMyLocationStyle(locationStyle);
        mAMap.setMyLocationEnabled(true);
        UiSettings settings = mAMap.getUiSettings();
        settings.setLogoBottomMargin(-100);
        settings.setZoomControlsEnabled(false);
    }
}
