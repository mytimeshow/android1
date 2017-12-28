package cn.czyugang.tcg.client.modules.entry.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.RecordApi;
import cn.czyugang.tcg.client.api.StoreApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Response;
import cn.czyugang.tcg.client.entity.TrolleyCheckResponse;
import cn.czyugang.tcg.client.entity.TrolleyGoods;
import cn.czyugang.tcg.client.entity.TrolleyResponse;
import cn.czyugang.tcg.client.entity.TrolleyStore;
import cn.czyugang.tcg.client.modules.common.dialog.MyDialog;
import cn.czyugang.tcg.client.modules.common.dialog.StoreTrolleyDialog;
import cn.czyugang.tcg.client.modules.order.ConfirmOrderActivity;
import cn.czyugang.tcg.client.modules.store.GoodDetailActivity;
import cn.czyugang.tcg.client.modules.store.StoreActivity;
import cn.czyugang.tcg.client.utils.CommonUtil;
import cn.czyugang.tcg.client.utils.LogRui;
import cn.czyugang.tcg.client.utils.app.AppUtil;
import cn.czyugang.tcg.client.utils.rxbus.RxBus;
import cn.czyugang.tcg.client.utils.rxbus.TrolleyBuyNumChangedEvent;
import cn.czyugang.tcg.client.utils.string.RichText;
import cn.czyugang.tcg.client.widget.ActivityTextView;
import cn.czyugang.tcg.client.widget.BottomBalanceView;
import cn.czyugang.tcg.client.widget.GoodsPlusMinusView;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;
import cn.czyugang.tcg.client.widget.SelectButton;

/**
 * @author ruiaa
 * @date 2017/11/20
 */

public class TrolleyFragment extends BaseFragment {
    @BindView(R.id.title_right)
    TextView edit;
    @BindView(R.id.trolley_store_list)
    RecyclerView storeR;
    @BindView(R.id.trolley_money)
    TextView money;
    @BindView(R.id.trolley_buy)
    TextView toBuy;
    @BindView(R.id.trolley_bottom_edit)
    LinearLayout bottomEditL;
    private Unbinder unbinder;

    private List<TrolleyStore> trolleyList = new ArrayList<>();
    private TrolleyAdapter adapter;
    private RefreshLoadHelper refreshLoadHelper;
    private TrolleyResponse trolleyResponse = null;

    public static TrolleyFragment newInstance() {
        TrolleyFragment fragment = new TrolleyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable.add(RxBus.toObservable(TrolleyBuyNumChangedEvent.class)
                .subscribe(event -> {

                }));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trolley, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        adapter = new TrolleyAdapter(trolleyList, getActivity());
        storeR.setLayoutManager(new LinearLayoutManager(getActivity()));
        storeR.setAdapter(adapter);
        refreshLoadHelper = new RefreshLoadHelper(getActivity(), true, false).build(storeR);
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> syncAndGetTrolley(false));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        syncAndGetTrolley(false);
    }

    private void syncAndGetTrolley(boolean loadmore) {
        if (trolleyResponse == null) {
            getTrolley(loadmore);
        } else {
            StoreApi.syncTrolleyGoods(new ArrayList<>(trolleyResponse.storeHashMap.values())).subscribe(new BaseActivity.NetObserver<Response<List<TrolleyGoods>>>() {
                @Override
                public void onNext(Response<List<TrolleyGoods>> response) {
                    super.onNext(response);
                    if (ErrorHandler.judge200(response)) getTrolley(loadmore);
                }
            });
        }
    }

    private void getTrolley(boolean loadmore) {
        StoreApi.getTrolley(null).subscribe(new BaseActivity.NetObserver<TrolleyResponse>() {
            @Override
            public void onNext(TrolleyResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                response.parse();
                trolleyList.clear();
                trolleyList.addAll(response.storeHashMap.values());
                trolleyResponse = response;
                adapter.notifyData();
                refreshBottom();
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void refreshItemChange(TrolleyStore trolleyStore) {
        if (!trolleyStore.hasNormalGoods()) {
            int p = trolleyList.indexOf(trolleyStore);
            if (p >= 0) {
                trolleyList.remove(p);
                adapter.notifyItemRemoved(p);
            }
        }
        refreshBottom();
    }

    private void refreshBottom() {
        money.setText(RichText.newRichText("合计：")
                .appendSpColorRes(CommonUtil.formatPrice(TrolleyStore.getAllPrice(trolleyList)), R.dimen.sp_18, R.color.text_black)
                .append("\n")
                .appendColorRes("不含配送费", R.color.text_gray)
                .build());

        int num = TrolleyStore.getAllGoodsBuyNum(trolleyList);
        toBuy.setText(String.format("结算(%d)", num));
        toBuy.setClickable(num > 0);
    }

    @OnClick(R.id.trolley_buy)
    public void onCommit() {
        if (trolleyList == null) return;
        List<String> cards = new ArrayList<>();
        for (TrolleyStore t : trolleyList) {
            for (TrolleyGoods goods : t.trolleyGoodsMap.values()) {
                if (!goods.hadDeleted() && goods.isSelect && !goods.trolleyId.equals("")) {
                    cards.add(goods.trolleyId);
                }
            }
        }
        String shoppingCartIds = CommonUtil.listIdsToStr(cards);
        if (shoppingCartIds.isEmpty()) return;
        StoreApi.checkTrolley(shoppingCartIds).subscribe(new BaseActivity.NetObserver<TrolleyCheckResponse>() {
            @Override
            public void onNext(TrolleyCheckResponse response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    LogRui.d("****onNext####结算前检查购物车");
                    if (response.errorTrolleyGoods.isEmpty()) {
                        ConfirmOrderActivity.startConfirmOrderActivity(shoppingCartIds);
                    }
                }
            }
        });
    }

    @OnClick(R.id.title_right)
    public void onEdit() {
        if (bottomEditL.getVisibility() == View.GONE) {
            bottomEditL.setVisibility(View.VISIBLE);
            edit.setText("完成");
        } else {
            bottomEditL.setVisibility(View.GONE);
            edit.setText("编辑");
        }
    }

    @OnClick(R.id.trolley_bottom_select)
    public void onSelectAll(View v) {
        if (trolleyList == null) return;
        for (TrolleyStore t : trolleyList) {
            t.selectAll(((CompoundButton) v).isChecked());
        }
        adapter.notifyDataSetChanged();
        refreshBottom();
    }

    @OnClick(R.id.trolley_collect)
    public void onCollect() {
        onEdit();
        if (trolleyList == null) return;
        List<String> goodIds = new ArrayList<>();
        for (TrolleyStore t : trolleyList) {
            for (TrolleyGoods goods : t.trolleyGoodsMap.values()) {
                goodIds.add(goods.goodId);
            }
        }
        RecordApi.collect("PRODUCT", goodIds).subscribe(new BaseActivity.NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                super.onNext(response);
                if (ErrorHandler.judge200(response)) {
                    AppUtil.toast("成功收藏商品");
                }
            }

            @Override
            public boolean showLoading() {
                return false;
            }
        });
    }

    @OnClick(R.id.trolley_delete)
    public void onDelete() {
        onEdit();
        if (trolleyList == null) return;
        for (TrolleyStore t : trolleyList) {
            t.deleteAll();
        }
        adapter.notifyData();
    }

    private class TrolleyAdapter extends RecyclerView.Adapter<TrolleyAdapter.Holder> {
        private List<TrolleyStore> list;
        private Activity activity;

        public TrolleyAdapter(List<TrolleyStore> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        private void notifyData() {
            Iterator<TrolleyStore> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().hasNormalGoods()) iterator.remove();
            }
            notifyDataSetChanged();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            TrolleyStore data = list.get(position);
            if (data.viewType == R.layout.item_trolley_store_invalid_title) return;
            if (data.viewType == R.layout.item_trolley_store_invalid_clear) {
                return;
            }
            if (data.viewType == R.layout.item_trolley_store_ad) {
                return;
            }

            holder.selectButton.setChecked(data.selected);
            holder.selectButton.setOnClickListener(v -> {
                data.selectAll(((CompoundButton) v).isChecked());
                notifyItemChanged(list.indexOf(data));
                refreshBottom();
            });

            holder.name.setText(data.store.name);
            holder.name.setOnClickListener(v -> StoreActivity.startStoreActivity(data.store.id));

            data.bindGoodsAdapter(activity, holder.goodsR, false, data);
            data.adapter.trolleyFragment = TrolleyFragment.this;

            holder.packFeeAsk.setOnClickListener(v -> {
                MyDialog.bubbleToast(activity, v, " ");
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).viewType;
        }

        class Holder extends RecyclerView.ViewHolder {
            SelectButton selectButton;
            TextView name;
            View moreActivityL;
            ActivityTextView moreActivity;
            TextView moreActivityAction;
            RecyclerView goodsR;
            View useActivityL;
            ActivityTextView useActivity;
            TextView fixActivity;
            View packFeeL;
            TextView packFee;
            View packFeeAsk;

            public Holder(View itemView) {
                super(itemView);
                selectButton = itemView.findViewById(R.id.item_select);
                name = itemView.findViewById(R.id.item_name);
                moreActivityL = itemView.findViewById(R.id.item_more_activityL);
                moreActivity = itemView.findViewById(R.id.item_more_activity);
                moreActivityAction = itemView.findViewById(R.id.item_more_activity_add);
                goodsR = itemView.findViewById(R.id.item_goods);
                useActivityL = itemView.findViewById(R.id.item_activity_useL);
                useActivity = itemView.findViewById(R.id.item_activity_use);
                fixActivity = itemView.findViewById(R.id.item_activity_fix);
                packFeeL = itemView.findViewById(R.id.item_pack_feeL);
                packFee = itemView.findViewById(R.id.item_pack_fee);
                packFeeAsk = itemView.findViewById(R.id.item_pack_fee_ask);

            }
        }
    }

    public static class TrolleyGoodsAdapter extends RecyclerView.Adapter<TrolleyGoodsAdapter.Holder> {
        private List<TrolleyGoods> list;
        private Activity activity;
        private TrolleyStore trolleyStore;
        public boolean isInStore = false;
        private BottomBalanceView bottomBalanceView;
        private StoreTrolleyDialog storeTrolleyDialog = null;
        public TrolleyFragment trolleyFragment = null;


        public TrolleyGoodsAdapter(List<TrolleyGoods> list, Activity activity, TrolleyStore trolleyStore) {
            this.list = list;
            this.activity = activity;
            this.trolleyStore = trolleyStore;
        }

        public TrolleyGoodsAdapter setBottomBalanceView(BottomBalanceView bottomBalanceView) {
            this.bottomBalanceView = bottomBalanceView;
            return this;
        }

        public TrolleyGoodsAdapter setStoreTrolleyDialog(StoreTrolleyDialog storeTrolleyDialog) {
            this.storeTrolleyDialog = storeTrolleyDialog;
            return this;
        }

        private void refreshOtherOnChange() {
            if (bottomBalanceView != null) bottomBalanceView.refresh();
            if (storeTrolleyDialog != null) storeTrolleyDialog.refreshPackFee();
            if (trolleyFragment != null) trolleyFragment.refreshItemChange(trolleyStore);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            TrolleyGoods data = list.get(position);

            holder.plusMinusView.setNum(data.num);
            holder.plusMinusView
                    .setIsMultiSpec(false)
                    .setOnPlusMinusListener(addNum -> {     //购物车
                        trolleyStore.addGood(data, addNum);
                        refreshOtherOnChange();
                        return data.num;
                    })
                    .setNum(data.num);

            holder.hotTag.setVisibility(View.GONE);
            holder.name.setText(data.name);
            holder.spec.setText(data.spec);
            holder.discountTag.setVisibility(View.GONE);
            holder.price.setText(data.getPriceStr());
            if (holder.activityPrice != null) holder.activityPrice.setVisibility(View.GONE);
            if (holder.activityTime != null) holder.activityTime.setVisibility(View.GONE);

            holder.delete.setOnClickListener(v -> {
                trolleyStore.deleteGood(data);
                notifyDataSetChanged();
                refreshOtherOnChange();
            });
            if (holder.collect != null) {
                holder.collect.setOnClickListener(v -> {
                    RecordApi.collect("PRODUCT", data.goodId).subscribe(new BaseActivity.NetObserver<Response>() {
                        @Override
                        public void onNext(Response response) {
                            super.onNext(response);
                            if (ErrorHandler.judge200(response))
                                AppUtil.toast("成功收藏");
                        }

                        @Override
                        public boolean showLoading() {
                            return false;
                        }
                    });
                });
                holder.itemView.setOnClickListener(v -> GoodDetailActivity.startGoodDetailActivity(data.goodId, data.storeId));
            }

            holder.selectButton.setChecked(data.isSelect);
            holder.selectButton.setOnClickListener(v -> {
                data.isSelect = ((CompoundButton) v).isChecked();
                refreshOtherOnChange();
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return isInStore ? R.layout.item_trolley_store_goods : R.layout.item_trolley_goods;
        }

        class Holder extends RecyclerView.ViewHolder {
            SelectButton selectButton;
            GoodsPlusMinusView plusMinusView;
            View hotTag;
            TextView name;
            TextView spec;
            View editSpec;
            TextView discountTag;
            TextView price;
            TextView activityPrice;
            TextView activityTime;
            TextView delete;
            TextView collect;

            public Holder(View itemView) {
                super(itemView);
                selectButton = itemView.findViewById(R.id.item_select);
                plusMinusView = itemView.findViewById(R.id.item_plus_minus);
                hotTag = itemView.findViewById(R.id.item_hot_tag);
                name = itemView.findViewById(R.id.item_name);
                spec = itemView.findViewById(R.id.item_spec);
                editSpec = itemView.findViewById(R.id.item_spec_edit);
                discountTag = itemView.findViewById(R.id.item_tag);
                price = itemView.findViewById(R.id.item_price);
                activityPrice = itemView.findViewById(R.id.item_activity_price);
                activityTime = itemView.findViewById(R.id.item_activity_price_time);
                delete = itemView.findViewById(R.id.item_delete);
                collect = itemView.findViewById(R.id.item_collect);
            }
        }
    }
}
