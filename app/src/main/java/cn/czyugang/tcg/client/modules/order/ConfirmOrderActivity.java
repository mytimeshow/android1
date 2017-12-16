package cn.czyugang.tcg.client.modules.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.OrderPreSettleResponse;
import cn.czyugang.tcg.client.entity.Store;
import cn.czyugang.tcg.client.utils.app.AppUtil;
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
    private static final int REQUEST_CODE_APP_ACTIVITY = 5;
    private static final int REQUEST_CODE_APP_COUPON = 6;

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
    private OrderPreSettleResponse preSettleResponse;

    public static void startConfirmOrderActivity() {
        Intent intent = new Intent(getTopActivity(), ConfirmOrderActivity.class);
        getTopActivity().startActivity(intent);
    }

    public static void startConfirmOrderActivity(String shoppingCartIds) {
        Intent intent = new Intent(getTopActivity(), ConfirmOrderActivity.class);
        intent.putExtra("shoppingCartIds", shoppingCartIds);
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


        StoreApi.preSettleTrolley(shoppingCartIds, null, null).subscribe(new NetObserver<OrderPreSettleResponse>() {
            @Override
            public void onNext(OrderPreSettleResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    response.parse();
                    preSettleResponse = response;
                    settleStoreAdapter.notifyDataSetChanged();

                    showAddress();
                }
            }
        });
    }

    private void showAddress() {
        if (preSettleResponse.address == null) return;
        addressOut.setVisibility(preSettleResponse.isAllInDeliveryRange() ? View.GONE : View.VISIBLE);
        addressName.setText(preSettleResponse.address.getLinkmanAndPhone());
        addressLocation.setText(preSettleResponse.address.getAddress());
        addressBottom.setText("收货地址：" + preSettleResponse.address.getAddress());
    }

    @OnClick(R.id.confirm_order_addressL)
    public void onAddress() {
        SelectAddressActivity.startSelectAddressActivity(this, REQUEST_CODE_ADDRESS);
    }

    public void onDeliver(Store store) {
        SelectDeliveryActivity.startSelectDeliveryActivity(this, REQUEST_CODE_DELIVERY,store,preSettleResponse.getMoreInfo(store));
    }

    @OnClick(R.id.confirm_order_commit)
    public void onCommit() {
        PayOrderActivity.startPayOrderActivity();
    }

    class SettleStoreAdapter extends RecyclerView.Adapter<SettleStoreAdapter.Holder> {

        private List<Store> storeList = new ArrayList<>();

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

            holder.orderCalculate.addItem("减", "满1减20", "-￥ 1.0");
            holder.orderCalculate.addItem("减", "满1减20", "-￥ 2.0");
            holder.orderCalculate.addItem("减", "满1减20", "-￥ 3.0");
            holder.orderCalculate.addItem("配送费", "+￥ 10", v -> {
                AppUtil.toast("ggg");
            });
            holder.orderCalculate.build();
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
            @BindView(R.id.confirm_order_store_activity_num)
            TextView storeActivityNum;
            @BindView(R.id.confirm_order_store_coupon_num)
            TextView couponNum;
            @BindView(R.id.confirm_order_store_coupon_name)
            TextView couponName;
            @BindView(R.id.confirm_order_app_activity_name)
            TextView appActivityName;
            @BindView(R.id.confirm_order_app_coupon_num)
            TextView appCouponNum;
            @BindView(R.id.confirm_order_app_coupon_name)
            TextView appCouponName;
            @BindView(R.id.confirm_order_calculate)
            CalculateOrderView orderCalculate;
            @BindView(R.id.confirm_order_message)
            EditText messageInput;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
