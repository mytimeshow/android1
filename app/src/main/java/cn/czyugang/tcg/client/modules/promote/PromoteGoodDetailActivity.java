package cn.czyugang.tcg.client.modules.promote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.PromoterGoodDetail;
import cn.czyugang.tcg.client.entity.PromoterGoods;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

/**
 * @author ruiaa
 * @date 2017/12/6
 */

public class PromoteGoodDetailActivity extends BaseActivity {
    @BindView(R.id.promote_goods_detail_img)
    ImgView goodsImg;
    @BindView(R.id.promote_goods_detail_name)
    TextView name;
    @BindView(R.id.promote_goods_detail_name_sub)
    TextView nameSub;
    @BindView(R.id.promote_goods_detail_price)
    TextView price;
    @BindView(R.id.promote_goods_detail_profit)
    TextView profit;
    @BindView(R.id.promote_goods_detail_sale)
    TextView sales;
    @BindView(R.id.promote_goods_detail_similar_list)
    RecyclerView similarR;

    private List<PromoterGoods> similarGoodList = new ArrayList<>();
    private PromoteGoodListActivity.PromoteGoodsAdapter adapter;
    private Response<List<PromoterGoods>> goodsResponseSales;
    private RefreshLoadHelper refreshLoadHelper;

    public static void startPromoteGoodDetailActivity(String id) {
        Intent intent = new Intent(getTopActivity(), PromoteGoodDetailActivity.class);
        intent.putExtra("id", id);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_goods_detail);
        ButterKnife.bind(this);

        similarGoodList.add(new PromoterGoods());
        adapter = new PromoteGoodListActivity.PromoteGoodsAdapter(similarGoodList, this);
        similarR.setLayoutManager(new LinearLayoutManager(this));
        similarR.setAdapter(adapter);
        refreshLoadHelper = new RefreshLoadHelper(this).build(similarR);
        //refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(()->{refreshList(false);});
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(()->{refreshList(true);});

        similarR.postDelayed(() -> {
            refreshList(true);
        }, 500);
        init();

    }

    void init() {
        PromoterApi.getProduct(getIntent().getStringExtra("id")).subscribe(new NetObserver<Response<PromoterGoodDetail>>() {
            @Override
            public void onNext(Response<PromoterGoodDetail> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                PromoterGoodDetail promoterGoodDetail=new PromoterGoodDetail();
                promoterGoodDetail=response.data;
                name.setText(promoterGoodDetail.title);
                nameSub.setText(promoterGoodDetail.subTitle);
                price.setText("¥"+promoterGoodDetail.minPrice+"-"+promoterGoodDetail.maxPrice);
                sales.setText("已售"+promoterGoodDetail.sales+"份");
                profit.setText("佣金 "+promoterGoodDetail.rate+"%（预计 ￥"+promoterGoodDetail.commission+"）");
                goodsImg.id(promoterGoodDetail.picId);

            }
        });


    }

    void refreshList(boolean loadmore) {
        int pagerIndex = 1;
        String accessTime = "";
        if (loadmore && goodsResponseSales != null) {
            accessTime = goodsResponseSales.accessTime;
            pagerIndex = goodsResponseSales.currentPage + 1;
        }
        PromoterApi.getProductList(String.valueOf(pagerIndex), accessTime, "SALE").subscribe(new NetObserver<Response<List<PromoterGoods>>>() {
            @Override
            public void onNext(Response<List<PromoterGoods>> response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (ErrorHandler.isRepeat(goodsResponseSales,response)) return;

                if (!loadmore) {
                    similarGoodList.clear();
                }
                similarGoodList.addAll(response.data);
                goodsResponseSales = response;
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

    @OnClick(R.id.commit)
    public void onShareThis() {

    }
}
