package cn.czyugang.tcg.client.modules.scan;

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

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.DiscountApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.QrcodeActRespose;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.common.dialog.GoodsSpecDialog;
import cn.czyugang.tcg.client.modules.common.dialog.StoreTrolleyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.BottomBalanceView;
import cn.czyugang.tcg.client.widget.GoodsPlusMinusView;
import cn.czyugang.tcg.client.widget.ProgressBgView;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
import cn.czyugang.tcg.client.widget.SpinnerSelectView;

/**
 * @author ruiaa
 * @date 2017/12/5
 */

public class ScanGoodsListActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
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
    @BindView(R.id.scan_good_orderL)
    SpinnerSelectView orderPriceSpinner;
    @BindView(R.id.goods_bottom_trolley)
    BottomBalanceView bottomBalanceView;

    private String activityId="";
    private String storeId="";
    private List<Good> goodList=new ArrayList<>();
    private ScanGoodAdapter adapter;
    private QrcodeActRespose qrcodeActRespose=null;
    private RefreshLoadHelper refreshLoadHelper;
    private String orderType="SALES";


    public static void startScanGoodsListActivity(String activityId,String storeId) {
        Intent intent = new Intent(getTopActivity(), ScanGoodsListActivity.class);
        intent.putExtra("activityId",activityId);
        intent.putExtra("storeId",storeId);
        getTopActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityId=getIntent().getStringExtra("activityId");
        storeId=getIntent().getStringExtra("storeId");

        setContentView(R.layout.activity_scan_goods_list);
        ButterKnife.bind(this);

        adapter=new ScanGoodAdapter(goodList,this);
        goodsR.setLayoutManager(new GridLayoutManager(this,1));
        goodsR.setAdapter(adapter);
        refreshLoadHelper=new RefreshLoadHelper(this).build(goodsR);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(()->getData(false));
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(()->getData(true));

        orderPriceSpinner.add("综合排序", "评价最高", "价格最低").setOnSelectItemListener(text -> {
            orderPrice.setText(text);
            orderPrice.setTextColor(ResUtil.getColor(R.color.main_red));
            orderPriceSpinner.setVisibility(View.GONE);
            orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderDistance.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            switch (text){
                case "综合排序":orderType="COMPREHENSIVE";break;
                case "评价最高":orderType="EVALUATE";break;
                case "价格最低":orderType="priceASC";break;
            }
            getData(false);
        }).build();

        initBottomTrolley();

        getData(true);
    }

    private void getData(boolean loadmore){
        int page=1;
        String accessTime=null;
        if (loadmore){
            if (qrcodeActRespose!=null){
                accessTime=qrcodeActRespose.accessTime;
                page=qrcodeActRespose.currentPage+1;
            }
        }else {
            goodList.clear();
        }
        DiscountApi.getQrcodeGoods(activityId,storeId,null,page,accessTime).subscribe(new NetObserver<QrcodeActRespose>() {
            @Override
            public void onNext(QrcodeActRespose response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (loadmore&&ErrorHandler.isRepeat(qrcodeActRespose,response)) return;
                response.parse();
                goodList.addAll(response.data);
                adapter.notifyDataSetChanged();
                qrcodeActRespose=response;

                titleText.setText(qrcodeActRespose.activityName);
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    //购物车
    public TrolleyStore trolleyStore = null;
    private StoreTrolleyDialog storeTrolleyDialog = null;
    private void initBottomTrolley() {
        trolleyStore=new TrolleyStore();
        bottomBalanceView.trolleyImg.setOnClickListener(v -> {
            if (storeTrolleyDialog == null) {
                storeTrolleyDialog = new StoreTrolleyDialog();
                storeTrolleyDialog.setTrolleyStore(trolleyStore, this);
                storeTrolleyDialog.setOnDismissRefresh(dialog -> {
                    refreshBottomTrolley();
                });
            }
            storeTrolleyDialog.show(getFragmentManager(), "StoreTrolleyDialog");
        });
    }

    public void refreshBottomTrolley() {
        if (trolleyStore == null) return;
        bottomBalanceView.setTrolleyStore(trolleyStore);
        bottomBalanceView.refresh();
    }

    public void refreshBuyNums() {
        adapter.notifyDataSetChanged();
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
        orderType="SALES";
        getData(false);
    }

    @OnClick(R.id.scan_good_order_distance)
    public void onOrderDistance() {
        resetOrderUI();
        orderDistance.setTextColor(ResUtil.getColor(R.color.main_red));
        orderType="DISTANCE";
        getData(false);
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

    private class ScanGoodAdapter extends RecyclerView.Adapter<ScanGoodAdapter.Holder> {
        private List<Good> list;
        private ScanGoodsListActivity activity;
        public boolean isSingleList = true;

        public ScanGoodAdapter(List<Good> list, ScanGoodsListActivity activity) {
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

            holder.imgView.id(data.pic);
            holder.name.setText(data.title);
            CommonUtil.setTextViewSingleLine(holder.name);
            if (holder.nameSub != null) holder.nameSub.setText(data.subTitle);
            holder.sale.setText(String.format("已抢 %d 件",data.snatch));
            if (data.buyLimit>0){
                holder.limit.setVisibility(View.VISIBLE);
                holder.limit.setText(String.format("每人限购%d件",data.buyLimit));
            }else {
                holder.limit.setVisibility(View.GONE);
            }
            holder.price.setText(data.getShowPriceStr());
            holder.progressBgView.setProgress(data.getQrcodeBuyRate()+0.64f);
            if (isSingleList){
                holder.progressBgView.setLeftColor(ResUtil.getColor(R.color.main_red));
                holder.progressBgView.setRightColor(ResUtil.getColor(R.color.main_light_red));
            }else {
                holder.progressBgView.setLeftColor(ResUtil.getColor(R.color.main_orange));
                holder.progressBgView.setRightColor(ResUtil.getColor(R.color.main_yellow));
            }


            holder.plusMinusView.setIsMultiSpec(data.isMultiSpec())
                    .setOnOpenSpecListener(() -> {
                        GoodsSpecDialog.showSpecDialog(activity, data, (trolleyGoods, num) -> {
                            activity.trolleyStore.addGood(trolleyGoods, num);
                            refreshBottomTrolley();
                            refreshBuyNums();
                        });
                    })
                    .setOnPlusMinusListener(addNum -> {      //店铺  goodsList
                        int num = activity.trolleyStore.addGood(data, addNum);
                        refreshBottomTrolley();
                        return num;
                    })
                    .setNum(activity.trolleyStore.getGoodsBuyNum(data.id));
            holder.multiSpec.setVisibility(data.isMultiSpec()?View.VISIBLE:View.INVISIBLE);
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
            ImgView imgView;
            TextView name;
            TextView nameSub;
            View multiSpec;
            ProgressBgView progressBgView;
            TextView sale;
            TextView price;
            TextView limit;
            GoodsPlusMinusView plusMinusView;
            public Holder(View itemView) {
                super(itemView);
                imgView=itemView.findViewById(R.id.item_img);
                name=itemView.findViewById(R.id.item_name);
                nameSub=itemView.findViewById(R.id.item_name_sub);
                multiSpec=itemView.findViewById(R.id.item_multi_spec);
                progressBgView=itemView.findViewById(R.id.item_progress);
                sale=itemView.findViewById(R.id.item_sale);
                price=itemView.findViewById(R.id.item_price);
                limit=itemView.findViewById(R.id.item_limit);
                plusMinusView=itemView.findViewById(R.id.item_plus);
            }
        }

    }
}
