package cn.czyugang.tcg.client.modules.promote;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.PromoteOrder;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoterProfitOrderFragment extends BaseFragment {
    @BindView(R.id.promote_profit_order_list)
    RecyclerView orderR;
    Unbinder unbinder;

    private String type=null;
    private List<PromoteOrder> orderList=new ArrayList<>();
    private ProfitOrderAdapter adapter;

    //所有订单 邀请注册  推广商品  失效订单
    public static PromoterProfitOrderFragment newInstance(String type) {
        PromoterProfitOrderFragment fragment = new PromoterProfitOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type==null) getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_promoter_profit_order, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        for(int i=0;i<10;i++){
            orderList.add(new PromoteOrder());
        }

        adapter=new ProfitOrderAdapter(orderList,getActivity());
        orderR.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderR.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getLabel() {
        if (type==null) type=getArguments().getString("type");
        return type;
    }

    private static class ProfitOrderAdapter extends RecyclerView.Adapter<ProfitOrderAdapter.Holder> {
        private List<PromoteOrder> list;
        private Activity activity;
        public ProfitOrderAdapter(List<PromoteOrder> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_promote_profit_order,parent,false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            PromoteOrder data=list.get(position);
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
