package cn.czyugang.tcg.client.modules.promote;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.PromoterApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.PromoterGoods;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
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

    private List<PromoterGoods> goodListComprehensive = new ArrayList<>();
    private List<PromoterGoods> goodListSales = new ArrayList<>();
    private List<PromoterGoods> goodListRate = new ArrayList<>();
    private PromoteGoodsAdapter adapter;

    private RefreshLoadHelper refreshLoadHelper;
    private Response<List<PromoterGoods>> goodsResponseComprehensive;
    private Response<List<PromoterGoods>> goodsResponseSales;
    private Response<List<PromoterGoods>> goodsResponseRate;
    private int clickType = 1;

    public static void startPromoteGoodListActivity() {
        Intent intent = new Intent(getTopActivity(), PromoteGoodListActivity.class);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_goods_list);
        ButterKnife.bind(this);

        adapter = new PromoteGoodsAdapter(goodListComprehensive, this);
        goodsR.setLayoutManager(new LinearLayoutManager(this));
        goodsR.setAdapter(adapter);
        refreshLoadHelper = new RefreshLoadHelper(this).build(goodsR);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> {
            String nowType;
            switch (clickType) {
                case 1:
                    nowType = "COMPREHENSIVE";
                    break;
                case 2:
                    nowType = "SALES";
                    break;
                default:
                    nowType = "RATE";
                    break;

            }
            refreshList(false, nowType);
        });
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> {
            String nowType;
            switch (clickType) {
                case 1:
                    nowType = "COMPREHENSIVE";
                    break;
                case 2:
                    nowType = "SALES";
                    break;
                default:
                    nowType = "RATE";
                    break;

            }
            refreshList(true, nowType);
        });

        goodsR.postDelayed(() -> {
            refreshList(true, "COMPREHENSIVE");
            refreshList(true, "SALES");
            refreshList(true, "RATE");
        }, 2000);
        orderSpinner.add("综合排序", "评价最高", " 价格最低").setOnSelectItemListener(text -> {
            orderPrice.setText(text);
            orderPrice.setTextColor(ResUtil.getColor(R.color.main_red));
            orderSpinner.setVisibility(View.GONE);
            orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderCommission.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        }).build();
    }

    void refreshList(boolean loadmore, String type) {
        int pagerIndex = 1;
        String accessTime = "";
        switch (type) {
            case "COMPREHENSIVE":
                if (loadmore && goodsResponseComprehensive != null) {
                    accessTime = goodsResponseComprehensive.accessTime;
                    pagerIndex = goodsResponseComprehensive.currentPage + 1;
                }
                break;
            case "SALES":
                if (loadmore && goodsResponseSales != null) {
                    accessTime = goodsResponseSales.accessTime;
                    pagerIndex = goodsResponseSales.currentPage + 1;
                }
                break;
            case "RATE":
                if (loadmore && goodsResponseRate != null) {
                    accessTime = goodsResponseRate.accessTime;
                    pagerIndex = goodsResponseRate.currentPage + 1;
                }
                break;
            default:
                break;
        }

        PromoterApi.getProductList(String.valueOf(pagerIndex), accessTime, type).subscribe(new NetObserver<Response<List<PromoterGoods>>>() {
            @Override
            public void onNext(Response<List<PromoterGoods>> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (response.data.isEmpty()) {
                    if (loadmore) AppUtil.toast("没有更多了");
                    return;
                }
                switch (type) {
                    case "COMPREHENSIVE":
                        if (!loadmore) {
                            goodListComprehensive.clear();
                        }
                        goodListComprehensive.addAll(response.data);
                        goodsResponseComprehensive = response;
                        break;
                    case "SALES":
                        if (!loadmore) {
                            goodListSales.clear();
                        }
                        goodListSales.addAll(response.data);
                        goodsResponseSales = response;
                        break;
                    case "RATE":
                        if (!loadmore) {
                            goodListRate.clear();
                        }
                        goodListRate.addAll(response.data);
                        goodsResponseRate = response;
                        break;
                    default:
                        break;
                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });

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
        if (clickType != 1) {
            adapter.list = goodListComprehensive;
            adapter.notifyDataSetChanged();
        }
        clickType = 1;
        orderSpinner.setVisibility(orderSpinner.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.promote_goods_order_sale)
    public void onOrderSale() {
        if (clickType != 2) {
            adapter.list = goodListSales;
            adapter.notifyDataSetChanged();
        }
        clickType = 2;
        resetOrderUI();
        orderSale.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    @OnClick(R.id.promote_goods_order_commission)
    public void onOrderCommission() {
        if (clickType != 3) {
            adapter.list = goodListRate;
            adapter.notifyDataSetChanged();
        }
        clickType = 3;
        resetOrderUI();
        orderCommission.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    public static class PromoteGoodsAdapter extends RecyclerView.Adapter<PromoteGoodsAdapter.Holder> {
        private List<PromoterGoods> list;
        private Activity activity;

        public PromoteGoodsAdapter(List<PromoterGoods> list, Activity activity) {
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
            PromoterGoods data = list.get(position);

            holder.priceDiscount.setVisibility(View.GONE);
            holder.img.id(data.picId);
            holder.title.setText(data.title);
            holder.sale.setText("已售"+data.sales+"份 | 好评率"+data.praiseRate+"%");
            if (data.minPrice==data.maxPrice){
                holder.price.setText("￥"+String.valueOf(data.minPrice));
            }else{
                holder.price.setText("￥"+String.valueOf(data.minPrice)+"起");
            }
            if (data.discountPrice!=0d){
                holder.priceDiscount.setText("￥"+String.valueOf(data.discountPrice));
                holder.price.setText("￥"+String.valueOf(data.minPrice));
                holder.priceDiscount.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                holder.priceDiscount.setVisibility(View.VISIBLE);
            }

            holder.profit.setText("赚￥"+data.commission);
            holder.itemView.setOnClickListener(v -> PromoteGoodDetailActivity.startPromoteGoodDetailActivity());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            ImgView img;
            TextView title;
            TextView sale;
            TextView price;
            TextView priceDiscount;
            TextView profit;

            public Holder(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.item_img);
                title=itemView.findViewById(R.id.item_name);
                sale=itemView.findViewById(R.id.item_sale);
                price=itemView.findViewById(R.id.item_price);
                priceDiscount =itemView.findViewById(R.id.item_price_special);
                profit=itemView.findViewById(R.id.item_profit);
            }

        }
    }
}
