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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.OrderApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.OrderDetailResponse;
import cn.czyugang.tcg.client.entity.OrderGoods;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.store.StoreActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.string.RichText;
import cn.czyugang.tcg.client.widget.CalculateOrderView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.MapView;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;


/**
 * @author ruiaa
 * @date 2017/11/28
 */

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.order_detail_statusL)
    View statusL;
    @BindView(R.id.order_detail_status)
    TextView status;
    @BindView(R.id.order_detail_status_sub)
    TextView statusSub;
    @BindView(R.id.order_detail_map)
    MapView mapView;

    @BindView(R.id.order_detail_delivery_nameL)
    TextView deliveryNameL;
    @BindView(R.id.order_detail_delivery_name)
    TextView deliveryName;
    @BindView(R.id.order_detail_delivery_phone)
    TextView deliveryPhone;
    @BindView(R.id.order_detail_coupon_codeL)
    TextView couponCodeL;
    @BindView(R.id.order_detail_coupon_code)
    TextView couponCode;
    @BindView(R.id.order_detail_coupon_status)
    TextView couponStatus;

    //店铺 商品
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

    //配送信息
    @BindView(R.id.order_detail_delivery_way)
    TextView deliveryWay;
    @BindView(R.id.order_detail_delivery_wayL)
    LinearLayout deliveryWayL;
    @BindView(R.id.order_delivery_time)
    TextView deliveryTime;
    @BindView(R.id.order_detail_delivery_timeL)
    LinearLayout deliveryTimeL;
    @BindView(R.id.order_detail_address)
    TextView address;
    @BindView(R.id.order_detail_addressL)
    LinearLayout addressL;
    @BindView(R.id.order_detail_note)
    TextView note;
    @BindView(R.id.order_detail_noteL)
    LinearLayout noteL;
    @BindView(R.id.order_detail_note_bar)
    View noteBottomBar;

    //订单编号 时间
    @BindView(R.id.order_detail_id)
    TextView orderIdText;
    @BindView(R.id.order_detail_pay_type)
    TextView payType;
    @BindView(R.id.order_detail_pay_typeL)
    TextView payTypeL;
    @BindView(R.id.order_detail_time_book)
    TextView timeBook;
    @BindView(R.id.order_detail_time_bookL)
    View timeBookL;
    @BindView(R.id.order_detail_time_cancel)
    TextView timeCancel;
    @BindView(R.id.order_detail_time_cancelL)
    View timeCancelL;
    @BindView(R.id.order_detail_time_pay)
    TextView timePay;
    @BindView(R.id.order_detail_time_payL)
    View timePayL;
    @BindView(R.id.order_detail_time_finish)
    TextView timeFinish;
    @BindView(R.id.order_detail_time_finishL)
    View timeFinishL;

    //底部按钮
    @BindView(R.id.item_order_cancel)
    View cancel;
    @BindView(R.id.item_order_delete)
    View delete;
    @BindView(R.id.item_order_pay)
    View pay;
    @BindView(R.id.item_order_buy_again)
    View buyAgain;
    @BindView(R.id.item_order_comment)
    View comment;
    @BindView(R.id.item_order_more_comment)
    View commentMore;
    @BindView(R.id.item_order_receipt)
    View receipt;


    private String orderId = "";
    private boolean showMapView = true;
    private OrderDetailResponse response = null;

    public static void startOrderDetailActivity(String orderId) {
        Intent intent = new Intent(getTopActivity(), OrderDetailActivity.class);
        intent.putExtra("id", orderId);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra("id");
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);


        goodsR.setLayoutManager(new LinearLayoutManager(this));
        goodsR.addItemDecoration(new RecycleViewDivider(this));

/*        calculateL.addItem("商品总价", "￥99.999")
                .addItem("打包费", "+￥10.00")
                .addItem("配送费", "+￥99.99")
                .addItem("代", "平台优惠券名称", " -￥10.00")
                .build();*/


        OrderApi.getOrderDetail(orderId).subscribe(new NetObserver<OrderDetailResponse>() {
            @Override
            public void onNext(OrderDetailResponse response) {
                super.onNext(response);
                response.parse();
                OrderDetailActivity.this.response = response;
                initTopStatus();
                initMapView(savedInstanceState);
                initDeliveryMan();
                initStoreGoods();
                initDeliveryWayNote();
                initOrderIdTime();
                initBottomButton();
            }
        });
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    /*
    *   顶部状态
    * */
    private void initTopStatus() {
        //WAIT_PAY:待付款。PAY:已付款。ORDERS已接单。DELIVERY:已发货。REACH:已送达。FINISH:已完成 status=CLOSE
        switch (response.data.status) {
            case "CLOSE": {
                statusL.setBackgroundResource(R.color.grey_500);
                status.setText("订单已取消>");
                statusSub.setText(response.data.closeReason);
                break;
            }
            case "WAIT_PAY": {
                //待付款
                statusL.setBackgroundResource(R.color.main_light_red);
                status.setText("订单待支付>");
                statusSub.setText("请在提交订单后40min内完成支付");
                break;
            }
            case "PAY": {
                status.setText("订单已支付，商家待接单>");
                statusSub.setText("商家在XXmin内未接单，将自动取消订单");
                break;
            }
            case "ORDERS": {
                //待发货   取消订单 | 再次购买
                status.setText("订单已支付，商家已接单>");
                statusSub.setText("商家正在为您准备商品，备货时间为XXmin");
                break;
            }
            case "DELIVERY": {
                //已发货   再次购买
                status.setText("订单已支付，商家已发货>");
                statusSub.setText("商家已备货完毕，待配送员接单配送");
                // statusSub.setText("商家已备货完毕，将由商家配送员为您配送");
                break;
            }
            case "REACH": {
                //已送达   确认收货 | 再次购买
                status.setText("");
                statusSub.setText("");
                break;
            }
            case "FINISH": {
                //待评价    删除订单 | 评价 | 再次购买
                status.setText("");
                statusSub.setText("");
                break;
            }
            default: {
                status.setText("");
                statusSub.setText("");
            }
        }


    }

    @OnClick(R.id.order_detail_statusL)
    public void onOrderTrack() {
        OrderTrackActivity.startOrderTrackActivity();
    }

    /*
    *   小地图
    * */
    private void initMapView(@Nullable Bundle savedInstanceState) {
        showMapView = false;

        if (!showMapView) return;
        mapView.onCreate(savedInstanceState);
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

    /*
    *   配送电话 、 自提码
    * */
    private void initDeliveryMan() {
        deliveryNameL.setVisibility(View.GONE);
        couponCodeL.setVisibility(View.GONE);
    }

    @OnClick({R.id.order_detail_call, R.id.order_detail_delivery_phone})
    public void onCall() {
        MyDialog.phoneDialog(this, "13138705415");
    }

    @OnClick({R.id.order_detail_coupon_open_qr, R.id.order_detail_coupon_qr})
    public void onOpenQR() {
        MyDialog.qrCodeDialog(this, "llll");
    }

    /*
    *   店铺商品
    * */
    private void initStoreGoods() {
        store.setText(response.store.name);
        if (goodsR.getAdapter() == null)
            goodsR.setAdapter(new OrderGoodsAdapter(response.data.goodsList, OrderDetailActivity.this));
    }

    @OnClick(R.id.order_detail_storeL)
    public void onStore() {
        StoreActivity.startStoreActivity(response.store.id);
    }

    /*
    *   配送方式 收货地址 留言
    * */
    private void initDeliveryWayNote() {
        if (response.deliveryInfo == null) {
            deliveryTimeL.setVisibility(View.GONE);
            deliveryWayL.setVisibility(View.GONE);
            addressL.setVisibility(View.GONE);
            noteL.setVisibility(View.GONE);
            noteBottomBar.setVisibility(View.GONE);
            return;
        }
        deliveryWay.setText(response.deliveryInfo.getTypeStr());
        deliveryTime.setText(response.deliveryInfo.expectDeliveryTime);
        address.setText(response.getTotalAddressStr());
        note.setText(response.note);
    }

    /*
    *   订单编号 支付方式 下单时间
    * */
    private void initOrderIdTime() {
        orderIdText.setText(response.orderNo);
        String payTypeStr = response.getPayTypeStr();
        if (payTypeStr.equals("")) {
            payTypeL.setVisibility(View.GONE);
        } else {
            payType.setText(payTypeStr);
        }

        if (response.schedule == null) return;
        timeBook.setText(response.schedule.create);
        if (CommonUtil.responseIsNull(response.schedule.close)) {
            timeCancelL.setVisibility(View.GONE);
        } else {
            timeCancel.setText(response.schedule.close);
        }
        if (CommonUtil.responseIsNull(response.schedule.pay)) {
            timePayL.setVisibility(View.GONE);
        } else {
            timePay.setText(response.schedule.pay);
        }
        if (CommonUtil.responseIsNull(response.schedule.evaluate)) {
            timeFinishL.setVisibility(View.GONE);
        } else {
            timeFinish.setText(response.schedule.evaluate);
        }
    }

    @OnClick(R.id.order_detail_id_copy)
    public void onCopyOrderId() {
        AppUtil.copyToClipBoard(response.orderNo);
    }


    /*
    *   底部按钮
    * */
    private void initBottomButton() {
        cancel.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        comment.setVisibility(View.GONE);
        commentMore.setVisibility(View.GONE);
        pay.setVisibility(View.GONE);
        buyAgain.setVisibility(View.GONE);
        receipt.setVisibility(View.GONE);

        cancel.setOnClickListener(v -> {
            onCancel();
        });
        delete.setOnClickListener(v -> {
            onDelete();
        });
        comment.setOnClickListener(v -> {
            onComment();
        });
        commentMore.setOnClickListener(v -> {
            onCommentMore();
        });
        receipt.setOnClickListener(v -> {
            onReceipt();
        });
        buyAgain.setOnClickListener(v -> {
            onBuyAgain();
        });
        pay.setOnClickListener(v -> {
            onPay();
        });

        //WAIT_PAY:待付款。PAY:已付款。ORDERS已接单。DELIVERY:已发货。REACH:已送达。FINISH:已完成
        switch (response.data.status) {
            case "WAIT_PAY": {
                //待付款  取消订单 | 删除订单| 去付款
                cancel.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                pay.setVisibility(View.VISIBLE);
                break;
            }
            case "PAY":
            case "ORDERS": {
                //待发货   取消订单 | 再次购买
                cancel.setVisibility(View.VISIBLE);
                buyAgain.setVisibility(View.VISIBLE);
                break;
            }
            case "DELIVERY": {
                //待收货   再次购买
                buyAgain.setVisibility(View.VISIBLE);
                break;
            }
            case "REACH": {
                //待收货   确认收货 | 再次购买
                receipt.setVisibility(View.VISIBLE);
                buyAgain.setVisibility(View.VISIBLE);
                break;
            }
            case "FINISH": {
                //待评价    删除订单 | 评价 | 再次购买
                delete.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
                buyAgain.setVisibility(View.VISIBLE);
                break;
            }
            default: {
                delete.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onCancel() {

    }

    private void onDelete() {

    }

    private void onComment() {

    }

    private void onCommentMore() {

    }

    private void onBuyAgain() {

    }

    private void onReceipt() {

    }

    private void onPay() {

    }

    static class OrderGoodsAdapter extends RecyclerView.Adapter<OrderGoodsAdapter.Holder> {
        private List<OrderGoods> list;
        private Activity activity;

        public OrderGoodsAdapter(List<OrderGoods> list, Activity activity) {
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
            OrderGoods data = list.get(position);
            holder.imgView.id(data.picId);
            holder.name.setText(data.title);
            holder.spec.setText(data.getSpec());
            holder.labels.setTexts(data.getLabels());
            holder.price.setText(CommonUtil.formatPrice(data.realPrice));
            if (data.unitPrice > data.realPrice)
                holder.priceOrigin.setText(RichText.setStrikethrough(CommonUtil.formatPrice(data.unitPrice)));
            holder.num.setText("x" + data.number);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            ImgView imgView;
            TextView name;
            TextView spec;
            LabelLayout labels;
            TextView price;
            TextView priceOrigin;
            TextView num;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                imgView = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                spec = itemView.findViewById(R.id.item_spec);
                labels = itemView.findViewById(R.id.item_labels);
                price = itemView.findViewById(R.id.item_price);
                priceOrigin = itemView.findViewById(R.id.item_price_origin);
                num = itemView.findViewById(R.id.item_num);
            }
        }
    }
}
