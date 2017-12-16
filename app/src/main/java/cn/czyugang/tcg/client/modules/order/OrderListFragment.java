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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Order;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;
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
    private List<Order> orderList = new ArrayList<>();

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

        for (int i = 0; i < 10; i++) {
            orderList.add(new Order());
        }

        adapter = new OrderListAdapter(orderList, myOrderActivity);
        orderR.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderR.setAdapter(adapter);
        orderR.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL,
                ResUtil.getDimenInPx(R.dimen.dp_10), ResUtil.getColor(R.color.bg)).setDrawTop(true).setDrawBottom(true));

        if (type==MYORDER_TYPE_NO_PAY) {
            bottomL.setVisibility(View.VISIBLE);
            adapter.setShowSelectButton(true);
        }

        return rootView;
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

    @OnClick(R.id.order_list_delete)
    public void onDeleteOrder(){
        Iterator<Order> iterator=orderList.iterator();
        while (iterator.hasNext()){
            if (iterator.next().selected) iterator.remove();
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.order_list_pay)
    public void onPayOrder(){

    }

    static class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.Holder> {
        private List<Order> list;
        private Activity activity;
        private boolean showSelectButton=false;

        public OrderListAdapter(List<Order> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        public OrderListAdapter setShowSelectButton(boolean showSelectButton) {
            this.showSelectButton = showSelectButton;
            return this;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_order, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Order data = list.get(position);

            if (showSelectButton) holder.selectButton.setVisibility(View.VISIBLE);
            holder.selectButton.setChecked(data.selected);
            holder.selectButton.setOnClickListener(v -> {
                data.selected=!data.selected;
            });

            holder.itemView.setOnClickListener(v -> {
                OrderDetailActivity.startOrderDetailActivity(data);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
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
            @BindView(R.id.item_list_one)
            View listOne;
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
            @BindView(R.id.item_count)
            TextView count;
            @BindView(R.id.item_button_one)
            TextView oneB;
            @BindView(R.id.item_button_two)
            TextView twoB;
            @BindView(R.id.item_button_three)
            TextView threeB;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
