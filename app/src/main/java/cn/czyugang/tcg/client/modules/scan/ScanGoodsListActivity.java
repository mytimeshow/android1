package cn.czyugang.tcg.client.modules.scan;

import android.app.Activity;
import android.content.Intent;
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
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.widget.SpinnerSelectView;

/**
 * @author ruiaa
 * @date 2017/12/5
 */

public class ScanGoodsListActivity extends BaseActivity {
    @BindView(R.id.scan_good_order_price)
    TextView orderPrice;
    @BindView(R.id.scan_good_order_sale)
    TextView orderSale;
    @BindView(R.id.scan_good_order_distance)
    TextView orderDistance;
    @BindView(R.id.scan_good_show_type)
    ImageView showType;
    @BindView(R.id.scan_goods_list)
    RecyclerView goodsR;
    @BindView(R.id.goods_money)
    TextView goodsMoney;
    @BindView(R.id.scan_good_orderL)
    SpinnerSelectView orderPriceSpinner;

    private List<Good> goodList=new ArrayList<>();
    private ScanGoodAdapter adapter;

    public static void startScanGoodsListActivity() {
        Intent intent = new Intent(getTopActivity(), ScanGoodsListActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_goods_list);
        ButterKnife.bind(this);

        for(int i=0;i<20;i++){
            goodList.add(new Good());
        }

        adapter=new ScanGoodAdapter(goodList,this);
        goodsR.setLayoutManager(new GridLayoutManager(this,1));
        goodsR.setAdapter(adapter);

        orderPriceSpinner.add("综合排序", "评价最高", " 价格最低").setOnSelectItemListener(text -> {
            orderPrice.setText(text);
            orderPrice.setTextColor(ResUtil.getColor(R.color.main_red));
            orderPriceSpinner.setVisibility(View.GONE);
            orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderDistance.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        }).build();
    }

    @OnClick(R.id.title_back)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.title_right)
    public void onScan(){
        ScanActivity.startScanActivity();
    }

    private void resetOrderUI() {
        orderPriceSpinner.resetSelect();
        orderPriceSpinner.setVisibility(View.GONE);
        orderPrice.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderDistance.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
    }

    @OnClick(R.id.scan_good_order_price)
    public void onOrderPrice() {
        orderPriceSpinner.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.scan_good_order_sale)
    public void onOrderSale() {
        resetOrderUI();
        orderSale.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    @OnClick(R.id.scan_good_order_distance)
    public void onOrderDistance() {
        resetOrderUI();
        orderDistance.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    @OnClick(R.id.scan_good_show_type)
    public void onShowType(){
        adapter.isSingleList = !adapter.isSingleList;
        if (adapter.isSingleList) {
            goodsR.setPadding(0,0,0,0);
            showType.setImageResource(R.drawable.icon_list_two);
            ((GridLayoutManager) goodsR.getLayoutManager()).setSpanCount(1);
            adapter.notifyDataSetChanged();
        } else {
            goodsR.setPadding(0,0,ResUtil.getDimenInPx(R.dimen.dp_10),0);
            showType.setImageResource(R.drawable.icon_list_one);
            ((GridLayoutManager) goodsR.getLayoutManager()).setSpanCount(2);
            adapter.notifyDataSetChanged();
        }
    }

    private static class ScanGoodAdapter extends RecyclerView.Adapter<ScanGoodAdapter.Holder> {
        private List<Good> list;
        private Activity activity;
        public boolean isSingleList = true;

        public ScanGoodAdapter(List<Good> list, Activity activity) {
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
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return isSingleList ? R.layout.item_scan_good_one : R.layout.item_scan_good_two;
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
