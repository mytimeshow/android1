package cn.czyugang.tcg.client.modules.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Discount;
import cn.czyugang.tcg.client.entity.OrderPreSettleResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.entity.TrolleyGoods;
import cn.czyugang.tcg.client.modules.discount.SelectDiscountActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.storage.AppKeyStorage;
import cn.czyugang.tcg.client.widget.CalculateOrderView;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class ConfirmOrderActivity extends BaseActivity {

    private static final int REQUEST_CODE_ADDRESS = 1;
    private static final int REQUEST_CODE_DELIVERY = 2;
    private static final int REQUEST_CODE_STORE_ACTIVITY = 3;
    private static final int REQUEST_CODE_STORE_COUPON = 4;
    private static final int REQUEST_CODE_PLATFORM_ACTIVITY = 5;
    private static final int REQUEST_CODE_PLATFORM_COUPON = 6;

    @BindView(R.id.confirm_order_scroll)
    NestedScrollView scrollView;
    @BindView(R.id.confirm_order_address_out)
    TextView addressOut;
    @BindView(R.id.confirm_order_address_name)
    TextView addressName;
    @BindView(R.id.confirm_order_address_location)
    TextView addressLocation;
    @BindView(R.id.confirm_order_address_bottom)
    TextView addressBottom;
    @BindView(R.id.confirm_order_deliveryL)
    View deliveryL;
    @BindView(R.id.confirm_order_delivery)
    TextView delivery;

    @BindView(R.id.confirm_order_money)
    TextView money;
    @BindView(R.id.confirm_order_commit)
    TextView commit;

    @BindView(R.id.confirm_order_stores)
    RecyclerView settleStoresR;
    private SettleStoreAdapter settleStoreAdapter;

    private String shoppingCartIds = "";
    private OrderPreSettleResponse preSettleResponse = null;
    private Map<String, String> deliveryTimeMap = new HashMap<>();
    private Map<String, String> deliveryWayMap = new HashMap<>();
    private Map<String, String> noteToStoreMap = new HashMap<>();
    private String selectAddressId = null;


    public static void startConfirmOrderActivity() {
        Intent intent = new Intent(getTopActivity(), ConfirmOrderActivity.class);
        getTopActivity().startActivity(intent);
    }

    public static void startConfirmOrderActivity(String shoppingCartIds) {
        Intent intent = new Intent(getTopActivity(), ConfirmOrderActivity.class);
        intent.putExtra("shoppingCartIds", shoppingCartIds);
        getTopActivity().startActivity(intent);
    }

    public static void startConfirmOrderActivity(List<String> shoppingCartIds) {
        Intent intent = new Intent(getTopActivity(), ConfirmOrderActivity.class);
        String str = shoppingCartIds.toString();
        str = str.replaceAll(" ", "");
        if (str.length() <= 2) return;
        intent.putExtra("shoppingCartIds", str.substring(1, str.length() - 1));
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        shoppingCartIds = getIntent().getStringExtra("shoppingCartIds");

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() ->
                addressBottom.setVisibility(scrollView.getScrollY() > 70 ? View.VISIBLE : View.GONE));

        settleStoreAdapter = new SettleStoreAdapter();
        settleStoresR.setLayoutManager(new LinearLayoutManager(this));
        settleStoresR.setAdapter(settleStoreAdapter);
        settleStoresR.setNestedScrollingEnabled(false);

        preSettleTrolley();
    }

    private void preSettleTrolley() {
        String deliveryWays = null;
        if (preSettleResponse != null) {
            StringBuilder builder = new StringBuilder();
            deliveryTimeMap.clear();
            deliveryWayMap.clear();
            for (OrderPreSettleResponse.StoreMoreInfo moreInfo : preSettleResponse.moreInfoHashMap.values()) {
                deliveryTimeMap.put(moreInfo.storeId, moreInfo.selectedDeliveryTime);
                deliveryWayMap.put(moreInfo.storeId, moreInfo.selectedDeliveryWay);
                builder.append(moreInfo.storeId);
                builder.append("-");
                builder.append(OrderPreSettleResponse.StoreMoreInfo.transferDeliveryType(moreInfo.selectedDeliveryWay));
                builder.append("-");
                builder.append(moreInfo.selectedDeliveryTime);
                builder.append(",");
            }
            if (builder.length() > 0) builder.deleteCharAt(builder.length() - 1);
            deliveryWays = builder.toString();
        }
        StoreApi.preSettleTrolley(shoppingCartIds, selectAddressId, deliveryWays).subscribe(new NetObserver<OrderPreSettleResponse>() {
            @Override
            public void onNext(OrderPreSettleResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    response.parse();
                    if (preSettleResponse != null)
                        response.setDeliveryTimeAndWay(deliveryTimeMap, deliveryWayMap);

                    preSettleResponse = response;
                    settleStoreAdapter.notifyDataSetChanged();
                    showAddressAndPrice();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_DELIVERY) {
            preSettleTrolley();
            return;
        }
        if (requestCode == REQUEST_CODE_ADDRESS) {
            selectAddressId = SelectAddressActivity.getAddressId(data);
            preSettleTrolley();
            return;
        }
    }

    private void showAddressAndPrice() {
        if (preSettleResponse.address == null) return;
        selectAddressId = preSettleResponse.address.id;
        addressName.setText(preSettleResponse.address.getLinkmanAndPhone());
        addressLocation.setText(preSettleResponse.address.getAddress());
        addressBottom.setText("收货地址：" + preSettleResponse.address.getAddress());
        money.setText(CommonUtil.formatPrice(preSettleResponse.totalRealPrice));
        if (preSettleResponse.isAllInDeliveryRange()) {
            addressOut.setVisibility(View.GONE);
            commit.setBackgroundResource(R.color.main_red);
            commit.setClickable(true);
        } else {
            addressOut.setVisibility(View.VISIBLE);
            commit.setBackgroundResource(R.color.grey_350);
            commit.setClickable(false);
        }
    }

    @OnClick(R.id.confirm_order_addressL)
    public void onAddress() {
        SelectAddressActivity.startSelectAddressActivity(this, REQUEST_CODE_ADDRESS);
    }

    public void onDeliver(Store store) {
        SelectDeliveryActivity.startSelectDeliveryActivity(this, REQUEST_CODE_DELIVERY, store, preSettleResponse.getMoreInfo(store));
    }

    @OnClick(R.id.confirm_order_commit)
    public void onCommit() {
        if (preSettleResponse == null) return;
        if (preSettleResponse.address == null || preSettleResponse.address.id == null) {
            AppUtil.toast("请先选择收货地址");
            return;
        }
        confirmOrder();
    }

    private void confirmOrder() {
        for (OrderPreSettleResponse.StoreMoreInfo storeMoreInfo : preSettleResponse.moreInfoHashMap.values()) {
            if (noteToStoreMap.containsKey(storeMoreInfo.storeId))
                storeMoreInfo.noteToStore = noteToStoreMap.get(storeMoreInfo.storeId);
        }
        StoreApi.settleTrolley(preSettleResponse).subscribe(new NetObserver<Response<List<String>>>() {
            @Override
            public void onNext(Response<List<String>> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    if (response.data == null || response.data.isEmpty()) return;
                    new Thread(() -> {
                        for (TrolleyGoods t : preSettleResponse.trolleyGoodsMap.values()) {
                            AppKeyStorage.clearTrolleyAfterSettle(t.storeId, t.trolleyId);
                        }
                        for (Store store : preSettleResponse.data) {
                            AppKeyStorage.clearTrolleyDeleteFlag(store.id);
                        }
                    }).start();
                    PayOrderActivity.startPayOrderActivity(response.data.get(0));
                    commit.postDelayed(() -> clearAllActivityExceptMainAndTop(), 300);
                }
            }
        });
    }

    class SettleStoreAdapter extends RecyclerView.Adapter<SettleStoreAdapter.Holder> {
        public SettleStoreAdapter() {

        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_settle_store, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Store store = preSettleResponse.data.get(position);
            OrderPreSettleResponse.StoreMoreInfo storeMoreInfo = preSettleResponse.getMoreInfo(store);

            //是否在配送范围
            holder.addressDisable.setVisibility(storeMoreInfo.isInDeliveryRange ? View.GONE : View.VISIBLE);

            LogRui.i("onBindViewHolder####", storeMoreInfo.selectedDeliveryWay);
            //配送方式
            if (preSettleResponse.data.size() > 1) {
                deliveryL.setVisibility(View.GONE);
                holder.deliveryL.setVisibility(View.VISIBLE);
                holder.delivery.setText(storeMoreInfo.selectedDeliveryWay);
                holder.deliveryL.setOnClickListener(v -> onDeliver(store));
            } else {
                holder.deliveryL.setVisibility(View.GONE);
                deliveryL.setVisibility(View.VISIBLE);
                delivery.setText(storeMoreInfo.selectedDeliveryWay);
                deliveryL.setOnClickListener(v -> onDeliver(store));
            }

            //店铺商品
            holder.storeName.setText(store.name);
            holder.storeDelivery.setVisibility(View.GONE);
            holder.goodsNum.setText(storeMoreInfo.getGoodsNum() + "件");
            holder.goodsNum.setOnClickListener(v -> OrderGoodsListActivity.startOrderGoodsListActivity(storeMoreInfo.trolleyGoodsList));

            storeMoreInfo.bindImgAdapter(ConfirmOrderActivity.this, holder.goodsR);

            //优惠券
            holder.storeActivity.setOnClickListener(v ->
                    SelectDiscountActivity.startSelectDiscountActivity(Discount.TYPE_STORE_ACTIVITY, store.id));
            holder.storeCoupon.setOnClickListener(v ->
                    SelectDiscountActivity.startSelectDiscountActivity(Discount.TYPE_STORE_COUPON, store.id));
            holder.platformActivity.setOnClickListener(v ->
                    SelectDiscountActivity.startSelectDiscountActivity(Discount.TYPE_PLATFORM_ACTIVITY, store.id));
            holder.platformCoupon.setOnClickListener(v ->
                    SelectDiscountActivity.startSelectDiscountActivity(Discount.TYPE_PLATFORM_COUPON, store.id));

/*            holder.orderCalculate.addItem("减", "满1减20", "-￥ 1.0");
            holder.orderCalculate.addItem("减", "满1减20", "-￥ 2.0");
            holder.orderCalculate.addItem("减", "满1减20", "-￥ 3.0");
            holder.orderCalculate.addItem("配送费", "+￥ 10", v -> {
                AppUtil.toast("ggg");
            });
            holder.orderCalculate.build();*/

            //留言
            holder.messageInput.setText(noteToStoreMap.containsKey(storeMoreInfo.storeId) ? noteToStoreMap.get(storeMoreInfo.storeId) : "");
            holder.messageInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    noteToStoreMap.put(storeMoreInfo.storeId, s.toString());
                }
            });
        }

        @Override
        public int getItemCount() {
            if (preSettleResponse == null) return 0;
            if (preSettleResponse.data == null) return 0;
            return preSettleResponse.data.size();
        }


        class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.confirm_order_store_name)
            TextView storeName;
            @BindView(R.id.confirm_order_store_delivery)
            TextView storeDelivery;
            @BindView(R.id.confirm_order_goods)
            RecyclerView goodsR;
            @BindView(R.id.confirm_order_goods_num)
            TextView goodsNum;
            @BindView(R.id.confirm_order_deliveryL)
            View deliveryL;
            @BindView(R.id.confirm_order_delivery)
            TextView delivery;

            @BindView(R.id.confirm_order_discount_goods_limit)
            TextView limitDiscountGoods;
            @BindView(R.id.confirm_order_store_activity_limit)
            TextView limitStoreActivity;
            @BindView(R.id.confirm_order_store_coupon_limit)
            TextView limitStoreCoupon;
            @BindView(R.id.confirm_order_app_activity_limit)
            TextView limitPlatformActivity;
            @BindView(R.id.confirm_order_app_coupon_limit)
            TextView limitPlatformCoupon;
            //店铺活动优惠
            @BindView(R.id.confirm_order_store_activity)
            View storeActivity;
            @BindView(R.id.confirm_order_store_activity_name)
            TextView storeActivityName;
            //商家优惠券
            @BindView(R.id.confirm_order_store_coupon)
            View storeCoupon;
            @BindView(R.id.confirm_order_store_coupon_num)
            TextView storeCouponNum;
            @BindView(R.id.confirm_order_store_coupon_name)
            TextView storeCouponName;
            //平台活动优惠
            @BindView(R.id.confirm_order_app_activity)
            View platformActivity;
            @BindView(R.id.confirm_order_app_activity_name)
            TextView platformActivityName;
            //平台优惠券
            @BindView(R.id.confirm_order_app_coupon)
            View platformCoupon;
            @BindView(R.id.confirm_order_app_coupon_num)
            TextView platformCouponNum;
            @BindView(R.id.confirm_order_app_coupon_name)
            TextView platformCouponName;

            @BindView(R.id.confirm_order_calculate)
            CalculateOrderView orderCalculate;
            @BindView(R.id.confirm_order_message)
            EditText messageInput;
            @BindView(R.id.confirm_order_address_disable)
            View addressDisable;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
