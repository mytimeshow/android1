package cn.czyugang.tcg.client.modules.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.widget.MapView;

/**
 * @author ruiaa
 * @date 2017/11/29
 */

public class OrderTrackMapActivity extends BaseActivity {
    @BindView(R.id.order_track_map)
    MapView mapView;

    public static void startOrderTrackMapActivity() {
        Intent intent = new Intent(getTopActivity(), OrderTrackMapActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track_map);
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
        LatLng latLng = new LatLng(23.657626, 116.621468);
        mapView.mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, GLMapStaticValue.MAP_LEVEL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }
}
