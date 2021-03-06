package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Good;
import cn.czyugang.tcg.client.entity.GoodCategory;
import cn.czyugang.tcg.client.entity.GoodsResponse;
import cn.czyugang.tcg.client.modules.common.dialog.GoodsSpecDialog;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.GoodsPlusMinusView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.RecyclerViewMaxH;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

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
    View categoryL;
    @BindView(R.id.goodslist_category_list)
    RecyclerViewMaxH categoryR;
    Unbinder unbinder;

    private StoreActivity storeActivity;
    private List<Good> goodList = new ArrayList<>();
    private GoodsAdapter adapter;
    private String currentClassifyId = null;
    private String currentOrder = null;
    private int orderByPriceType = 0;

    private CategoryAdapter categoryAdapter;
    private List<GoodCategory> categoryList = new ArrayList<>();
    private GoodsResponse goodsResponse;
    private RefreshLoadHelper refreshLoadHelper;

    public static GoodsListFragment newInstance() {
        GoodsListFragment fragment = new GoodsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeActivity = (StoreActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goodslist, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        adapter = new GoodsAdapter(goodList, getActivity());
        goodsR.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        goodsR.setAdapter(adapter);
        refreshLoadHelper = new RefreshLoadHelper(getActivity()).build(goodsR);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> refreshGoods(false));
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> refreshGoods(true));

        refreshGoods(false);

        return rootView;
    }

    private void refreshGoods(boolean loadmore) {
        int pagerIndex = 1;
        String accessTime = null;
        if (loadmore && goodsResponse != null) {
            accessTime = goodsResponse.accessTime;
            pagerIndex = goodsResponse.currentPage + 1;
        }
        StoreApi.getGoods(storeActivity.store.id, currentClassifyId, currentOrder, pagerIndex, accessTime).subscribe(new BaseActivity.NetObserver<GoodsResponse>() {
            @Override
            public void onNext(GoodsResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (loadmore && ErrorHandler.isRepeat(goodsResponse, response)) return;
                response.parse();
                if (!loadmore) goodList.clear();
                goodList.addAll(response.data);
                adapter.notifyDataSetChanged();
                if (goodsResponse == null) {
                    categoryList.add(null);
                    categoryList.addAll(response.goodCategoryList);
                    categoryAdapter = new CategoryAdapter(categoryList, getActivity());
                    categoryR.setLayoutManager(new LinearLayoutManager(getActivity()));
                    categoryR.setAdapter(categoryAdapter);
                    categoryR.setMaxHeightRes(R.dimen.dp_280);
                }
                goodsResponse = response;
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    private void refreshGoods() {
        refreshGoods(false);
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

    public void refreshBuyNums() {
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.goodslist_order_show)
    public void onShowType() {
        adapter.isSingleList = !adapter.isSingleList;
        if (adapter.isSingleList) {
            showType.setImageResource(R.drawable.icon_list_two);
            ((GridLayoutManager) goodsR.getLayoutManager()).setSpanCount(1);
            adapter.notifyDataSetChanged();
        } else {
            showType.setImageResource(R.drawable.icon_list_one);
            ((GridLayoutManager) goodsR.getLayoutManager()).setSpanCount(2);
            adapter.notifyDataSetChanged();
        }
    }

    //分类
    @OnClick(R.id.goodslist_category_open)
    public void onOpenCategory() {
        categoryL.setVisibility(categoryL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    //排序
    @OnClick(R.id.goodslist_order_default)
    public void onOrderDefault() {
        currentOrder = null;
        refreshGoods();
        initOrderUi();
        orderDefault.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    @OnClick(R.id.goodslist_order_news)
    public void onOrderNews() {
        currentOrder = "new";
        refreshGoods();
        initOrderUi();
        orderNews.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    @OnClick(R.id.goodslist_order_sale)
    public void onOrderSale() {
        currentOrder = "productSales";
        refreshGoods();
        initOrderUi();
        orderSale.setTextColor(ResUtil.getColor(R.color.main_red));
    }

    @OnClick(R.id.goodslist_order_price)
    public void onOrderPrice() {
        if (orderByPriceType == 0) {
            currentOrder = "priceASC";
            refreshGoods();
            orderByPriceType = 1;
        } else if (orderByPriceType > 0) {
            currentOrder = "priceDESC";
            refreshGoods();
            orderByPriceType = -1;
        } else {
            currentOrder = "priceASC";
            refreshGoods();
            orderByPriceType = 1;
        }
        initOrderUi();
        orderPrice.setTextColor(ResUtil.getColor(R.color.main_red));
        orderPriceImg.setImageResource(orderByPriceType > 0 ?
                R.drawable.ic_price_order_red_grey : R.drawable.ic_price_order_grey_red);
    }

    private void initOrderUi() {
        orderDefault.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderNews.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderPrice.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        orderPriceImg.setImageResource(R.drawable.ic_price_order_grey);
    }

    private class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.Holder> {
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

            if (!data.isSaleAble()) {
                holder.imgView.setDisCount("暂停售卖");
                holder.plusMinusView.setClickable(false);
            } else {
                holder.plusMinusView.setClickable(true);
            }

            holder.imgView.id(data.pic);
            holder.name.setText(data.title);
            CommonUtil.setTextViewSingleLine(holder.name);
            if (holder.nameSub != null) holder.nameSub.setText(data.subTitle);
            holder.sale.setText(String.format("已售%d份  评价%d", data.sales, (int) data.score));//已售11份  评价22
            holder.price.setText(data.getShowPriceStr());
            holder.tag.setText(data.getTag());

            holder.itemView.setOnClickListener(v -> GoodDetailActivity.startGoodDetailActivity(data.id, storeActivity.id));
            holder.itemView.setOnLongClickListener(v -> {
                MyDialog.collectionBg(activity, v, false, myDialog -> {
                    myDialog.dismiss();
                });
                return true;
            });

            holder.plusMinusView.setIsMultiSpec(data.isMultiSpec())
                    .setOnOpenSpecListener(() -> {
                        GoodsSpecDialog.showSpecDialog(storeActivity, data, (trolleyGoods, num) -> {
                            storeActivity.trolleyStore.addGood(trolleyGoods, num);
                            storeActivity.refreshBottomTrolley();
                            refreshBuyNums();
                        });
                    })
                    .setOnPlusMinusListener(addNum -> {      //店铺  goodsList
                        int num = storeActivity.trolleyStore.addGood(data, addNum);
                        storeActivity.refreshBottomTrolley();
                        return num;
                    })
                    .setNum(storeActivity.trolleyStore.getGoodsBuyNum(data.id));
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
            TextView tag;
            TextView price;
            GoodsPlusMinusView plusMinusView;

            public Holder(View itemView) {
                super(itemView);
                imgView = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                nameSub = itemView.findViewById(R.id.item_name_sub);
                sale = itemView.findViewById(R.id.item_sale);
                tag = itemView.findViewById(R.id.item_tag);
                price = itemView.findViewById(R.id.item_price);
                plusMinusView = itemView.findViewById(R.id.item_plus);
            }
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {
        private List<GoodCategory> list;
        private Activity activity;
        private TextView currentSelectCategory = null;

        public CategoryAdapter(List<GoodCategory> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_goods_list_category, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            GoodCategory data = list.get(position);
            if (data == null) {
                holder.name.setText("全部分类");
                if (currentSelectCategory == null) {
                    currentSelectCategory = holder.name;
                    currentSelectCategory.setTextColor(ResUtil.getColor(R.color.main_red));
                }
            } else {
                holder.name.setText(data.name);
                holder.labelLayout.setTexts(data.getSeconds());
            }
            holder.name.setOnClickListener(v -> {
                currentSelectCategory.setTextColor(ResUtil.getColor(R.color.text_black));
                currentSelectCategory = holder.name;
                currentSelectCategory.setTextColor(ResUtil.getColor(R.color.main_red));
                if (data == null) {
                    currentClassifyId = null;
                    refreshGoods();
                } else {
                    currentClassifyId = data.id;
                    refreshGoods();
                }
                categoryL.setVisibility(View.GONE);
            });
            holder.labelLayout.setOnClickItemListener((text, textView) -> {
                currentSelectCategory.setTextColor(ResUtil.getColor(R.color.text_black));
                currentSelectCategory = textView;
                currentSelectCategory.setTextColor(ResUtil.getColor(R.color.main_red));
                currentClassifyId = data.getSecondCategoryId(text);
                refreshGoods();
                categoryL.setVisibility(View.GONE);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView name;
            LabelLayout labelLayout;

            public Holder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                labelLayout = itemView.findViewById(R.id.item_labels);
            }
        }
    }
}
