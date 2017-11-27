package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.utils.img.ImgView;

/**
 * @author ruiaa
 * @date 2017/11/22
 */

public class GoodsListFragment extends BaseFragment {
    @BindView(R.id.goodslist_category_open)
    TextView categoryOpen;
    @BindView(R.id.goodslist_order_default)
    TextView orderDefault;
    @BindView(R.id.goodslist_order_news)
    TextView orderNews;
    @BindView(R.id.goodslist_order_sale)
    TextView orderSale;
    @BindView(R.id.goodslist_order_price)
    TextView orderPrice;
    @BindView(R.id.goodslist_order_price_img)
    ImageView orderPriceImg;
    @BindView(R.id.goodslist_order_show)
    ImageView showType;
    @BindView(R.id.goodslist_foods)
    RecyclerView goodsR;
    @BindView(R.id.goodslist_category_select)
    View categorySelect;
    Unbinder unbinder;

    private List<Good> goodList = new ArrayList<>();
    private GoodsAdapter adapter;

    public static GoodsListFragment newInstance() {
        GoodsListFragment fragment = new GoodsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodslist, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        for(int i=0;i<30;i++){
            goodList.add(new Good());
        }

        adapter=new GoodsAdapter(goodList,getActivity());
        goodsR.setLayoutManager(new GridLayoutManager(getActivity(),2));
        goodsR.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getLabel() {
        return "商品";
    }

    @OnClick(R.id.goodslist_order_show)
    public void onShowType() {
        adapter.isSingleList = !adapter.isSingleList;
        if (adapter.isSingleList) {
            showType.setImageResource(R.drawable.icon_list_two);
            ((GridLayoutManager)goodsR.getLayoutManager()).setSpanCount(1);
            adapter.notifyDataSetChanged();
        } else {
            showType.setImageResource(R.drawable.icon_list_one);
            ((GridLayoutManager)goodsR.getLayoutManager()).setSpanCount(2);
            adapter.notifyDataSetChanged();
        }
    }

    private static class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.Holder> {
        private List<Good> list;
        private Activity activity;
        public boolean isSingleList = false;

        public GoodsAdapter(List<Good> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Good data = list.get(position);

            holder.price.setText(String.format("￥%.2f",Math.random()*10));
            holder.imgView.id("919910512769269760");

            holder.itemView.setOnClickListener(v -> GoodDetailActivity.startGoodDetailActivity());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return isSingleList ? R.layout.item_store_goods_one : R.layout.item_store_goods_two;
        }

        class Holder extends RecyclerView.ViewHolder {
            ImgView imgView;
            TextView name;
            TextView nameSub;
            TextView sale;
            TextView price;
            TextView buyNum;
            View plus;
            View minus;
            public Holder(View itemView) {
                super(itemView);
                imgView = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                nameSub = itemView.findViewById(R.id.item_name_sub);
                sale = itemView.findViewById(R.id.item_sale);
                price = itemView.findViewById(R.id.item_price);
                buyNum = itemView.findViewById(R.id.item_num);
                plus = itemView.findViewById(R.id.item_plus);
                minus = itemView.findViewById(R.id.item_minus);
            }
        }
    }
}
