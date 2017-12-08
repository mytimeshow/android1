package cn.czyugang.tcg.client.modules.promote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.widget.SpinnerSelectView;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoteGoodListActivity extends BaseActivity {
    @BindView(R.id.promote_goods_search)
    EditText search;
    @BindView(R.id.promote_goods_order_price)
    TextView orderPrice;
    @BindView(R.id.promote_goods_order_sale)
    TextView orderSale;
    @BindView(R.id.promote_goods_order_commission)
    TextView orderCommission;
    @BindView(R.id.promote_goods_list)
    RecyclerView goodsR;
    @BindView(R.id.promote_goods_orderL)
    SpinnerSelectView orderSpinner;

    private List<Good> goodList = new ArrayList<>();
    private PromoteGoodsAdapter adapter;

    public static void startPromoteGoodListActivity() {
        Intent intent = new Intent(getTopActivity(), PromoteGoodListActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_goods_list);
        ButterKnife.bind(this);

        for (int i = 0; i < 10; i++) {
            goodList.add(new Good());
        }

        adapter = new PromoteGoodsAdapter(goodList, this);
        goodsR.setLayoutManager(new LinearLayoutManager(this));
        goodsR.setAdapter(adapter);


        orderSpinner.add("综合排序", "评价最高", " 价格最低").setOnSelectItemListener(text -> {
            orderPrice.setText(text);
            orderPrice.setTextColor(ResUtil.getColor(R.color.main_red));
            orderSpinner.setVisibility(View.GONE);
            orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderCommission.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        }).build();
    }

    @OnClick(R.id.title_back)
    public void onBack() {
        finish();
    }

    private void resetOrderUI() {
        orderSpinner.resetSelect();
        orderSpinner.setVisibility(View.GONE);
        orderPrice.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderCommission.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
    }

    @OnClick(R.id.promote_goods_order_price)
    public void onOrderPrice() {
        orderSpinner.setVisibility(orderSpinner.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.promote_goods_order_sale)
    public void onOrderSale() {
        resetOrderUI();
        orderSale.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    @OnClick(R.id.promote_goods_order_commission)
    public void onOrderCommission() {
        resetOrderUI();
        orderCommission.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    public static class PromoteGoodsAdapter extends RecyclerView.Adapter<PromoteGoodsAdapter.Holder> {
        private List<Good> list;
        private Activity activity;

        public PromoteGoodsAdapter(List<Good> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_promote_goods, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Good data = list.get(position);
            holder.itemView.setOnClickListener(v -> PromoteGoodDetailActivity.startPromoteGoodDetailActivity());
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
