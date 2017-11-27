package cn.czyugang.tcg.client.modules.order;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Order;

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

    private int type = -1;
    private MyOrderActivity myOrderActivity;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_list, container, false);

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

    private static class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.Holder> {
        private List<Order> list;
        private Activity activity;

        public OrderListAdapter(List<Order> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_order, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Order data = list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(itemView);
            }
        }
    }
}
