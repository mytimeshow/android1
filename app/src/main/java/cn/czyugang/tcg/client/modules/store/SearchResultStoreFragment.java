package cn.czyugang.tcg.client.modules.store;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.SearchStore;
import cn.czyugang.tcg.client.entity.SearchStoreResponse;
import cn.czyugang.tcg.client.utils.AmapLocationUtil;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.app.ResUtil;
import cn.czyugang.tcg.client.utils.img.ImgView;
import cn.czyugang.tcg.client.widget.CalculateOrderView;
import cn.czyugang.tcg.client.widget.FiveStarView;
import cn.czyugang.tcg.client.widget.LabelLayout;
import cn.czyugang.tcg.client.widget.RecycleViewDivider;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

/**
 * @author ruiaa
 * @date 2017/12/20
 */

public class SearchResultStoreFragment extends BaseFragment {

    @BindView(R.id.search_type)
    TextView type;
    @BindView(R.id.search_order_price)
    TextView orderPrice;
    @BindView(R.id.search_order_sale)
    TextView orderSale;

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
    @BindView(R.id.search_orderL)
    LinearLayout orderL;


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
    private RecyclerView.Adapter adapter;
    private List<SearchStore> storeList = new ArrayList<>();
    private SearchStoreResponse searchResponse;
    private RefreshLoadHelper refreshLoadHelper;

    private String resultType = null;   //TAKEOUT-外卖; MARKET-商超;不传为全部
    private String orderType = null;    //LOCATION-距离最近   GOOD_RATE-好评   PRICE_DESC-价格从低到高   PRICE_ASC-价格从高到低  SALE- 销量  不传按发布时间
    private String searchKey = null;

    public static SearchResultStoreFragment newInstance() {
        SearchResultStoreFragment fragment = new SearchResultStoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_store_result, container, false);
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


        storeList.add(null);

        adapter = new StoreAdapter(storeList, getActivity());
        resultsR.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsR.setAdapter(adapter);
        refreshLoadHelper = new RefreshLoadHelper(getActivity()).build(resultsR);
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> refreshResult(true));
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> refreshResult(false));

        preSearch();
        initRefresh();

        return rootView;
    }

    @Override
    public String getLabel() {
        return "店铺";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
    *   数据获取
    * */
    private void preSearch() {
        StoreApi.searchPre().subscribe(new BaseActivity.NetObserver<Response<Object>>() {
            @Override
            public void onNext(Response<Object> response) {
                super.onNext(response);
            }
        });
    }

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
        map.put("pageSize", 15);
        map.put("lat", AmapLocationUtil.getLatitude());
        map.put("lon", AmapLocationUtil.getLongitude());
        map.put("title", searchKey);
        if (orderType != null) map.put("orderType", orderType);
        if (resultType != null) map.put("searchType", resultType);
        StoreApi.searchStore(map).subscribe(new BaseActivity.NetObserver<SearchStoreResponse>() {
            @Override
            public void onNext(SearchStoreResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    if (response.data.isEmpty()) {
                        if (loadmore) AppUtil.toast("没有更多了");
                        return;
                    }
                    response.parse();
                    if (!loadmore) {
                        storeList.clear();
                        storeList.add(null);
                        storeList.addAll(response.data);
                        response.currentPage = 1;
                    } else {
                        storeList.addAll(response.data);
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
    *  好评,距离最近,销量
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
            orderPraiseSelect.setImageDrawable(null);
            orderDistanceSelect.setImageDrawable(null);
            orderDistanceText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderPraiseText.setTextColor(ResUtil.getColor(R.color.text_dark_gray));
            orderSale.setTextColor(ResUtil.getColor(R.color.main_red));
        } else {
            if (selectPrice == null) return;
            orderPrice.setTextColor(ResUtil.getColor(R.color.main_red));
            orderPrice.setText(selectPrice.getText());
            orderPraiseSelect.setImageDrawable(null);
            orderDistanceSelect.setImageDrawable(null);
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
        public FilterAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FilterAdapter.Holder(LayoutInflater.from(activity).inflate(
                    R.layout.item_search_filter, parent, false));
        }

        @Override
        public void onBindViewHolder(FilterAdapter.Holder holder, int position) {
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

    private static class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.Holder> {
        private List<SearchStore> list;
        private Activity activity;

        public StoreAdapter(List<SearchStore> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public StoreAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StoreAdapter.Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(StoreAdapter.Holder holder, int position) {
            if (position == 0) {
                return;
            }
            SearchStore data = list.get(position);

            holder.storeImg.id(data.pic);
            holder.name.setText(data.title);
            holder.deliveryTime.setText(String.format("%.0f分钟 | %.2f km", data.deliveryTime, data.distance / 1000));
            holder.stars.setScore(data.score);
            holder.sale.setText(String.format("| 已售%d份", data.saled));
            holder.deliveryPrice.setText(String.format("起步￥%.2f | 配送￥%.2f起", data.startDeliveryPrice, data.deliveryPrice));

            holder.activities.clear();
            holder.activities.addActivityItem("减", "满20减5；满45减20");
            holder.activities.addActivityItem("减", "满20减5；满45减20；满90减50");
            holder.activities.addActivityItem("减", "满20减5；满45减20；满90减50；满500减110...");
            holder.activities.build();

            data.bindSearchResultGoodsImg(activity, holder.goodsImgs);

            holder.itemView.setOnClickListener(v -> {
                StoreActivity.startStoreActivity(data.storeId);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? R.layout.item_search_result_title : R.layout.item_search_result_store;
        }

        class Holder extends RecyclerView.ViewHolder {
            View title;
            ImgView storeImg;
            TextView name;
            TextView deliveryTime;
            TextView sale;
            FiveStarView stars;
            TextView deliveryPrice;
            CalculateOrderView activities;
            RecyclerView goodsImgs;

            public Holder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.item_title);
                storeImg = itemView.findViewById(R.id.item_img);
                name = itemView.findViewById(R.id.item_name);
                deliveryTime = itemView.findViewById(R.id.item_delivery_time);
                sale = itemView.findViewById(R.id.item_sale);
                stars = itemView.findViewById(R.id.item_star);
                deliveryPrice = itemView.findViewById(R.id.item_delivery_price);
                activities = itemView.findViewById(R.id.item_activities);
                goodsImgs = itemView.findViewById(R.id.item_goods);
            }
        }
    }
}