package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.MyApplication;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.Order;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgUtil;
import cn.czyugang.tcg.client.widget.CalculateOrderView;
import cn.czyugang.tcg.client.widget.MapView;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;


/**
 * @author ruiaa
 * @date 2017/11/28
 */

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.order_detail_status)
    TextView status;
    @BindView(R.id.order_detail_status_sub)
    TextView statusSub;
    @BindView(R.id.order_detail_map)
    MapView mapView;
    @BindView(R.id.order_detail_store)
    TextView store;
    @BindView(R.id.order_detail_storeL)
    LinearLayout storeL;
    @BindView(R.id.order_detail_goods)
    RecyclerView goodsR;
    @BindView(R.id.order_detail_calculate)
    CalculateOrderView calculateL;
    @BindView(R.id.order_detail_pay_money)
    TextView payMoney;
    @BindView(R.id.order_detail_id)
    TextView orderId;
    @BindView(R.id.order_detail_pay_type)
    TextView payType;
    @BindView(R.id.order_detail_time_book)
    TextView timeBook;
    @BindView(R.id.order_detail_time_cancel)
    TextView timeCancel;
    @BindView(R.id.order_detail_button_one)
    TextView buttonOne;
    @BindView(R.id.order_detail_button_two)
    TextView buttonTwo;
    @BindView(R.id.order_detail_button_three)
    TextView buttonThree;

    private Order order;
    private List<Good> goodList = new ArrayList<>();
    private boolean showMapView = true;

    public static void startOrderDetailActivity(Order order) {
        MyApplication.getInstance().activityTransferData = order;
        Intent intent = new Intent(getTopActivity(), OrderDetailActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        order = (Order) MyApplication.getInstance().activityTransferData;
        MyApplication.getInstance().activityTransferData = null;


        goodList.add(new Good());
        goodList.add(new Good());
        goodList.add(new Good());

        goodsR.setLayoutManager(new LinearLayoutManager(this));
        goodsR.setAdapter(new OrderGoodsAdapter(goodList, this));
        goodsR.addItemDecoration(new RecycleViewDivider(this));

        calculateL.addItem("商品总价", "￥99.999")
                .addItem("打包费", "+￥10.00")
                .addItem("配送费", "+￥99.99")
                .addItem("代", "平台优惠券名称", " -￥10.00")
                .build();

        if (showMapView) {
            mapView.onCreate(savedInstanceState);
            initMapView();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showMapView) mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (showMapView) mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (showMapView) mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (showMapView) mapView.onSaveInstanceState(outState);
    }

    private void initTopStatus() {
        //
    }

    private void initMapView() {
        LatLng latLng = new LatLng(23.657626, 116.621468);
        mapView.mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, GLMapStaticValue.MAP_LEVEL));
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_location);
        int size = ResUtil.getDimenInPx(R.dimen.dp_32);
        mapView.mAMap.addMarker(new MarkerOptions()
                .position(latLng)
                .anchor(0.5f, 0.5f)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(ImgUtil.transfer(icon, size, size)))
        );
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.order_detail_id_copy)
    public void onCopyOrderId() {
        AppUtil.copyToClipBoard(order.id);
    }

    @OnClick({R.id.order_detail_call, R.id.order_detail_delivery_phone})
    public void onCall() {
        MyDialog.phoneDialog(this, "13138705415");
    }

    @OnClick({R.id.order_detail_coupon_open_qr,R.id.order_detail_coupon_qr})
    public void onOpenQR(){
        MyDialog.qrCodeDialog(this,"llll");
    }

    @OnClick(R.id.order_detail_statusL)
    public void onOrderTrack() {
        OrderTrackActivity.startOrderTrackActivity();
    }

    static class OrderGoodsAdapter extends RecyclerView.Adapter<OrderGoodsAdapter.Holder> {
        private List<Good> list;
        private Activity activity;

        public OrderGoodsAdapter(List<Good> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.view_item_good, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Good data = list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
