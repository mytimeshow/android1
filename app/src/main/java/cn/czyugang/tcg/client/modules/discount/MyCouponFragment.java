package cn.czyugang.tcg.client.modules.discount;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.api.DiscountApi;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseFragment;
import cn.czyugang.tcg.client.common.ErrorHandler;
import cn.czyugang.tcg.client.entity.Discount;
import cn.czyugang.tcg.client.entity.DiscountsResponse;
import cn.czyugang.tcg.client.widget.RefreshLoadHelper;

/**
 * @author ruiaa
 * @date 2018/1/5
 */

public class MyCouponFragment extends BaseFragment {

    @BindView(R.id.discount_my_coupon_list)
    RecyclerView couponR;
    Unbinder unbinder;

    private int type = 1;  //  1 未使用       2 已使用       3 已过期
    private List<Discount> couponList = new ArrayList<>();
    private CouponAdapter adapter;
    private RefreshLoadHelper refreshLoadHelper;
    private DiscountsResponse discountsResponse = null;


    public static MyCouponFragment newInstance(int type) {
        MyCouponFragment fragment = new MyCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discount_my_coupon, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        adapter = new CouponAdapter(couponList, getActivity());
        adapter.type = type;
        couponR.setLayoutManager(new LinearLayoutManager(getActivity()));
        couponR.setAdapter(adapter);
        refreshLoadHelper = new RefreshLoadHelper(getActivity()).build(couponR);
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(() -> getData(true));
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(() -> getData(false));


        return rootView;
    }

    private void getData(boolean loadmore) {
        int page = 1;
        String time = null;
        if (loadmore && discountsResponse != null) {
            page = discountsResponse.currentPage + 1;
            time = discountsResponse.accessTime;
        }
        DiscountApi.myCoupon(type, page, time).subscribe(new BaseActivity.NetObserver<DiscountsResponse>() {
            @Override
            public void onNext(DiscountsResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (loadmore && ErrorHandler.isRepeat(discountsResponse, response)) return;
                response.parse();
                if (!loadmore) couponList.clear();
                couponList.addAll(response.data);
                couponList.add(new Discount());
                adapter.notifyDataSetChanged();
                discountsResponse = response;
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }

    @Override
    public String getLabel() {
        type = getArguments().getInt("type");
        return type == 1 ? "未使用" : (type == 2 ? "已使用" : "已过期");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.Holder> {
        private List<Discount> list;
        private Activity activity;
        public int type = 1;//  1 未使用       2 已使用       3 已过期    //  1 可用      2 不可用
        public boolean showToUse = true;
        public int selectPosition=-1;

        public CouponAdapter(List<Discount> list, Activity activity) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(activity).inflate(
                    viewType, parent, false));
        }

        @Override
        public int getItemViewType(int position) {
            return position == getItemCount() - 1 ? R.layout.item_discout_open_get_coupon : R.layout.item_discount_coupon;
        }

        @Override
        public int getItemCount() {
            return list.size() + (showToUse ? 1 : 0);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            if (holder.openGetCoupon != null) {
                holder.openGetCoupon.setOnClickListener(v -> GetCouponActivity.startGetCouponActivity());
                return;
            }


            Discount data = list.get(position);

            if (showToUse){
                //我的优惠券
                holder.tagUsed.setVisibility( type == 2 ? View.VISIBLE : View.GONE);
                holder.toUse.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
                holder.toUse.setOnClickListener(v -> {

                });
            }else {
                //下单
                if (type == 1){
                    holder.selected.setVisibility(selectPosition==position ? View.VISIBLE : View.GONE);
                    holder.itemView.setOnClickListener(v -> {
                        selectPosition=holder.getAdapterPosition();
                        notifyItemChanged(holder.getAdapterPosition());
                    });
                }
            }


            //非可用优惠券，置灰
            if (type != 1) {
                holder.bgPrice.setBackgroundResource(R.color.text_gray);
                holder.bgPriceLeft.setBackgroundResource(R.color.text_gray);
            }

            holder.openDetail.setOnClickListener(v -> {
                if (holder.detail.getVisibility() == View.GONE) {
                    holder.barDetail.setVisibility(View.VISIBLE);
                    holder.detail.setVisibility(View.VISIBLE);
                    holder.openDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_up_circle, 0);
                } else {
                    holder.barDetail.setVisibility(View.GONE);
                    holder.detail.setVisibility(View.GONE);
                    holder.openDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down_circle, 0);
                }
            });

        }

        class Holder extends RecyclerView.ViewHolder {
            View tagNewUser;
            View bgPriceLeft;
            View bgPrice;
            TextView price;
            TextView name;
            TextView time;
            TextView openDetail;
            View barDetail;
            TextView detail;
            View toUse;
            View selected;
            View tagUsed;

            View openGetCoupon;

            public Holder(View itemView) {
                super(itemView);
                tagNewUser = itemView.findViewById(R.id.item_tag_new_user);
                bgPriceLeft = itemView.findViewById(R.id.item_price_bg_left);
                bgPrice = itemView.findViewById(R.id.item_price_bg);
                price = itemView.findViewById(R.id.item_price);
                name = itemView.findViewById(R.id.item_name);
                time = itemView.findViewById(R.id.item_time);
                openDetail = itemView.findViewById(R.id.item_open_detail);
                barDetail = itemView.findViewById(R.id.item_bar);
                detail = itemView.findViewById(R.id.item_detail);
                toUse = itemView.findViewById(R.id.item_use);
                selected = itemView.findViewById(R.id.item_select);
                tagUsed = itemView.findViewById(R.id.item_tag_used);

                openGetCoupon = itemView.findViewById(R.id.item_discout_open_get_coupon);
            }
        }
    }
}
