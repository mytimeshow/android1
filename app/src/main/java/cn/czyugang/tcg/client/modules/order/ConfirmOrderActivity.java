package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

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
    @BindView(R.id.confirm_order_delivery)
    TextView delivery;

    @BindView(R.id.confirm_order_money)
    TextView money;
    @BindView(R.id.confirm_order_commit)
    TextView commit;


    @BindView(R.id.confirm_order_store_name)
    TextView storeName;
    @BindView(R.id.confirm_order_store_delivery)
    TextView storeDelivery;
    @BindView(R.id.confirm_order_goods)
    RecyclerView goodsR;
    @BindView(R.id.confirm_order_goods_num)
    TextView goodsNum;
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


    private String shoppingCartIds="";

    public static void startConfirmOrderActivity() {
        Intent intent = new Intent(getTopActivity(), ConfirmOrderActivity.class);
        getTopActivity().startActivity(intent);
    }

    public static void startConfirmOrderActivity(String shoppingCartIds){
        Intent intent=new Intent(getTopActivity(),ConfirmOrderActivity.class);
        intent.putExtra("shoppingCartIds",shoppingCartIds);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        shoppingCartIds=getIntent().getStringExtra("shoppingCartIds");

        orderCalculate.addItem("减", "满1减20", "-￥ 1.0");
        orderCalculate.addItem("减", "满1减20", "-￥ 2.0");
        orderCalculate.addItem("减", "满1减20", "-￥ 3.0");
        orderCalculate.addItem("配送费", "+￥ 10", v -> {
            AppUtil.toast("ggg");
        });
        orderCalculate.build();

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                addressBottom.setVisibility(scrollView.getScrollY() > 70 ? View.VISIBLE : View.GONE);
            }
        });

        StoreApi.preSettleTrolley(shoppingCartIds,null,null).subscribe(new NetObserver<OrderPreSettleResponse>() {
            @Override
            public void onNext(OrderPreSettleResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)){

                }
            }
        });
    }

    @OnClick(R.id.confirm_order_addressL)
    public void onAddress() {
        SelectAddressActivity.startSelectAddressActivity(this, REQUEST_CODE_ADDRESS);
    }

    @OnClick(R.id.confirm_order_deliveryL)
    public void onDeliver() {
        SelectDeliveryActivity.startSelectDeliveryActivity(this, REQUEST_CODE_DELIVERY);
    }

    @OnClick(R.id.confirm_order_commit)
    public void onCommit(){
        PayOrderActivity.startPayOrderActivity();
    }

    private static class SettleStoreAdapter extends RecyclerView.Adapter<SettleStoreAdapter.Holder> {
        private List<Store> list;
        private Activity activity;
        public SettleStoreAdapter(List<Store> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_settle_store,parent,false));
        }
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Store data=list.get(position);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
