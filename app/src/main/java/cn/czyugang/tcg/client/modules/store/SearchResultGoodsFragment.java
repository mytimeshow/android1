package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;
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
import cn.czyugang.tcg.client.entity.SearchGood;
import cn.czyugang.tcg.client.entity.SearchGoodsResponse;
import cn.czyugang.tcg.client.utils.AmapLocationUtil;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

/**
 * @author ruiaa
 * @date 2017/12/1
 */

public class SearchResultGoodsFragment extends BaseFragment {

    @BindView(R.id.search_type)
    TextView type;
    @BindView(R.id.search_order_price)
    TextView orderPrice;
    @BindView(R.id.search_order_sale)
    TextView orderSale;
    @BindView(R.id.search_show_type)
    ImageView showType;
    @BindView(R.id.search_type_all_text)
    TextView typeAllText;
    @BindView(R.id.search_type_all_select)
    ImageView typeAllSelect;
    @BindView(R.id.search_type_food_text)
    TextView typeFoodText;
    @BindView(R.id.search_type_food_select)
    ImageView typeFoodSelect;
    @BindView(R.id.search_type_goods_text)
    TextView typeGoodsText;
    @BindView(R.id.search_type_goods_select)
    ImageView typeGoodsSelect;
    @BindView(R.id.search_typeL)
    LinearLayout typeL;
    @BindView(R.id.search_order_distance_text)
    TextView orderDistanceText;
    @BindView(R.id.search_order_distance_select)
    ImageView orderDistanceSelect;
    @BindView(R.id.search_order_praise_text)
    TextView orderPraiseText;
    @BindView(R.id.search_order_praise_select)
    ImageView orderPraiseSelect;
    @BindView(R.id.search_order_price_up_text)
    TextView orderPriceUpText;
    @BindView(R.id.search_order_price_up_select)
    ImageView orderPriceUpSelect;
    @BindView(R.id.search_order_price_down_text)
    TextView orderPriceDownText;
    @BindView(R.id.search_order_price_down_select)
    ImageView orderPriceDownSelect;
    @BindView(R.id.search_orderL)
    LinearLayout orderL;
    @BindView(R.id.filter_price_min)
    EditText filterPriceMin;
    @BindView(R.id.filter_price_max)
    EditText filterPriceMax;
    @BindView(R.id.filter_label_list)
    RecyclerView filterLabelR;
    @BindView(R.id.filter_reset)
    TextView filterReset;
    @BindView(R.id.filter_confirm)
    TextView filterConfirm;
    @BindView(R.id.search_filterL)
    FrameLayout filterL;
    @BindView(R.id.search_results)
    RecyclerView resultsR;
    Unbinder unbinder;


    private List<Item> filterList = new ArrayList<>();
    private FilterAdapter filterAdapter;
    private RecyclerView.Adapter adapter = null;
    private List<SearchGood> goodList = new ArrayList<>();
    private SearchGoodsResponse searchResponse = null;
    private RefreshLoadHelper refreshLoadHelper;

    private String resultType = null;   //TAKEOUT-外卖; MARKET-商超;不传为全部
    private String orderType = null;    //LOCATION-距离最近   GOOD_RATE-好评   PRICE_DESC-价格从低到高   PRICE_ASC-价格从高到低  SALE- 销量  不传按发布时间
    private String searchKey = null;

    public static SearchResultGoodsFragment newInstance() {
        SearchResultGoodsFragment fragment = new SearchResultGoodsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        filterList.add(new Item());
        filterList.add(new Item());
        filterList.add(new Item());
        filterList.add(new Item());
        filterList.add(new Item());

        filterAdapter = new FilterAdapter(filterList, getActivity());
        filterLabelR.setLayoutManager(new LinearLayoutManager(getActivity()));
        filterLabelR.setAdapter(filterAdapter);
        filterLabelR.addItemDecoration(new RecycleViewDivider(getActivity()));
        filterLabelR.setNestedScrollingEnabled(false);

        goodList.add(null);

        adapter = new GoodsAdapter(goodList, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (((GoodsAdapter) adapter).isSingleList) return 1;
                return position == 0 ? 2 : 1;
            }
        });
        resultsR.setLayoutManager(gridLayoutManager);
        resultsR.setAdapter(adapter);

        refreshLoadHelper = new RefreshLoadHelper(getActivity()).build(resultsR);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> refreshResult(false));
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> refreshResult(true));
        initRefresh();

        return rootView;
    }

    @Override
    public String getLabel() {
        return "商品";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
    *   数据获取
    * */
    private void refreshResult() {
        if (adapter == null) return;
        refreshResult(false);
    }

    private void refreshResult(boolean loadmore) {
        HashMap<String, Object> map = new HashMap<>();
        if (loadmore && searchResponse != null) {
            map.put("page", searchResponse.currentPage + 1);
        } else {
            map.put("page", 1);
        }
        map.put("pageSize", 10);
        map.put("lat", AmapLocationUtil.getLatitude());
        map.put("lon", AmapLocationUtil.getLongitude());
        map.put("title", searchKey);
        if (orderType != null) map.put("orderType", orderType);
        if (resultType != null) map.put("searchType", resultType);
        StoreApi.searchGoods(map).subscribe(new BaseActivity.NetObserver<SearchGoodsResponse>() {
            @Override
            public void onNext(SearchGoodsResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    if (response.data.isEmpty()) {
                        if (loadmore) AppUtil.toast("没有更多了");
                        return;
                    }
                    response.parse();
                    if (!loadmore) {
                        goodList.clear();
                        goodList.add(null);
                        goodList.addAll(response.data);
                        response.currentPage = 1;
                    } else {
                        goodList.addAll(response.data);
                        response.currentPage = searchResponse.currentPage + 1;
                    }
                    adapter.notifyDataSetChanged();
                    searchResponse = response;
                }
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    private void initRefresh() {
        if (resultType == null) {
            onTypeAll();
        } else if (resultType.equals("TAKEOUT")) {
            onTypeFood();
        } else if (resultType.equals("MARKET")) {
            onTypeGoods();
        }
    }

    public void setSearchKey(String key, int searchType) {
        searchKey = key;
        switch (searchType) {
            case SearchResultActivity.SEARCH_TYPE_ALL: {
                resultType = null;
                break;
            }
            case SearchResultActivity.SEARCH_TYPE_FOOD: {
                resultType = "TAKEOUT";
                break;
            }
            case SearchResultActivity.SEARCH_TYPE_GOODS: {
                resultType = "MARKET";
                break;
            }
        }
    }

    public void setSearchKey(String key) {
        searchKey = key;
        refreshResult();
    }


    /*
    *   全部 外卖 商超
    *
    *   TAKEOUT-外卖; MARKET-商超;不传为全部
    * */
    @OnClick(R.id.search_type)
    public void onOpenType() {
        typeL.setVisibility(typeL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        orderL.setVisibility(View.GONE);
        filterL.setVisibility(View.GONE);
    }

    private void setTypeUI(TextView selectType, ImageView selectHook) {
        typeAllText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        typeFoodText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        typeGoodsText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        typeAllSelect.setImageDrawable(null);
        typeFoodSelect.setImageDrawable(null);
        typeGoodsSelect.setImageDrawable(null);

        type.setText(selectType.getText());
        selectType.setTextColor(ResUtil.getColor(R.color.main_red));
        selectHook.setImageResource(R.drawable.icon_selected_hook);
    }

    @OnClick(R.id.search_type_all)
    public void onTypeAll() {
        setTypeUI(typeAllText, typeAllSelect);
        typeL.setVisibility(View.GONE);

        resultType = null;
        refreshResult();
    }

    @OnClick(R.id.search_type_food)
    public void onTypeFood() {
        setTypeUI(typeFoodText, typeFoodSelect);
        typeL.setVisibility(View.GONE);

        resultType = "TAKEOUT";
        refreshResult();
    }

    @OnClick(R.id.search_type_goods)
    public void onTypeGoods() {
        setTypeUI(typeGoodsText, typeGoodsSelect);
        typeL.setVisibility(View.GONE);

        resultType = "MARKET";
        refreshResult();
    }


    /*
    *  好评,价格从低至高,价格从高至低,距离最近,销量
    *  LOCATION-距离最近   GOOD_RATE-好评   PRICE_DESC-价格从低到高   PRICE_ASC-价格从高到低  SALE- 销量
    * */
    @OnClick(R.id.search_order_price)
    public void onOpenPrice() {
        typeL.setVisibility(View.GONE);
        orderL.setVisibility(orderL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        filterL.setVisibility(View.GONE);
    }

    private void setOrderUI(boolean selectSale, TextView selectPrice, ImageView selectHook) {
        if (selectSale) {
            orderPrice.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderPriceDownSelect.setImageDrawable(null);
            orderPriceUpSelect.setImageDrawable(null);
            orderPraiseSelect.setImageDrawable(null);
            orderDistanceSelect.setImageDrawable(null);
            orderPriceDownText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderPriceUpText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderDistanceText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderPraiseText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderSale.setTextColor(ResUtil.getColor(R.color.main_red));
        } else {
            if (selectPrice == null) return;
            orderPrice.setTextColor(ResUtil.getColor(R.color.main_red));
            orderPrice.setText(selectPrice.getText());
            orderPriceDownSelect.setImageDrawable(null);
            orderPriceUpSelect.setImageDrawable(null);
            orderPraiseSelect.setImageDrawable(null);
            orderDistanceSelect.setImageDrawable(null);
            orderPriceDownText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderPriceUpText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderDistanceText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderPraiseText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            selectPrice.setTextColor(ResUtil.getColor(R.color.main_red));
            if (selectHook != null) selectHook.setImageResource(R.drawable.icon_selected_hook);
            orderSale.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
        }
    }

    @OnClick(R.id.search_order_sale)
    public void onSelectSale() {
        setOrderUI(true, null, null);
        typeL.setVisibility(View.GONE);
        orderL.setVisibility(View.GONE);
        filterL.setVisibility(View.GONE);

        orderType = "SALE";
        refreshResult();
    }

    @OnClick(R.id.search_order_price_up)
    public void onOrderPriceUp() {
        setOrderUI(false, orderPriceUpText, orderPriceUpSelect);
        orderL.setVisibility(View.GONE);

        orderType = "PRICE_DESC";
        refreshResult();
    }

    @OnClick(R.id.search_order_price_down)
    public void onOrderPriceDown() {
        setOrderUI(false, orderPriceDownText, orderPriceDownSelect);
        orderL.setVisibility(View.GONE);

        orderType = "PRICE_ASC";
        refreshResult();
    }

    @OnClick(R.id.search_order_praise)
    public void onOrderPraise() {
        setOrderUI(false, orderPraiseText, orderPraiseSelect);
        orderL.setVisibility(View.GONE);

        orderType = "GOOD_RATE";
        refreshResult();
    }

    @OnClick(R.id.search_order_distance)
    public void onOrderDistance() {
        setOrderUI(false, orderDistanceText, orderDistanceSelect);
        orderL.setVisibility(View.GONE);

        orderType = "LOCATION";
        refreshResult();
    }

    /*
    *  单列，双列
    * */
    @OnClick(R.id.search_show_type)
    public void onShowType() {
        if (!(adapter instanceof GoodsAdapter)) return;
        GoodsAdapter goodsAdapter = (GoodsAdapter) adapter;
        goodsAdapter.isSingleList = !goodsAdapter.isSingleList;
        if (goodsAdapter.isSingleList) {
            showType.setImageResource(R.drawable.icon_list_two);
            ((GridLayoutManager) resultsR.getLayoutManager()).setSpanCount(1);
            goodsAdapter.notifyDataSetChanged();
        } else {
            showType.setImageResource(R.drawable.icon_list_one);
            ((GridLayoutManager) resultsR.getLayoutManager()).setSpanCount(2);
            goodsAdapter.notifyDataSetChanged();
        }
    }

    /*
    *  筛选
    * */
    @OnClick(R.id.search_filter)
    public void onOpenFilter() {
        typeL.setVisibility(View.GONE);
        orderL.setVisibility(View.GONE);
        filterL.setVisibility(filterL.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.filter_confirm)
    public void onFilterConfirm() {
        filterL.setVisibility(View.GONE);
    }

    @OnClick(R.id.filter_reset)
    public void onFilterReset() {
        for (Item item : filterList) {
            item.selectLabels.clear();
        }
        filterPriceMin.setText("");
        filterPriceMax.setText("");
        filterAdapter.notifyDataSetChanged();
    }

    /*
    *   滚动到顶
    * */
    @OnClick(R.id.search_list_to_top)
    public void onToTop() {
        resultsR.getLayoutManager().scrollToPosition(0);
    }

    private static class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {
        private List<Item> list;
        private Activity activity;

        public FilterAdapter(List<Item> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_search_filter, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final Item data = list.get(position);
            holder.arrow.setImageResource(holder.labels.getVisibility() == View.GONE ?
                    R.drawable.picture_orange_arrow_down : R.drawable.picture_orange_arrow_up);
            holder.titleL.setOnClickListener(v -> {
                holder.labels.setVisibility(holder.labels.getVisibility() == View.GONE ?
                        View.VISIBLE : View.GONE);
                holder.arrow.setImageResource(holder.labels.getVisibility() == View.GONE ?
                        R.drawable.picture_orange_arrow_down : R.drawable.picture_orange_arrow_up);
            });
            holder.labels.setTexts(data.labels);
            holder.labels.setOnClickItemListener((text, textView) -> {
                if (data.selectLabels.contains(text)) {
                    data.selectLabels.remove(text);
                    textView.setBackgroundResource(R.drawable.bg_rect);
                    textView.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
                } else {
                    data.selectLabels.add(text);
                    textView.setBackgroundResource(R.drawable.icon_filter_label_bg);
                    textView.setTextColor(ResUtil.getColor(R.color.main_red));
                }
            });
            holder.name.setText(data.name);
//            if (position==getItemCount()-1) CommonUtil.setMarginBottom(holder.itemView,R.dimen.dp_10);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            View titleL;
            TextView name;
            ImageView arrow;
            LabelLayout labels;

            public Holder(View itemView) {
                super(itemView);
                titleL = itemView.findViewById(R.id.item_arrowL);
                arrow = itemView.findViewById(R.id.item_arrow);
                labels = itemView.findViewById(R.id.item_labels);
                name = itemView.findViewById(R.id.item_name);
            }
        }
    }

    private static class Item {
        public String name = "阿迪达斯";
        public List<String> labels = new ArrayList<>();
        public List<String> selectLabels = new ArrayList<>();

        public Item() {
            for (int i = 0; i < 8; i++) {
                labels.add("hhhh" + (i * i));
            }
        }
    }

    private static class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.Holder> {
        private List<SearchGood> list;
        private Activity activity;
        public boolean isSingleList = true;

        public GoodsAdapter(List<SearchGood> list, Activity activity) {
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
            if (position == 0) {
                return;
            }
            SearchGood data = list.get(position);
            holder.goodImg.id(data.pic);
            holder.name.setText(data.title);
            holder.sale.setText(String.format("已售%d份 | 好评率%s", data.sales, data.goodRate + "%"));
            holder.delivery.setText(String.format("%.0f分钟 | %.2f km", data.deliveryTime, data.deliveryDistance/1000));
            holder.price.setText(CommonUtil.formatPrice(data.price));
            holder.platformDelivery.setVisibility("同城鸽专送".equals(data.deliveryWay) ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(v -> {
                GoodDetailActivity.startGoodDetailActivity(data.id,data.storeId);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? R.layout.item_search_result_title :
                    (isSingleList ? R.layout.item_search_result_goods : R.layout.item_search_result_goods_two);
        }


        class Holder extends RecyclerView.ViewHolder {
            View title;
            ImgView goodImg;
            TextView name;
            TextView sale;
            TextView delivery;
            TextView price;
            TextView platformDelivery;
            LabelLayout labels;

            public Holder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.item_title);

                goodImg = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                sale = itemView.findViewById(R.id.item_sale);
                delivery = itemView.findViewById(R.id.item_delivery);
                price = itemView.findViewById(R.id.item_price);
                platformDelivery = itemView.findViewById(R.id.item_platform_delivery);
                labels = itemView.findViewById(R.id.item_labels);
            }
        }
    }

}
