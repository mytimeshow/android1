package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.OrderApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Order;
import cn.czyugang.tcg.client.entity.OrderGoods;
import cn.czyugang.tcg.client.entity.OrderResponse;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.utils.rxbus.DeleteOrderEvent;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/11/23
 */

public class OrderListFragment extends BaseFragment {
    public static final int MYORDER_TYPE_ALL = 1;
    public static final int MYORDER_TYPE_NO_PAY = 2;
    public static final int MYORDER_TYPE_NO_SEND = 3;
    public static final int MYORDER_TYPE_NO_RECEIVED = 4;
    public static final int MYORDER_TYPE_NO_COMMENT = 5;
    @BindView(R.id.order_list)
    RecyclerView orderR;
    Unbinder unbinder;
    @BindView(R.id.order_list_bottom)
    LinearLayout bottomL;

    private int type = -1;
    private MyOrderActivity myOrderActivity;
    private OrderListAdapter adapter;
    private OrderResponse orderResponse = null;
    private RefreshLoadHelper refreshLoadHelper;

    public static OrderListFragment newInstance(int type) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
        myOrderActivity = (MyOrderActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        adapter = new OrderListAdapter(myOrderActivity);
        orderR.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderR.setAdapter(adapter);
        orderR.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL,
                ResUtil.getDimenInPx(R.dimen.dp_10), ResUtil.getColor(R.color.bg)).setDrawTop(true).setDrawBottom(true));

        if (type == MYORDER_TYPE_NO_PAY) {
            bottomL.setVisibility(View.VISIBLE);
            adapter.setShowSelectButton(true);
        }

        refreshLoadHelper=new RefreshLoadHelper(getActivity()).build(orderR);
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(()->getOrders(true));
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(()->getOrders(false));

        registerOnDeleteOrder();
        getOrders(false);

        return rootView;
    }

    private void getOrders(boolean loadMore) {
        //待付款：WAIT_PAY，待发货：ORDERS，待收货：DELIVERY，待评价：COMMENT
        String status = null;
        switch (type) {
            case MYORDER_TYPE_ALL:
                status = null;
                break;
            case MYORDER_TYPE_NO_PAY:
                status = "WAIT_PAY";
                break;
            case MYORDER_TYPE_NO_SEND:
                status = "ORDERS";
                break;
            case MYORDER_TYPE_NO_RECEIVED:
                status = "DELIVERY";
                break;
            case MYORDER_TYPE_NO_COMMENT:
                status = "COMMENT";
                break;
        }
        int page = 0;
        String accessTime = null;
        if (loadMore && orderResponse != null) {
            page = orderResponse.currentPage + 1;
            accessTime = orderResponse.accessTime;
        }
        OrderApi.getAllOrder(page, accessTime, status).subscribe(new BaseActivity.NetObserver<OrderResponse>() {
            @Override
            public void onNext(OrderResponse response) {
                super.onNext(response);
                response.parse();
                if (loadMore && orderResponse != null) {
                    orderResponse.merge(response);
                } else {
                    orderResponse = response;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    @Override
    public String getLabel() {
        if (type < 0) type = getArguments().getInt("type", 1);
        switch (type) {
            case MYORDER_TYPE_NO_PAY: {
                return "待付款";
            }
            case MYORDER_TYPE_NO_SEND: {
                return "待发货";
            }
            case MYORDER_TYPE_NO_RECEIVED: {
                return "待收货";
            }
            case MYORDER_TYPE_NO_COMMENT: {
                return "待评论";
            }
            default: {
                return "全部";
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
    *   删除订单
    * */
    @OnClick(R.id.order_list_delete)
    public void onDeleteOrders() {
        if (orderResponse == null) return;
        ArrayList<String> deleteOrderIds = new ArrayList<>();
        Iterator<Order> iterator = orderResponse.data.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.selected) {
                deleteOrderIds.add(order.id);
                iterator.remove();
            }
        }
        adapter.notifyDataSetChanged();
        if (!deleteOrderIds.isEmpty()) {
            OrderApi.deleteOrder(deleteOrderIds).subscribe(new BaseActivity.NetObserver<Response<Object>>() {
                @Override
                public void onNext(Response<Object> response) {
                    super.onNext(response);
                    if (ErrorHandler.judge200(response)) {
                        LogRui.i("onNext####", response.message);
                    }
                }

                @Override
                public boolean showLoading() {
                    return false;
                }
            });
        }
    }

    private void deleteOrder(Order order) {
        if (order == null) return;
        orderResponse.data.remove(order);
        adapter.notifyDataSetChanged();

        List<String> list = new ArrayList<>();
        list.add(order.id);
        postDeleteOrderEvent(list);
        OrderApi.deleteOrder(list).subscribe(new BaseActivity.NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    LogRui.i("onNext####", response.message);
                }
            }

            @Override
            public boolean showLoading() {
                return false;
            }
        });
    }

    private void postDeleteOrderEvent(List<String> orderIds) {
        RxBus.post(new DeleteOrderEvent(orderIds));
    }

    private void registerOnDeleteOrder() {
        mCompositeDisposable.add(RxBus.toObservable(DeleteOrderEvent.class).subscribe(deleteOrderEvent -> {
            removeOrderInResponse(deleteOrderEvent.orderIds);
        }));
    }

    private void removeOrderInResponse(List<String> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) return;
        if (orderResponse == null) return;
        Iterator<Order> iterator = orderResponse.data.iterator();
        boolean hadChange = false;
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (orderIds.contains(order.id)) {
                iterator.remove();
                hadChange = true;
            }
        }
        if (adapter != null && hadChange) adapter.notifyDataSetChanged();
    }

    /*
    *   再次购买
    * */
    private void buyAgain(Order order) {

    }

    /*
    *   追加评价
    * */
    private void moreComment(Order order) {

    }

    /*
    *   评价
    * */
    private void comment(Order order) {

    }

    /*
    *   取消订单
    * */
    private void cancel(Order order) {
        OrderApi.cancelOrder(order.id).subscribe(new BaseActivity.NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
            }

            @Override
            public boolean showLoading() {
                return false;
            }
        });
    }


    /*
    *   去付款
    * */
    private void toPay(Order order) {

    }

    /*
    *   确认收货
    * */
    private void receipt(Order order) {
        OrderApi.reach(order.id).subscribe(new BaseActivity.NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
            }

            @Override
            public boolean showLoading() {
                return false;
            }
        });
    }


    @OnClick(R.id.order_list_pay)
    public void onPayOrder() {

    }

    class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.Holder> {
        private Activity activity;
        private boolean showSelectButton = false;

        public OrderListAdapter(Activity activity) {
            this.activity = activity;
        }

        public OrderListAdapter setShowSelectButton(boolean showSelectButton) {
            this.showSelectButton = showSelectButton;
            return this;
        }

        @Override
        public int getItemCount() {
            return orderResponse == null ? 0 : orderResponse.data.size();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_order, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Order data = orderResponse.data.get(position);

            //选择
            if (showSelectButton) holder.selectButton.setVisibility(View.VISIBLE);
            holder.selectButton.setChecked(data.selected);
            holder.selectButton.setOnClickListener(v -> {
                data.selected = !data.selected;
            });

            //店铺商品
            holder.store.setText(data.storeName);
            holder.status.setText(orderResponse.getStatusStr(data.status));
            if (data.goodsList.size() == 1) {
                holder.list.setVisibility(View.GONE);
                holder.listOne.setVisibility(View.VISIBLE);
                OrderGoods orderGoods = data.goodsList.get(0);
                holder.img.id(orderGoods.picId);
                holder.goodsName.setText(orderGoods.title);
                holder.spec.setText(orderGoods.getSpec());
                holder.num.setText("x" + orderGoods.number);
            } else {
                holder.list.setVisibility(View.VISIBLE);
                holder.listOne.setVisibility(View.GONE);
                data.bindImgAdapter(holder.list, activity);
            }

            //总计
            holder.count.setText(data.getTotalCal());

            setBottomButton(holder, data);

            holder.itemView.setOnClickListener(v -> {
                OrderDetailActivity.startOrderDetailActivity(data.id);
            });
        }

        private void setBottomButton(Holder holder, Order data) {
            holder.cancel.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.comment.setVisibility(View.GONE);
            holder.commentMore.setVisibility(View.GONE);
            holder.pay.setVisibility(View.GONE);
            holder.buyAgain.setVisibility(View.GONE);
            holder.receipt.setVisibility(View.GONE);

            holder.cancel.setOnClickListener(v -> {
                cancel(data);
            });
            holder.delete.setOnClickListener(v -> {
                deleteOrder(data);
            });
            holder.comment.setOnClickListener(v -> {
                comment(data);
            });
            holder.commentMore.setOnClickListener(v -> {
                moreComment(data);
            });
            holder.receipt.setOnClickListener(v -> {
                receipt(data);
            });
            holder.buyAgain.setOnClickListener(v -> {
                buyAgain(data);
            });
            holder.pay.setOnClickListener(v -> {
                toPay(data);
            });

            //WAIT_PAY:待付款。PAY:已付款。ORDERS已接单。DELIVERY:已发货。REACH:已送达。FINISH:已完成
            switch (data.status) {
                case "WAIT_PAY": {
                    //待付款  取消订单 | 删除订单| 去付款
                    holder.cancel.setVisibility(View.VISIBLE);
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.pay.setVisibility(View.VISIBLE);
                    break;
                }
                case "PAY":
                case "ORDERS": {
                    //待发货   取消订单 | 再次购买
                    holder.cancel.setVisibility(View.VISIBLE);
                    holder.buyAgain.setVisibility(View.VISIBLE);
                    break;
                }
                case "DELIVERY": {
                    //待收货   再次购买
                    holder.buyAgain.setVisibility(View.VISIBLE);
                    break;
                }
                case "REACH": {
                    //待收货   确认收货 | 再次购买
                    holder.receipt.setVisibility(View.VISIBLE);
                    holder.buyAgain.setVisibility(View.VISIBLE);
                    break;
                }
                case "FINISH": {
                    //待评价    删除订单 | 评价 | 再次购买
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.comment.setVisibility(View.VISIBLE);
                    holder.buyAgain.setVisibility(View.VISIBLE);
                    break;
                }
                default: {
                    holder.delete.setVisibility(View.VISIBLE);
                }
            }
        }

        class Holder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_select)
            SelectButton selectButton;
            @BindView(R.id.item_store_name)
            TextView store;
            @BindView(R.id.item_status)
            TextView status;
            @BindView(R.id.item_list)
            RecyclerView list;
            /*
            *
            * */
            @BindView(R.id.item_list_one)
            View listOne;
            @BindView(R.id.item_img)
            ImgView img;
            @BindView(R.id.item_name)
            TextView goodsName;
            @BindView(R.id.item_spec)
            TextView spec;
            @BindView(R.id.item_tag)
            TextView tags;
            @BindView(R.id.item_price)
            TextView price;
            @BindView(R.id.item_price_origin)
            TextView priceOrigin;
            @BindView(R.id.item_num)
            TextView num;
            /*
            *
            * */
            @BindView(R.id.item_count)
            TextView count;

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


            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
