package cn.czyugang.tcg.client.modules.discount;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import io.reactivex.Observable;

/**
 * @author ruiaa
 * @date 2018/1/5
 */

public class SelectCouponFragment extends BaseFragment {

    @BindView(R.id.discount_select_coupon_list)
    RecyclerView couponR;
    Unbinder unbinder;
    private boolean isPlatform = false;
    private boolean isUseful = false;

    private List<Discount> couponList = new ArrayList<>();
    private MyCouponFragment.CouponAdapter adapter;
    private RefreshLoadHelper refreshLoadHelper;
    private DiscountsResponse discountsResponse=null;

    public static SelectCouponFragment newInstance(boolean isPlatform, boolean isUseful) {
        SelectCouponFragment fragment = new SelectCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPlatform", isPlatform);
        bundle.putBoolean("isUseful", isUseful);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPlatform = getArguments().getBoolean("isPlatform");
        isUseful = getArguments().getBoolean("isUseful");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discount_select_coupon, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        adapter=new MyCouponFragment.CouponAdapter(couponList,getActivity());
        adapter.showToUse=false;
        adapter.type=isUseful?1:2;
        couponR.setLayoutManager(new LinearLayoutManager(getActivity()));
        couponR.setAdapter(adapter);
        refreshLoadHelper=new RefreshLoadHelper(getActivity()).build(couponR);
        refreshLoadHelper.swipeToLoadLayout.setOnLoadMoreListener(()->getData(true));
        refreshLoadHelper.swipeToLoadLayout.setOnRefreshListener(()->getData(false));


        return rootView;
    }

    private void getData(boolean loadmore){
        int page=1;
        String time=null;
        if (loadmore&&discountsResponse!=null){
            page=discountsResponse.currentPage+1;
            time=discountsResponse.accessTime;
        }
        Observable<DiscountsResponse> observable=null;
        if (isPlatform){
            observable= DiscountApi.payPlatformCoupon(isUseful,page,time);
        }else {
            observable= DiscountApi.payStoreCoupon(isUseful,page,time);
        }
        observable.subscribe(new BaseActivity.NetObserver<DiscountsResponse>() {
            @Override
            public void onNext(DiscountsResponse response) {
                super.onNext(response);
                if (!ErrorHandler.judge200(response)) return;
                if (loadmore&&ErrorHandler.isRepeat(discountsResponse,response)) return;
                response.parse();
                if (loadmore)couponList.clear();
                couponList.addAll(response.data);
                adapter.notifyDataSetChanged();
                discountsResponse=response;
            }

            @Override
            public SwipeToLoadLayout getSwipeToLoadLayout() {
                return refreshLoadHelper.swipeToLoadLayout;
            }
        });
    }
    @Override
    public String getLabel() {
        isPlatform = getArguments().getBoolean("isPlatform");
        isUseful = getArguments().getBoolean("isUseful");
        return isUseful ? "可用优惠券" : "不可用优惠券";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
